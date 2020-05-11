/**
 * @providesModule DataWedgeIntents
 */

'use strict';

let { Platform, NativeModules } = require('react-native');
let RNDataWedgeIntents = NativeModules.DataWedgeIntents;
let DataWedgeIntents = {};

if(RNDataWedgeIntents) {
    DataWedgeIntents = {
        //  Specifying the DataWedge API constants in this module is deprecated.  It is not feasible to stay current with the DW API.
        registerReceiver(action, category ) {
            RNDataWedgeIntents.registerReceiver(action,category);
        },
    };
}

module.exports = DataWedgeIntents;
