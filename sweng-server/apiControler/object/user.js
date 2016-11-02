var utils = require('../utils/Utils.js');

function UserDto()
{

    this.transformResponseClient = function (UserObject) {

        utils.logInfo("transformResponseClient(), Modify the Json client")
        var locationUser = { 
                     'lattitude' : UserObject.lattitude,
                     'longitude' : UserObject.longitude
                   }


        UserObject.currentMusicId = UserObject.CurrentMusicId

        delete UserObject['lattitude']
        delete UserObject['CurrentMusicId']
        delete UserObject['longitude']

        UserObject.location = locationUser;
        utils.logInfo("transformResponseClient(), return response")
        return UserObject;
    }

    this.computeAge = function (birthdate) {

        utils.logInfo(birthdate);
        if(birthdate == undefined){
            return 0
        }

        var userBirthDate = new Date(birthdate)
        var age = Math.floor( ((new Date).getTime()-userBirthDate.getTime()) / (365.24*24*3600*1000));
        return age;
    }

}


// export object
module.exports = new UserDto();