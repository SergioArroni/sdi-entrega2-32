const {ObjectId} = require("mongodb");
module.exports = function (app, usersRepository, friendsRepository) {

    app.get("/api/v1.0/friendlist", function (req, res) {
        let user = res.user;
        let filter = {email: user}
        let options = {};
        usersRepository.getUsers(filter, options).then(user => {
            let id = user[0]._id;
            let filter1 = {id_from: id};
            let filter2 = {id_to: id};
            friendsRepository.getFriends(filter1, filter2, options).then(friends => {
                let ids = new Array();
                for (let i = 0; i < friends.length; i++) {
                    if (friends[i].accept) {
                        if (friends[i].id_from.equals(id)) {
                            ids.push(friends[i].id_to);
                        } else {
                            ids.push(friends[i].id_from);
                        }
                    }
                }
                usersRepository.getFriends(ids).then(users => {
                    res.status(200);
                    res.send({users: users});
                }).catch(error => {
                    res.status(500);
                    res.json({error: "Se ha producido un error al cargar los usuarios."})
                });
            }).catch(error => {
                res.status(500);
                res.json({error: "Se ha producido un error al cargar los usuarios."})
            });
        }).catch(error => {
            res.status(500);
            res.json({error: "Se ha producido un error al cargar los usuarios."})
        });
    });

    app.post('/api/v1.0/users/login', function (req, res) {
        try {
            let securePassword = app.get("crypto").createHmac('sha256', app.get('clave'))
                .update(req.body.password).digest('hex');
            let filter = {
                email: req.body.email,
                password: securePassword
            }
            let options = {}
            usersRepository.findUser(filter, options).then(user => {
                if (user == null) {
                    req.status(401);
                    res.json({
                        message: "Usuario no identificado",
                        authenticated: false
                    })
                } else {
                    let token = app.get('jwt').sign(
                        {user: user.email, time: Date.now() / 1000},
                        "secreto");
                    res.status(200);
                    res.json({
                        message: "Usuario autorizado",
                        authenticated: true,
                        token: token
                    });
                }
            }).catch(error => {
                res.status(401);
                res.json({
                    message: "Se ha producido un error al verificar credenciales",
                    authenticated: false
                })
            })
        } catch (e) {
            res.status(500);
            res.json({
                message: "Se ha producido un error al verificar credenciales",
                authenticated: false
            })
        }
    });

    app.post("/api/v1.0/message/:id", function (req, res) {
        let user = res.user;
        let filter = {email: user}
        let options = {};
        if (req.body.text.trim() == "") {
            res.status(409);
            res.json({error: "El texto no puede ser vacío"});
        } else {
            usersRepository.getUsers(filter, options).then(user => {
                let id = user[0]._id;
                let idFriend = ObjectId(req.params.id);
                let filter1 = {id_from: id, id_to: idFriend};
                let filter2 = {id_to: id, id_from: idFriend};
                options = {};
                const time = Date.now();
                let date = new Date(time);
                friendsRepository.getFriends(filter1, filter2, options).then(friend => {
                    let sonAmigos = friend[0].accept;
                    if (friend != null) {
                        if (sonAmigos) {
                            let message = {
                                id_from: id,
                                id_to: idFriend,
                                text: req.body.text,
                                saw: false,
                                date: date
                            }
                            usersRepository.insertMessage(message, function (messageId) {
                                if (messageId === null) {
                                    res.status(409);
                                    res.json({error: "No se ha podido enviar el mensaje."});
                                } else {
                                    res.status(201);
                                    res.json({
                                        message: "Mensaje enviado correctamente.",
                                        _id: messageId
                                    });
                                }
                            });
                        } else {
                            res.status(422);
                            res.json({error: "No sois amigos."})
                        }
                    } else {
                        res.status(422);
                        res.json({error: "No sois amigos."})
                    }
                }).catch(error => {
                    res.status(500);
                    res.json({error: "Se ha producido un error al enviar el mensaje."})
                });
            }).catch(error => {
                res.status(500);
                res.json({error: "Se ha producido un error al enviar el mensaje."})
            });
        }
    });

    app.get("/api/v1.0/message/list/:id", function (req, res) {
        let user = res.user;
        let filter = {email: user}
        let options = {};
        usersRepository.getUsers(filter, options).then(user => {
            let id = user[0]._id;
            let idFriend = ObjectId(req.params.id);
            let filter1 = {id_from: id, id_to: idFriend};
            let filter2 = {id_to: id, id_from: idFriend};
            options = {};
            friendsRepository.getFriends(filter1, filter2, options).then(friend => {
                let sonAmigos = friend[0].accept;
                if (sonAmigos) {
                    usersRepository.getMessages(filter1, filter2, options).then(messages => {
                        res.status(200);
                        res.send({messages: messages});
                    }).catch(error => {
                        res.status(500);
                        res.json({error: "Se ha producido un error al cargar los usuarios."})
                    });
                } else {
                    res.status(422);
                    res.json({error: "No sois amigos."})
                }
            }).catch(error => {
                res.status(500);
                res.json({error: "Se ha producido un error al enviar el mensaje."})
            });
        }).catch(error => {
            res.status(500);
            res.json({error: "Se ha producido un error al enviar el mensaje."})
        });
    });

    app.put("/api/v1.0/message/:id", function (req, res) {
        let user = res.user
        let filter = {email: user}
        let options = {}
        usersRepository.getUsers(filter, options).then(user => {
            let id = user[0]._id;
            let messageId = ObjectId(req.params.id);
            filter = {_id: messageId};
            //Si la _id NO no existe, no crea un nuevo documento.
            const options = {upsert: false};

            usersRepository.getMessage(filter, {}).then(message => {
                let id_to = message.id_to
                if (!id.equals(id_to)) {
                    res.status(403);
                    res.json({error: "No es el receptor del mensaje."});
                } else {
                    let m = {
                        saw: true
                    }
                    usersRepository.readMessage(m, filter, options).then(result => {

                        //La _id No existe o los datos enviados no difieren de los ya almacenados.
                        if (result.modifiedCount == 0) {
                            res.status(409);
                            res.json({error: "No se ha modificado ningun mensaje."});
                        } else {
                            res.status(200);
                            res.json({
                                message: "Mensaje leído correctamente."
                            })
                        }
                    }).catch(error => {
                        res.status(500);
                        res.json({error: "Se ha producido un error al leer el mensaje: " + error})
                    }).catch(error => {
                        res.status(500);
                        res.json({error: "Se ha producido un error al leer el mensaje: " + error})
                    });
                }
            }).catch(error => {
                res.status(500);
                res.json({error: "Se ha producido un error al leer el mensaje: " + error})
            });
        });
    });

    app.put("/api/v1.0/message", function (req, res) {
        let user = res.user
        let filter = {email: user}
        let options = {}
        usersRepository.getUsers(filter, options).then(user => {
            let id = user[0]._id;
            //let messageId = ObjectId(req.params.id);
            let idFriend = ObjectId(req.body.idFriend);
            let filter = {id_to: id, id_from: idFriend};
            //filter = {_id: messageId};
            //Si la _id NO no existe, no crea un nuevo documento.
            const options = {upsert: false};

            usersRepository.getMessages(filter, {}).then(message => {
                /*let id_to = message.id_to
                if (!id.equals(id_to)) {
                    res.status(403);
                    res.json({error: "No es el receptor del mensaje."});*/
               
                let m = {
                    saw: true
                }
                usersRepository.readMessages(m, filter, options).then(result => {

                    //La _id No existe o los datos enviados no difieren de los ya almacenados.
                    if (result.modifiedCount == 0) {
                        res.status(409);
                        res.json({error: "No se ha modificado ningun mensaje."});
                    } else {
                        res.status(200);
                        res.json({
                            message: "Mensajes leídos correctamente."
                        })
                    }
                }).catch(error => {
                    res.status(500);
                    res.json({error: "Se ha producido un error al leer el mensaje: " + error})
                }).catch(error => {
                    res.status(500);
                    res.json({error: "Se ha producido un error al leer el mensaje: " + error})
                });
            }).catch(error => {
                res.status(500);
                res.json({error: "Se ha producido un error al leer el mensaje: " + error})
            });
        });
    });
}