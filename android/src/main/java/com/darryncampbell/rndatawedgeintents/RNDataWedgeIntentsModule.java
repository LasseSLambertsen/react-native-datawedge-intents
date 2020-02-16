package com.darryncampbell.rndatawedgeintents;

import android.content.Intent;
import android.util.Log;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;


import java.util.Observable;
import java.util.Observer;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableType;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.modules.core.DeviceEventManagerModule;

public class RNDataWedgeIntentsModule extends ReactContextBaseJavaModule implements Observer, LifecycleEventListener {

    private static final String TAG = RNDataWedgeIntentsModule.class.getSimpleName();


    //  Scan data receiver - These strings are only used by registerReceiver, a deprecated method
    private static final String RECEIVED_SCAN_SOURCE = "com.symbol.datawedge.source";
    private static final String RECEIVED_SCAN_DATA = "com.symbol.datawedge.data_string";
    private static final String RECEIVED_SCAN_TYPE = "com.symbol.datawedge.label_type";

    private ReactApplicationContext reactContext;

    public RNDataWedgeIntentsModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        reactContext.addLifecycleEventListener(this);
        Log.v(TAG, "Constructing React native DataWedge intents module");

        //  Register a broadcast receiver to return data back to the application
        ObservableObject.getInstance().addObserver(this);
    }


    @Override
    public String getName() {
        return "DataWedgeIntents";
    }


    @ReactMethod
    public void registerReceiver(String action, String category)
    {
        //  THIS METHOD IS DEPRECATED, use registerBroadcastReceiver
        Log.d(TAG, "Registering an Intent filter for action: " + action);
        this.registeredAction = action;
        this.registeredCategory = category;
        //  User has specified the intent action and category that DataWedge will be reporting
        try
        {
            this.reactContext.unregisterReceiver(scannedDataBroadcastReceiver);
        }
        catch (IllegalArgumentException e)
        {
            //  Expected behaviour if there was not a previously registered receiver.
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(action);
        if (category != null && category.length() > 0)
            filter.addCategory(category);
        this.reactContext.registerReceiver(genericReceiver, filter);
    }


    public BroadcastReceiver genericReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.v(TAG, "Received Broadcast from DataWedge");
            intent.putExtra("v2API", true);
            ObservableObject.getInstance().updateValue(intent);
        }
    };


    //  Sending events to JavaScript as defined in the native-modules documentation.
    //  Note: Callbacks can only be invoked a single time so are not a suitable interface for barcode scans.
    private void sendEvent(ReactContext reactContext,
                           String eventName,
                           WritableMap params) {
        reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventName, params);
    }

    //  Credit: http://stackoverflow.com/questions/28083430/communication-between-broadcastreceiver-and-activity-android#30964385
    @Override
    public void update(Observable observable, Object data) {
        Intent intent = (Intent) data;

        //  Intent from the scanner (barcode has been scanned)
        String decodedSource = intent.getStringExtra(RECEIVED_SCAN_SOURCE);
        String decodedData = intent.getStringExtra(RECEIVED_SCAN_DATA);
        String decodedLabelType = intent.getStringExtra(RECEIVED_SCAN_TYPE);

        WritableMap scanData = new WritableNativeMap();
        scanData.putString("source", decodedSource);
        scanData.putString("data", decodedData);
        scanData.putString("labelType", decodedLabelType);
        sendEvent(this.reactContext, "barcode_scan", scanData);

    }

    @Override
    public void onHostResume() {

    }

    @Override
    public void onHostPause() {

    }

    @Override
    public void onHostDestroy() {

    }
}
