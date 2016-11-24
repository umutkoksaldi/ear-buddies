//During the test the env variable is set to test
process.env.NODE_ENV = 'test';

//Require the dev-dependencies
var chai = require('chai');
var chaiHttp = require('chai-http');
var server = require('../app');
var utils = require('../apiControler/utils/Utils.js');
var should = chai.should();
var chaiAsPromised = require ("chai-as-promised")
chai.use(chaiAsPromised);
var setting = require('../setting/error.js');

// import model form the ORM
var User = require('../apiControler/database/SequelizeORM.js').User;
var expect = chai.expect;
chai.use(chaiHttp);

 // ----------------------------------------------   DEFINE USEFUL CONSTANT  -----------------------------------------
var userTest = {

          "email": "etienne.husler@epfl.ch",
          "id" : "121620614972695",
          "firstname": "Sweng",
          "lastname": "Tests",
          "profilePicture": "https://graph.facebook.com/121620614972695/picture?height=500&width=500",
          "backgroundPicture": "https://graph.facebook.com/121620614972695/picture?height=500&width=500"
  }

var FAKE_ID = 1; 
 // ----------------------------------------------   TEST  -----------------------------------------

//Our parent block
describe('Test User API create User', function(){

    this.timeout(4000);
    // Before each test we insert a user. 
    before((done) => { 
          // We rfroce to recreate the table User.
          User.sync({force: true}).then(function () {done();});
    });

 // ----------------------------------------------   Create User -----------------------------------------
         describe('create user', () => {
                it('should create a user', (done) => {

                // the promise allow to test some asynchronous method.
                var testPromise = new Promise(function(resolve, reject) {

                setTimeout(function() {
                      chai.request(server)
                      .post('/api/Users')
                      .send({"id":userTest.id,
                              "accesToken":"EAAOZCzloFDqEBAHGnY8Q6I4d6fJRy9c6FWYZAqNxp2ChFBvpv8ZAycQC7a0oT21ZBp0KuIbZCIUkLWSH4Ev7pI"+
                              "QrjlzAxvrfznhXZAeb8A3ZCZBDks8WekNs4WgtfteZCMhUPQx5ZBPmbBMfwBgjqqAeaHOjtYFe38VYfXV35ZCnQ0yZBzPSDzCKDBBMkGhWA8ZAyrJAcBZA6LCi5XtgZDZD"
                        })
                        .end((err, res) => {

                                res.should.have.status(setting.htmlCode.succes_request);

                                 // select query.
                                 var getUser =  User.findOne({
                                      where: {
                                        idApiConnection: userTest.id
                                      }
                                    }).then(function(getUser) {
                                          resolve(getUser.dataValues);
                                    });
                            });
                    
                        }, 200);
                  });

                testPromise.then(function(result){
                try {
                      expect(result.idApiConnection).to.equal(userTest.id);
                      expect(result.firstname).to.equal(userTest.firstname);
                      expect(result.lastname).to.equal(userTest.lastname);
                      expect(result.backgroundPicture).to.equal(userTest.backgroundPicture);
                      expect(result.profilePicture).to.equal(userTest.profilePicture);
                      done();
                } catch(err) {
                    done(err);
                }
            }, done);    
          });
      });


 describe('create user', () => {
        it('should not create a user because bad information', (done) => {
          chai.request(server)
            .post('/api/Users')
            .send({"id":FAKE_ID,
                    "accesToken":"EAAOZCzloFDqEBAHGnY8Q6I4d6fJRy9c6FWYZAqNxp2ChFBvpv8ZAycQC7a0oT21ZBp0KuIbZCIUkLWSH4Ev7pI"+
                    "QrjlzAxvrfznhXZAeb8A3ZCZBDks8WekNs4WgtfteZCMhUPQx5ZBPmbBMfwBgjqqAeaHOjtYFe38VYfXV35ZCnQ0yZBzPSDzCKDBBMkGhWA8ZAyrJAcBZA6LCi5XtgZDZD"
              })
              .end((err, res) => {
                    res.should.have.status(setting.htmlCode.unavailable_ressources);
                    done();
              });
        });
    });
  
});