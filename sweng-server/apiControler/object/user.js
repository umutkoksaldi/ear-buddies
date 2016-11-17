var utils = require('../utils/Utils.js');




function UserDto(){

    this.transformResponseClient = function (UserObject) {

        utils.logInfo("transformResponseClient(), Modify the Json client")
        UserObject.currentMusicId = UserObject.CurrentMusicId

        // Modify the location object.
        if(UserObject.location != null){
            
            UserObject.location.lattitude = UserObject.location.coordinates[0]
            UserObject.location.longitude = UserObject.location.coordinates[1]
            delete UserObject.location['type']
            delete UserObject.location['coordinates']
        }
        
        delete UserObject['CurrentMusicId']
        utils.logInfo("transformResponseClient(), return response")
        return UserObject;
    }

    this.changeSetting = function(UserObject,SettingObject) {
        utils.logInfo("changeSetting(), Modify the Json client")
        delete UserObject['SettingId']
        UserObject.setting = SettingObject
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