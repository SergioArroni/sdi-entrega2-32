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
            options = {};
            friendsRepository.getFriends(filter1, filter2, options).then(friends => {
                let ids = new Array();
                for(let i = 0; i < friends.length; i++){
                    if(friends[i].accept) {
                        if (friends[i].id_from.equals(id)) {
                            ids.push(friends[i].id_to);
                        } else {
                            ids.push(friends[i].id_from);
                        }
                    }
                }
                usersRepository.getFriends(ids).then(users =>{
                    //logger.debug("/api/v1.0/friendlist GET request, 200");
                    res.status(200);
                    res.send({users: users});
                }).catch(error => {
                    //logger.error("Error, Se ha producido un error al cargar los usuarios, 500");
                    res.status(500);
                    res.json({ error: "Se ha producido un error al cargar los usuarios." })
                });
            }).catch(error => {
                //logger.error("Error, Se ha producido un error al cargar los usuarios, 500");
                res.status(500);
                res.json({ error: "Se ha producido un error al cargar los usuarios." })
            });
        }).catch(error => {
            //logger.error("Error, Se ha producido un error al cargar los usuarios, 500");
            res.status(500);
            res.json({ error: "Se ha producido un error al cargar los usuarios." })
        });
    });

    app.post('/api/v1.0/users/login',function (req, res){
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
                    //logger.error("Error, Usuario no identificado, 401");
                    req.status(401);
                    res.json({
                        message: "Usuario no identificado",
                        authenticated: false
                    })
                } else {
                    let token = app.get('jwt').sign(
                        {user: user.email, time: Date.now() / 1000},
                        "secreto");
                    //logger.debug("/api/v1.0/users/login POST request, 200");
                    res.status(200);
                    res.json({
                        message: "Usuario autorizado",
                        authenticated: true,
                        token: token
                    });
                }
            }).catch(error => {
                //logger.error("Error, Se ha producido un error al verificar credenciales, 401");
                res.status(401);
                res.json({
                    message: "Se ha producido un error al verificar credenciales",
                    authenticated: false
                })
            })
        } catch(e){
            //logger.error("Error, Se ha producido un error al verificar credenciales, 500");
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
            //logger.error("Error, El texto no puede ser vacío, 409");
            res.status(409);
            res.json({error: "El texto no puede ser vacío"});
        } else{
            usersRepository.getUsers(filter, options).then(user => {
                let id = user[0]._id;
                let idFriend = ObjectId(req.params.id);
                let filter1 = {id_from: id, id_to: idFriend};
                let filter2 = {id_to: id, id_from: idFriend};
                options = {};
                friendsRepository.getFriends(filter1, filter2, options).then(friend => {
                    let sonAmigos = friend[0].accept;
                    if (friend != null) {
                        if (sonAmigos) {
                            let message = {
                                id_from: id,
                                id_to: idFriend,
                                text: req.body.text,
                                saw: false
                            }
                            usersRepository.insertMessage(message, function (messageId) {
                                if (messageId === null) {
                                    //logger.error("Error, Se ha producido un error al enviar el mensaje, 409");
                                    res.status(409);
                                    res.json({error: "No se ha podido enviar el mensaje."});
                                } else {
                                    //logger.debug("/api/v1.0/message/:id POST request, 201");
                                    res.status(201);
                                    res.json({
                                        message: "Mensaje enviado correctamente.",
                                        _id: messageId
                                    });
                                }
                            });
                        } else {
                            //logger.error("Error, No sois amigos, 422");
                            res.status(422);
                            res.json({error: "No sois amigos."})
                        }
                    } else {
                        //logger.error("Error, No sois amigos, 422");
                        res.status(422);
                        res.json({error: "No sois amigos."})
                    }
                }).catch(error => {
                    //logger.error("Error, Se ha producido un error al enviar el mensaje, 500");
                    res.status(500);
                    res.json({error: "Se ha producido un error al enviar el mensaje."})
                });
            }).catch(error => {
                //logger.error("Error, Se ha producido un error al enviar el mensaje, 500");
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
                if(sonAmigos) {
                    usersRepository.getMessages(filter1, filter2, options).then(messages => {
                        //logger.debug("/api/v1.0/message/list/:id GET request, 200");
                        res.status(200);
                        res.send({messages: messages});
                    }).catch(error => {
                        //logger.error("Error, Se ha producido un error al cargar los usuarios, 500");
                        res.status(500);
                        res.json({error: "Se ha producido un error al cargar los usuarios."})
                    });
                }
                else{
                    //logger.error("Error, No sois amigos, 422");
                    res.status(422);
                    res.json({ error: "No sois amigos." })
                }
            }).catch(error => {
                //logger.error("Error, Se ha producido un error al enviar el mensaje, 500");
                res.status(500);
                res.json({ error: "Se ha producido un error al enviar el mensaje." })
            });
        }).catch(error => {
            //logger.error("Error, Se ha producido un error al enviar el mensaje, 500");
            res.status(500);
            res.json({ error: "Se ha producido un error al enviar el mensaje." })
        });
    });
}