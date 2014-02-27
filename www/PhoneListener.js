alert("phonelistener");
var PhoneListener = {
    start: function(successCallback, failureCallback) {
      return cordova.exec(
        successCallback,
        failureCallback,
        'PhoneListener',
        'startMonitoringPhoneState',
      []); // no arguments required
    },
    stop: function(successCallback, failureCallback) {
      return cordova.exec(
        successCallback,
        failureCallback,
        'PhoneListener',
        'stopMonitoringPhoneState',
      []); // no arguments required
    }
};