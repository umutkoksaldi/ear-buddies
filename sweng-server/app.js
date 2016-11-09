//----------------------------------------------------------------------------------
// Utils
var express = require('express');
var path = require('path');
var favicon = require('serve-favicon');
var logger = require('morgan');
var cookieParser = require('cookie-parser');
var bodyParser = require('body-parser');
var utils = require('./apiControler/utils/Utils.js');
//----------------------------------------------------------------------------------

//----------------------------------------------------------------------------------
// app Configuration. 
var app = express();

// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'jade');
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

// if we are in test, we display any log.
if(process.env.NODE_ENV !== 'test') {
  app.use(logger('dev'));    
}


//----------------------------------------------------------------------------------

////----------------------------------------------------------------------------------
// database
var databasePostgres = require('./apiControler/database/postgres.js');

databasePostgres.ConnexionPostgres();

//----------------------------------------------------------------------------------
// routing
var routes               = require('./routes/index');
var RouteUtilisateur     = require('./apiControler/routing/users.js');
var RouteMusic           = require('./apiControler/routing/music.js');

app.use('/', routes);
app.use('/api/Users',RouteUtilisateur);
app.use('/api/Musics',RouteMusic);

//----------------------------------------------------------------------------------
utils.logInfo("app , SERVEUR OK : Ecoute sur le port 3000");
utils.logInfo("app ,acces au site sur http://localhost:3000");

module.exports = app;

// catch 404 and forward to error handler
app.use(function(req, res, next) {
  var err = new Error('Not Found');
  err.status = 404;
  next(err);
});

// error handlers

// development error handler
// will print stacktrace
if (app.get('env') === 'development') {
  app.use(function(err, req, res, next) {
    res.status(err.status || 500);
    res.render('error', {
      message: err.message,
      error: err
    });
  });
}

// production error handler
// no stacktraces leaked to user
app.use(function(err, req, res, next) {
  res.status(err.status || 500);
  res.render('error', {
    message: err.message,
    error: {}
  });
});


module.exports = app;
