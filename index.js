/**
 * @providesModule DataWedgeIntents
 */

'use strict';

var { Platform, NativeModules } = require('react-native');
var RNDataWedgeIntents = NativeModules.DataWedgeIntents;

var DataWedgeIntents = {
    //  Specifying the DataWedge API constants in this module is deprecated.  It is not feasible to stay current with the DW API.
    registerBroadcastReceiver(filter) {
        RNDataWedgeIntents.registerBroadcastReceiver(filter);
    },
};

module.exports = DataWedgeIntents;
