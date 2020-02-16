/**
 * @providesModule DataWedgeIntents
 */

'use strict';

var { Platform, NativeModules } = require('react-native');
var RNDataWedgeIntents = NativeModules.DataWedgeIntents;

var DataWedgeIntents = {
    //  Specifying the DataWedge API constants in this module is deprecated.  It is not feasible to stay current with the DW API.
    registerReceiver(action, category ) {
        RNDataWedgeIntents.registerReceiver(action,category);
    },
};

module.exports = DataWedgeIntents;
