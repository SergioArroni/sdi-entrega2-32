var createError = require('http-errors');
var express = require('express');
var path = require('path');
var cookieParser = require('cookie-parser');
var logger = require('morgan');

var app = express();

let expressSession = require('express-session');
app.use(expressSession({
    secret: 'abcdefg', resave: true, saveUninitialized: true
}));

let crypto = require('crypto');

let fileUpload = require('express-fileupload');
app.use(fileUpload({
    limits: {fileSize: 50 * 1024 * 1024}, createParentPath: true
}));
app.set('uploadPath', __dirname);
app.set('clave', 'abcdefg');
app.set('crypto', crypto);

let bodyParser = require('body-parser');
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: true}));

var indexRouter = require('./routes');

const {MongoClient, ObjectId} = require("mongodb");
const url = 'mongodb+srv://admin:admin@cluster0.a1mrh.mongodb.net/myFirstDatabase?retryWrites=true&w=majority';
app.set('connectionStrings', url);

const userSessionRouter = require('./routes/userSessionRouter');
app.use("/users/list", userSessionRouter);

const usersRepository = require("./repositories/usersRepository.js");
usersRepository.init(app, MongoClient);


require("./routes/users.js")(app, usersRepository);

// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'twig');

app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({extended: false}));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

app.use('/', indexRouter);

// catch 404 and forward to error handler
app.use(function (req, res, next) {
    next(createError(404));
});

// error handler
app.use(function (err, req, res, next) {
    // set locals, only providing error in development
    console.log("Se ha producido un error: " + err);
    res.locals.message = err.message;
    res.locals.error = req.app.get('env') === 'development' ? err : {};

    // render the error page
    res.status(err.status || 500);
    res.render('error');
});

module.exports = function (app) {
    app.get("/users", function (req, res) {

        let response = "";
        if (req.query.email != null && typeof (req.query.email) != "undefined")
            response = 'Email: ' + req.query.email + '<br>'
        if (req.query.name != null && typeof (req.query.name) != "undefined")
            response += 'Name: ' + req.query.name;
        if (req.query.surname != null && typeof (req.query.surname) != "undefined")
            response += 'Surname: ' + req.query.name;
        res.send(response);
    });
};

module.exports = app;