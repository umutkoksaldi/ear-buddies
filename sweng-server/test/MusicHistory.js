//During the test the env variable is set to test
process.env.NODE_ENV = 'test';

var chai = require('chai');
var chaiHttp = require('chai-http');
var server = require('../app');
var utils = require('../apiControler/utils/Utils.js');
var should = chai.should();
var chaiAsPromised = require ("chai-as-promised")
chai.use(chaiAsPromised);
var setting = require('../setting/error.js');
var expect = chai.expect;

// import model form the ORM
var Music = require('../apiControler/database/SequelizeORM.js').Music;
var User = require('../apiControler/database/SequelizeORM.js').User;
chai.use(chaiHttp);

 // ----------------------------------------------   DEFINE USEFUL CONSTANT  -----------------------------------------

// define constante 
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

var musicTest = {
      'artist' : 'Rihanna',
      'name'   : 'Umbrella',
      'url'    : 'https://www.last.fm/music/Rihanna/_/Umbrella',
      'tag'    : 'rock'
}


LIMIT_LOOP_MUSIC = 12;
LIMIT_HISTORY_MUSIC = 10;


// ----------------------------------------------   TEST  -----------------------------------------

//We create the user.
describe('Test Music API', function() {

    this.timeout(150000);
      // Before each test we insert a user and we add 12 music to the user 
    beforeEach((done) => {

        // the promise allow to test some asynchronous method.
        var testPromise = new Promise(function(resolve, reject) {
          setTimeout(function() {
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

                          Music.sync({force: true}).then(function () {
                            "use strict";
                            // We should use let becase the loop with asynchronous call does not work:
                            // http://stackoverflow.com/questions/11488014/asynchronous-process-inside-a-javascript-for-loop
                            for (let i =0; i <= LIMIT_LOOP_MUSIC; i++){

                              // We need the timeout in oder to keep the order.
                              setTimeout(function(){ 

                                  // We change the date to simulate the user's behaviour 
                                  var date = 2013 + i
                                  var CreateMusic =  Music.create({
                                          artist        : musicTest.artist,
                                          // by implementing the i we define a way to know the order of wich
                                          // each music have been added.
                                          name          : musicTest.name+i,
                                          url           : musicTest.url,
                                          tag           : musicTest.tag,
                                          createdAt     : date+"-09-18T21:23:09.713Z"

                                  // callback if the user srequest succeed.
                                  }).then(function(CreateMusic) {

                                      // We link the user with  the music.
                                      createUser.setCurrentMusic(CreateMusic.id);
                                      createUser.addMusic(CreateMusic.id);

                                      // if this is the last loop we have inserted every music
                                      if (LIMIT_LOOP_MUSIC == i ) {
                                        console.log('added 12 music in order.')
                                        resolve(true); 
                                      }   

                                    }).catch(function(error) {
                                        console.log(error)
                                        resolve(false);
                                    });
                              }, 100);
                            }
                          });

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

 // ----------------------------------------------   Create User good parameters -----------------------------------------
         describe('get the music history', () => {
                it('should get the music history from the user', (done) => {
                  url = '/api/Musics/history/'+ userTest.id;
                  chai.request(server)
                      .get(url)
                      .end((err, res) => {

                        res.text  = JSON.parse(res.text);

                        //check the response.
                        res.should.have.status(setting.htmlCode.succes_request);

                        //check the lennth
                        chai.assert.equal(res.text.length, LIMIT_HISTORY_MUSIC, 'should return the last 10 musics');

                        //check the order.
                        for (var i = LIMIT_LOOP_MUSIC ; i > (LIMIT_LOOP_MUSIC - LIMIT_HISTORY_MUSIC); i--){
                          chai.assert.equal(res.text[LIMIT_LOOP_MUSIC-i].name.includes(i), true , 'bad order in the historic');
                        }

                        done();
                      
                      });
                });
        });
});





