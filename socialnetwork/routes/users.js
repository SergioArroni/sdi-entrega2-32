module.exports = function (app, usersRepository) {
    app.get('/users', function (req, res) {
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
    app.post('/users', function (req, res) {
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
                res.redirect("/users");
            } else {
                req.session.user = null;
                res.redirect("/users/login" + "?message=No puedes acceder a esa pagina sin permisos" + "&messageType=alert-danger ");
            }
        }
    )
    app.get('/users/logout', function (req, res) {
        req.session.user = null;
        res.send("El usuario se ha desconectado correctamente");
    })
    app.get('/users/login', function (req, res) {
        res.render("users/login.twig");
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
                        res.redirect("/users");
                    } else {
                        res.redirect("/");
                    }
                }
            }).catch(error => {
                req.session.user = null;
                res.redirect("/users/login" + "?message=Se ha producido un error al buscar el usuario" + "&messageType=alert-danger ");
            })
        }
    )
    app.get('/users/register', function (req, res) {
        res.render("users/register.twig");
    })
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
}