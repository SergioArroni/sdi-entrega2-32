
/*
                                                                              ,--.
  .--.--.                                             ,--,                  ,--.'|              ___             .---.                         ,-.
 /  /    '.                       ,--,              ,--.'|              ,--,:  : |            ,--.'|_          /. ./|                     ,--/ /|
|  :  /`. /    ,---.            ,--.'|              |  | :           ,`--.'`|  ' :            |  | :,'     .--'.  ' ;   ,---.    __  ,-.,--. :/ |
;  |  |--`    '   ,'\           |  |,               :  : '           |   :  :  | |            :  : ' :    /__./ \ : |  '   ,'\ ,' ,'/ /|:  : ' /
|  :  ;_     /   /   |   ,---.  `--'_      ,--.--.  |  ' |           :   |   \ | :   ,---.  .;__,'  / .--'.  '   \' . /   /   |'  | |' ||  '  /
 \  \    `. .   ; ,. :  /     \ ,' ,'|    /       \ '  | |           |   : '  '; |  /     \ |  |   | /___/ \ |    ' '.   ; ,. :|  |   ,''  |  :
  `----.   \'   | |: : /    / ' '  | |   .--.  .-. ||  | :           '   ' ;.    ; /    /  |:__,'| : ;   \  \;      :'   | |: :'  :  /  |  |   \
  __ \  \  |'   | .; :.    ' /  |  | :    \__\/: . .'  : |__         |   | | \   |.    ' / |  '  : |__\   ;  `      |'   | .; :|  | '   '  : |. \
 /  /`--'  /|   :    |'   ; :__ '  : |__  ," .--.; ||  | '.'|        '   : |  ; .''   ;   /|  |  | '.'|.   \    .\  ;|   :    |;  : |   |  | ' \ \
'--'.     /  \   \  / '   | '.'||  | '.'|/  /  ,.  |;  :    ;        |   | '`--'  '   |  / |  ;  :    ; \   \   ' \ | \   \  / |  , ;   '  : |--'
  `--'---'    `----'  |   :    :;  :    ;  :   .'   \  ,   /         '   : |      |   :    |  |  ,   /   :   '  |--"   `----'   ---'    ;  |,'
                       \   \  / |  ,   /|  ,     .-./---`-'          ;   |.'       \   \  /    ---`-'     \   \ ;                       '--'
                        `----'   ---`-'  `--`---'                    '---'          `----'                 '---"

 */

//=====MODULOS=====
let express = require('express');
let app = express();
let createError = require('http-errors');
let log4js = require('log4js');
let jwt = require('jsonwebtoken');
let expressSession = require('express-session');
let path = require('path');
let cookieParser = require('cookie-parser');
let crypto = require('crypto');
let bodyParser = require('body-parser');
let fileUpload = require('express-fileupload');
let indexRouter = require('./routes');
const logger = require('morgan');
log4js.configure({
    appenders: { cheese: { type: 'file', filename: 'socialNetwork.log' } },
    categories: { default: { appenders: ['cheese'], level: 'all' } }
});

//====VARIABLES====

app.set('jwt', jwt);
app.set('uploadPath', __dirname);
app.set('clave', 'abcdefg');
app.set('crypto', crypto);
app.set('logger', log4js.getLogger('tunder'));

//====INICIALIZACIÓN====
app.use(logger('dev'));

app.use(function(req, res, next) {
    res.header("Access-Control-Allow-Origin", "*");
    res.header("Access-Control-Allow-Credentials", "true");
    res.header("Access-Control-Allow-Methods", "POST, GET, DELETE, UPDATE, PUT");
    res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type,Accept, token");
    // Debemos especificar todas las headers que se aceptan. Content-Type , token
    next();
});

//Session
app.use(expressSession({
    secret: 'abcdefg',
    resave: true,
    saveUninitialized: true
}));

app.use(fileUpload({
    limits: {fileSize: 50 * 1024 * 1024}, createParentPath: true
}));

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: true}));

app.use(express.json());
app.use(express.urlencoded({extended: false}));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

//====MONGO====
const {MongoClient} = require("mongodb");
//Deploy
//const url = 'mongodb+srv://admin:admin@cluster0.a1mrh.mongodb.net/Cluster0?retryWrites=true&w=majority';
//Tests
const url = 'mongodb+srv://hugo:admin@cluster0.uznrd.mongodb.net/Cluster0?retryWrites=true&w=majority';
app.set('connectionStrings', url);

//====RUTAS====
const userTokenRouter = require('./routes/userTokenRouter.js');
app.use("/api/v1.0/message", userTokenRouter);
app.use("/api/v1.0/friendlist", userTokenRouter);

const userSessionRouter = require('./routes/userSessionRouter.js');
app.use("/users/list", userSessionRouter);

const usersRepository = require("./repositories/usersRepository.js");
usersRepository.init(app, MongoClient);

const friendsRepository = require("./repositories/friendsRepository.js");
friendsRepository.init(app, MongoClient);

const publicationsRepository = require("./repositories/publicationsRepository.js");
publicationsRepository.init(app, MongoClient);

const invitacionRepository = require("./repositories/invitacionRepository.js");
invitacionRepository.init(app, MongoClient);

require("./routes/users.js")(app, usersRepository, friendsRepository, publicationsRepository);
require("./routes/invitaciones.js")(app, invitacionRepository, friendsRepository,usersRepository);

require("./routes/publications.js")(app, publicationsRepository);
require("./routes/api/socialNetworkAPI")(app, usersRepository, friendsRepository);

// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'twig');

app.use('/', indexRouter);

//====ERROR====

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

//====MODULE====

module.exports = function (app) {
    app.get("/users/listAdmin", function (req, res) {
        let response = "";
        if (req.query.email != null && typeof (req.query.email) != "undefined")
            response = 'Email: ' + req.query.email + '<br>'
        if (req.query.name != null && typeof (req.query.name) != "undefined")
            response += 'Name: ' + req.query.name;
        if (req.query.surname != null && typeof (req.query.surname) != "undefined")
            response += 'Surname: ' + req.query.surname;
        if (req.query.rol != null && typeof (req.query.rol) != "undefined")
            response += 'Rol: ' + req.query.rol;
        res.send(response);
    });
};

module.exports = app;

console.log("\n" +
    "                                                                                                                                                   \n" +
    "                                                                              ,--.                                                                 \n" +
    "  .--.--.                                             ,--,                  ,--.'|              ___             .---.                         ,-.  \n" +
    " /  /    '.                       ,--,              ,--.'|              ,--,:  : |            ,--.'|_          /. ./|                     ,--/ /|  \n" +
    "|  :  /`. /    ,---.            ,--.'|              |  | :           ,`--.'`|  ' :            |  | :,'     .--'.  ' ;   ,---.    __  ,-.,--. :/ |  \n" +
    ";  |  |--`    '   ,'\\           |  |,               :  : '           |   :  :  | |            :  : ' :    /__./ \\ : |  '   ,'\\ ,' ,'/ /|:  : ' /   \n" +
    "|  :  ;_     /   /   |   ,---.  `--'_      ,--.--.  |  ' |           :   |   \\ | :   ,---.  .;__,'  / .--'.  '   \\' . /   /   |'  | |' ||  '  /    \n" +
    " \\  \\    `. .   ; ,. :  /     \\ ,' ,'|    /       \\ '  | |           |   : '  '; |  /     \\ |  |   | /___/ \\ |    ' '.   ; ,. :|  |   ,''  |  :    \n" +
    "  `----.   \\'   | |: : /    / ' '  | |   .--.  .-. ||  | :           '   ' ;.    ; /    /  |:__,'| : ;   \\  \\;      :'   | |: :'  :  /  |  |   \\   \n" +
    "  __ \\  \\  |'   | .; :.    ' /  |  | :    \\__\\/: . .'  : |__         |   | | \\   |.    ' / |  '  : |__\\   ;  `      |'   | .; :|  | '   '  : |. \\  \n" +
    " /  /`--'  /|   :    |'   ; :__ '  : |__  ,\" .--.; ||  | '.'|        '   : |  ; .''   ;   /|  |  | '.'|.   \\    .\\  ;|   :    |;  : |   |  | ' \\ \\ \n" +
    "'--'.     /  \\   \\  / '   | '.'||  | '.'|/  /  ,.  |;  :    ;        |   | '`--'  '   |  / |  ;  :    ; \\   \\   ' \\ | \\   \\  / |  , ;   '  : |--'  \n" +
    "  `--'---'    `----'  |   :    :;  :    ;  :   .'   \\  ,   /         '   : |      |   :    |  |  ,   /   :   '  |--\"   `----'   ---'    ;  |,'     \n" +
    "                       \\   \\  / |  ,   /|  ,     .-./---`-'          ;   |.'       \\   \\  /    ---`-'     \\   \\ ;                       '--'       \n" +
    "                        `----'   ---`-'  `--`---'                    '---'          `----'                 '---\"                                   \n" +
    "                                                                                                                                                   \n")