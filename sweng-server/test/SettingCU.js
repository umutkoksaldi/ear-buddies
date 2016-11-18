//During the test the env variable is set to test
process.env.NODE_ENV = 'test';

//Require the dev-dependencies
var chai = require('chai');
var chaiHttp = require('chai-http');
var server = require('../app');
var utils = require('../apiControler/utils/Utils.js');
var Constant = require('../apiControler/utils/Constant.js');
var should = chai.should();
var chaiAsPromised = require ("chai-as-promised")
chai.use(chaiAsPromised);
var setting = require('../setting/error.js');
var expect = chai.expect;

chai.use(chaiHttp);
var User = require('../apiControler/database/SequelizeORM.js').User;


//**************************************************************************** 
// Declaration constante

var AGE_MIN_DEFAULT = 0;
var AGE_MAX_DEFAULT = 100;
var RADIUS_DEFAULT = 20;

var userTest = {

          "email": "test@test.test",
          "id" : "121620614972695",
          "age": 12,
          "firstname": "Antoine",
          "lastname": "Dupond",
          "name": "Antoine Dupond",
          "profilePicture": "https://graph.facebook.com/121620614972695/picture?height=500&width=500",
          "backgroundPicture": "https://graph.facebook.com/121620614972695/picture?height=500&width=500"
  }

  var SETTING_UPDATED = {
              'ageMin'        : 2,
              'ageMax'        : 10,
              'radius'        : 5,
  }

//****************************************************************************

//Our parent block
describe('Test Setting API create/update.', () => {

    // Before each test we insert a user. 
    beforeEach((done) => { 

        // the promise allow to test some asynchronous method.
        var testPromise = new Promise(function(resolve, reject) {
          setTimeout(function() {

                  // create a User --> by default value of setting is the setting indice one.
                  User.sync({force: true}).then(function () {

                      var createUser =  User.create({
                              idApiConnection :userTest.id, 
                              lastname : userTest.lastname,
                              firstname: userTest.firstname,
                              email : userTest.email,
                              age : userTest.age,
                              profilePicture : userTest.profilePicture,
                              backgroundPicture : userTest.backgroundPicture

                      // callback if the user srequest succeed.
                      }).then(function(createUser) {
                          createUser.setSetting(Constant.GeneralModel.idDefaultSetting);
                          resolve(true);

                      // return a 500 code if the request is null.
                      }).catch(function(error) {
                           resolve(false);
                            
                      })
                })
              }, 200);
        });

          testPromise.then(function(result){
              try {
                    console.log ("Before :" +result);
                    expect(result).to.equal(true);
                    done();
              } catch(err) {
                  done(err);
              }
          }, done); 
    });


    describe('check initial value', () => {
        it('check initial value', (done) => {

                      // check the initial value.
                      var testPromise = new Promise(function(resolve, reject) {
                      setTimeout(function() {
                        
                            User.sync().then(function () {
                              // select query.
                               var getUser =  User.findOne({
                                    where: {
                                      idApiConnection: userTest.id
                                    }
                                  }).then(function(getUser) {
                                      getUser.getSetting().then(function(associatedTasks) {
                                        resolve(associatedTasks);
                                      });
                                  })
                              });
                    }, 200);
                });

                testPromise.then(function(result){
                    try {
                          expect(result.ageMin).to.equal(AGE_MIN_DEFAULT);
                          expect(result.ageMax).to.equal(AGE_MAX_DEFAULT);
                          expect(result.radius).to.equal(RADIUS_DEFAULT);
                          done();
                    } catch(err) {
                        done(err);
                    }
                  }, done);

        });
    });


    describe('should update setting', () => {
        it('should update setting', (done) => {
          chai.request(server)
              .put('/api/Users/Setting/'+userTest.id)
              .send(SETTING_UPDATED)
              .end((err, res) => {

                  // Check the User 
                  res.should.have.status(setting.htmlCode.succes_request);
                   // the promise allow to test some asynchronous method.
                      var testPromise = new Promise(function(resolve, reject) {
                      setTimeout(function() {

                            User.sync().then(function () {
                              // select query.
                               var getUser =  User.findOne({
                                    where: {
                                      idApiConnection: userTest.id
                                    }
                                  }).then(function(getUser) {
                                      getUser.getSetting().then(function(associatedTasks) {
                                        resolve(associatedTasks);
                                      });
                                  })
                              });
                    }, 200);
                });

                testPromise.then(function(result){
                    try {
                          expect(result.ageMin).to.equal(SETTING_UPDATED.ageMin);
                          expect(result.ageMax).to.equal(SETTING_UPDATED.ageMax);
                          expect(result.radius).to.equal(SETTING_UPDATED.radius);
                          done();
                    } catch(err) {
                        done(err);
                    }
                  }, done);
            });
        });
    });


});
