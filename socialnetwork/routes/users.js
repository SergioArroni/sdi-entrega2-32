module.exports = function (app, usersRepository) {
    app.get('/users', function (req, res) {
        res.send('lista de usuarios');
    })
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
                    req.session.user = user.email;
                    if(user.rol === "Admin"){
                        res.redirect("/");
                    }else{
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
                        email: req.body.email, name: req.body.name, surname: req.body.surname, password: securePassword
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