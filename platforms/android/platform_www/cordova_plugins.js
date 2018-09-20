cordova.define('cordova/plugin_list', function(require, exports, module) {
module.exports = [
    {
        "id": "cordova-plugin-device.device",
        "file": "plugins/cordova-plugin-device/www/device.js",
        "pluginId": "cordova-plugin-device",
        "clobbers": [
            "device"
        ]
    },
    {
        "id": "cordova-plugin-contactplugin.contactPlugin",
        "file": "plugins/cordova-plugin-contactplugin/www/contactplugin.js",
        "pluginId": "cordova-plugin-contactplugin",
        "clobbers": [
            "window.plugins.contactPlugin"
        ]
    }
];
module.exports.metadata = 
// TOP OF METADATA
{
    "cordova-plugin-whitelist": "1.3.3",
    "cordova-plugin-device": "2.0.2",
    "cordova-plugin-contactplugin": "0.0.1"
};
// BOTTOM OF METADATA
});