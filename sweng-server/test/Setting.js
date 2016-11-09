//During the test the env variable is set to test
process.env.NODE_ENV = 'test';

//Require the dev-dependencies
var chai = require('chai');
var chaiHttp = require('chai-http');
var server = require('../app');
var utils = require('../apiControler/utils/Utils.js');
var constant = require('../apiControler/utils/Constant.js');
var should = chai.should();
var chaiAsPromised = require ("chai-as-promised")
chai.use(chaiAsPromised);
var setting = require('../setting/error.js');
var expect = chai.expect;

chai.use(chaiHttp);
var User = require('../apiControler/database/SequelizeORM.js').User;


 // ----------------------------------------------   DEFINE USEFUL CONSTANT  ----------------------------------------

 // ----------------------------------------------   TEST  -----------------------------------------
