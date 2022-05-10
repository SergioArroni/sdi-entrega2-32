const {ObjectID} = require("mongodb");
module.exports = function (app, usersRepository, friendsRepository, publicationsRepository) {
    app.get('/users/listAdmin' +
        '', function (req, res) {
        let userA = req.session.user
        if (userA.rol == 'Admin') {
            usersRepository.getUsers({}, {}).then(users => {
                let usuariosNormales = [];

                for (let i = 0; i < users.length; i++) {
                    if (users[i].rol != 'Admin') {
                        usuariosNormales.push(users[i]);
                    }
                }

                res.render("users/users.twig", {users: usuariosNormales});
            }).catch(error => {
                res.send("Se ha producido un error al listar los usuarios:" + error)
            });
        } else {
            req.session.user = null;
            res.redirect("/users/login" + "?message=No puedes acceder a esa pagina sin permisos" + "&messageType=alert-danger ");
        }
    })
    app.post('/users/listAdmin', function (req, res) {
            let userA = req.session.user
            if (userA.rol == 'Admin') {
                usersRepository.getUsers({}, {}).then(users => {
                        for (let i = 0; i < Object.keys(req.body).length; i++) {
                            for (let j = 0; j < users.length; j++) {
                                if (req.body[users[j]._id]) {
                                    let filter = {_id: users[j]._id};
                                    usersRepository.findDeleteUser(filter, {}).then(result => {
                                        if (result == null || result.deletedCount == 0) {
                                            res.send("No se ha podido eliminar el usuario: " + users[j]._id);
                                        }
                                    }).catch(error => {
                                        res.send("Se ha producido un error al borrar algun usuario:" + error)
                                    });
                                }
                            }
                        }
                    }
                ).catch(error => {
                    res.send("Se ha producido un error al listar los usuarios:" + error)
                });

                res.redirect("/");
            } else {
                req.session.user = null;
                res.redirect("/users/login" + "?message=No puedes acceder a esa pagina sin permisos" + "&messageType=alert-danger ");
            }
        }
    )
    app.get('/users/logout', function (req, res) {
        req.session.user = null;
        res.redirect("/users/login");
    })
    app.get('/users/login', function (req, res) {
        res.render("users/login.twig", {session: req.session.user});
    })
    app.post('/users/login', function (req, res) {
            let securePassword = app.get("crypto").createHmac('sha256', app.get('clave'))
                .update(req.body.password).digest('hex');
            let filter = {
                email: req.body.email, password: securePassword
            }
            let options = {};
            usersRepository.findUser(filter, options).then(user => {
                if (user == null) {
                    req.session.user = null;
                    res.redirect("/users/login" + "?message=Email o password incorrecto" + "&messageType=alert-danger ");
                } else {
                    req.session.user = user;
                    if (user.rol === "Admin") {
                        res.redirect("/users/listAdmin");
                    } else {

                        res.redirect("/users/listUsers");
                    }
                }
            }).catch(error => {
                req.session.user = null;
                res.redirect("/users/login" + "?message=Se ha producido un error al buscar el usuario" + "&messageType=alert-danger ");
            })
        }
    );
    app.get('/users/register', function (req, res) {
        res.render("users/register.twig", {session: req.session.user});
    });
    app.get("/users/listUsers", function (req, res) {
        let filter = {};
        let searchText=req.query.search;
        if (searchText != null) {
            filter={$or: [{"name": {$regex: ".*" + searchText + ".*"}},
                    {"surname": {$regex: ".*" + searchText + ".*"}},
                    {"email": {$regex: ".*" + searchText + ".*"}}]};
        }
        let page = parseInt(req.query.page);
        if (typeof req.query.page === "undefined" || req.query.page === null || req.query.page === "0") {
            page = 1;
        }

        usersRepository.getAllUsersPg(filter,page,req.session.user, function(result, usuarios){
            let lastPage=(usuarios.length)/5;
            if((usuarios.length)%5>0){
                lastPage=lastPage+1;
            }
            let pages=[];
            for(let i=page-2;i<=page+2;i++){
                if(i>0 && i<=lastPage){
                    pages.push(i);
                }
            }
            friendsRepository.getAllFriends().then(amigos=>{
                let response={
                    users: result.users,
                    friends:amigos,
                    search:searchText,
                    pages:pages,
                    currentPage:page,
                    session:req.session.user
                }
                res.render("users/listUsers.twig", response);
            })

        }).catch(error => {
            res.send("Se ha producido un error al listar los usuarios:" + error)
        });

    });

    app.post('/users/register', function (req, res) {
        let filter = {
            email: req.body.email
        }
        let options = {};
        usersRepository.findUser(filter, options).then(user => {
            if (user == null) {
                if (req.body.password === req.body.repeatPassword) {
                    let securePassword = app.get("crypto").createHmac('sha256', app.get('clave')).update(req.body.password).digest('hex');
                    let user = {
                        email: req.body.email,
                        name: req.body.name,
                        surname: req.body.surname,
                        password: securePassword
                    }
                    usersRepository.insertUser(user).then(userId => {
                        res.redirect("/users/login");
                    }).catch(error => {
                        res.redirect("/users/register" + "?message=Se ha producido un error al registrar el usuario" + "&messageType=alert-danger ");
                    });
                } else {
                    res.redirect("/users/register" + "?message=Las dos contraseñas no coinciden" + "&messageType=alert-danger ");
                }
            } else {
                res.redirect("/users/register" + "?message=Este email ya esta vinculado con un usuario" + "&messageType=alert-danger ");
            }
        }).catch(error => {
            req.session.user = null;
            res.redirect("/users/login" + "?message=Se ha producido un error al buscar el usuario" + "&messageType=alert-danger ");
        })
    });

    app.get('/users/friends', function (req, res) {
        let userA = req.session.user
        if (userA.rol != 'Admin') {
            let id = new ObjectID(userA._id);
            let filter1 = {id_from: id};
            let filter2 = {id_to: id};
            let options = {};
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
                let page = parseInt(req.query.page); // Es String !!!
                if (typeof req.query.page === "undefined" || req.query.page === null || req.query.page === "0") {
                    page = 1;
                }
                usersRepository.getFriendsPg(ids, page).then(result => {
                    let lastPage = result.total / 5;
                    if (result.total % 5 > 0) { // Sobran decimales
                        lastPage = lastPage + 1;
                    }
                    let pages = []; // paginas mostrar
                    for (let i = page - 2; i <= page + 2; i++) {
                        if (i > 0 && i <= lastPage) {
                            pages.push(i);
                        }
                    }
                    let response = {
                        users: result.users,
                        pages: pages,
                        currentPage: page,
                        session: req.session.user
                    }
                    res.render("users/friends.twig", response);
                }).catch(error => {
                    res.send("Se ha producido un error al cargar la lista de amigos:" + error)
                });
            }).catch(error => {
                res.send("Se ha producido un error al cargar la lista de amigos:" + error)
            });
        } else {
            req.session.user = null;
            res.redirect("/users/login" + "?message=No puedes acceder a esa pagina sin permisos" + "&messageType=alert-danger ");
        }
    })

    app.get('/users/create/publication', function (req, res) {
        let userA = req.session.user
        if (userA.rol != 'Admin') {
            res.render("publications/createPublication.twig", {session: userA});
        } else {
            req.session.user = null;
            res.redirect("/users/login" + "?message=No puedes acceder a esa pagina sin permisos" + "&messageType=alert-danger ");
        }
    })

    app.post('/users/create/publication', function (req, res) {
        let userA = req.session.user
        let id = new ObjectID(userA._id)
        let titulo = req.body.titulo
        let texto = req.body.texto
        const tiempo = Date.now();
        let fecha = new Date(tiempo);
        let insertFecha = fecha.toDateString();
        console.log(insertFecha)
        console.log("" + insertFecha)
        if (userA.rol != 'Admin') {
            let publication = {
                titulo: titulo,
                texto: texto,
                user: id,
                fecha: insertFecha
            }
            publicationsRepository.insertPublicaction(publication).then(publicationId => {
                res.redirect("/publications/listPublicaciones");
            }).catch(error => {
                res.redirect("/users/register" + "?message=Se ha producido un error al registrar el usuario" + "&messageType=alert-danger ");
            });
        } else {
            req.session.user = null;
            res.redirect("/users/login" + "?message=No puedes acceder a esa pagina sin permisos" + "&messageType=alert-danger ");
        }
    })

    app.get('/users/friends/publications/:id', function (req, res) {
        let userA = req.session.user
        let id = new ObjectID(userA._id);
        let idFriend = new ObjectID(req.params.id);
        let filter = {user: idFriend}
        let options = {}
        let page = parseInt(req.query.page);
        if (typeof req.query.page === "undefined" || req.query.page === null || req.query.page === "0") {
            page = 1;
        }
        publicationsRepository.getPublicationsPg(filter, options, page).then(result => {
            let lastPage = result.total / 5;
            console.log(lastPage)
            if (result.total % 5 > 0) { // Sobran decimales
                lastPage = lastPage + 1;
            }
            let pages = []; // paginas mostrar
            for (let i = page - 2; i <= page + 2; i++) {
                if (i > 0 && i <= lastPage) {
                    pages.push(i);
                }
            }
                let response = {
                    publications: result.publications,
                    pages: pages,
                    currentPage: page,
                    userid: req.params.id,
                    session: req.session.user
                }
                res.render("publications/friendPublications.twig", response);
            }).catch(error => {
                res.send("Se ha producido un error al listar las canciones del usuario " + error)
            });
    })
}
