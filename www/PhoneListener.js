var exec = require('cordova/exec');

var PhoneListener = function(src, successCallback, errorCallback, statusCallback) {

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
PhoneListener.prototype.stop = function() {
    exec(
        successCallback,
        failureCallback,
        'PhoneListener',
        'stopMonitoringPhoneState',
      []);
};

module.exports = PhoneListener;