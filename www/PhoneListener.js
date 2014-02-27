var argscheck = require('cordova/argscheck'),
    utils = require('cordova/utils'),
    exec = require('cordova/exec');

var PhoneListener = function() {

};

/**
 * Start Listener
 */
PhoneListener.prototype.start = function(successCallback, failureCallback) {
    exec(
        successCallback,
        failureCallback,
        'PhoneListener',
        'startMonitoringPhoneState',
      []);
};

/**
 * Stop Listener.
 */
PhoneListener.prototype.stop = function(successCallback, failureCallback) {
    exec(
        successCallback,
        failureCallback,
        'PhoneListener',
        'stopMonitoringPhoneState',
      []);
};

module.exports = PhoneListener;