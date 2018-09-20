// Empty constructor
function ContactPlugin() { }

// The function that passes work along to native shells
// Message is a string, duration may be 'long' or 'short'
ContactPlugin.prototype.show = function (message, duration, successCallback, errorCallback) {
    var options = {};
    options.message = message;
    options.duration = duration;
    // SEND THIS REQUEST TO NATIVE
    console.log('CALLING CORDOVA EXEC...');
    var successCallback = function(contacts) {
        console.log(contacts);
    };
    var errorCallback = function(error) {
        console.log('ERROR: ', error);
    };
    try{
        cordova.exec(successCallback, errorCallback, 'ContactPlugin', 'getDeviceCode', []);
    }catch(e) {
        errorCallback(e);
    }
    
}

// Installation constructor that binds ToastyPlugin to window
// ToastyPlugin.install = function() {
//   if (!window.plugins) {
//     window.plugins = {};
//   }
//   window.plugins.toastyPlugin = new ToastyPlugin();
//   return window.plugins.toastyPlugin;
// };
cordova.addConstructor(ContactPlugin.install);