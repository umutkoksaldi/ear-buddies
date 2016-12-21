var express = require('express');
var router = express.Router();

/* GET home page. */
router.get('/', function(req, res, next) {
  // on affiche la page d'acceuil.
  res.sendFile('acceuil.html', {root: __dirname + './../public'});
});

module.exports = router;
