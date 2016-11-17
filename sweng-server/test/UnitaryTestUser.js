//During the test the env variable is set to test
process.env.NODE_ENV = 'test';

//Require the dev-dependencies
var chai = require('chai');
var chaiHttp = require('chai-http');
var server = require('../app');
var utils = require('../apiControler/utils/Utils.js');
var should = chai.should();
var setting = require('../setting/error.js');
var userManipulation = require ('../apiControler/object/user.js')

chai.use(chaiHttp);
var User = require('../apiControler/database/SequelizeORM.js').User;


// ----------------------------------------------   DEFINE USEFUL CONSTANT  -----------------------------------------
var DATE = '1994-02-17'
var AGE = 22
var AGE_NOT_DEFINED = 0


var LATTITUDE = 1;
var LONGITUDE = 1;

var objectTest  = {
    "location": {
      'type' : 'point',
      'coordinates' : [1,1]
    }
}

// ----------------------------------------------   TEST  -----------------------------------------

//Our parent block
describe('Test user unitary method', () => {

  // ----------------------------------------------   USER -----------------------------------------

  // ----------------------------------------------   COMPUTE AGE  -----------------------------------------
    describe('compute age', () => {
        it('should compute the age', (done) => {
                    var ageTest = userManipulation.computeAge(DATE)
                    chai.assert.equal(ageTest,AGE,'age does not be equals');
                    done();

              });
        });

      describe('compute age', () => {
        it('should return 0 because it is undefined ', (done) => {
                    var ageTest = userManipulation.computeAge(undefined)
                    chai.assert.equal(ageTest, AGE_NOT_DEFINED, 'age does not be equals');
                    done();
              });
        });

  // ----------------------------------------------   Transform client response  -----------------------------------------
      describe('change object return ', () => {
        it('modify the object return', (done) => {
                    var objectResponse = userManipulation.transformResponseClient(objectTest)
                    chai.assert.equal(objectResponse.location.lattitude , LONGITUDE, 'lattitude should be equals');
                    chai.assert.equal(objectResponse.location.longitude , LONGITUDE, 'longitude should be equals');
                    done();
              });
        });


});