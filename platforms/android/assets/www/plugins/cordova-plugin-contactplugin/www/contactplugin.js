cordova.define("cordova-plugin-contactplugin.contactPlugin", function(require, exports, module) {
// Empty constructor
function ContactPlugin() { }

// The function that passes work along to native shells
// Message is a string, duration may be 'long' or 'short'
ContactPlugin.prototype.show = function (message, duration, successCallback, errorCallback) {
    var options = {};
    options.message = message;
    options.duration = duration;
    // SEND THIS REQUEST TO NATIVE
    cordova.exec(successCallback, errorCallback, 'ContactPlugin', 'show', [options]);
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
});
