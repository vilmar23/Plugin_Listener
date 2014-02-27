/**
* PhoneListener.java
* PhoneListener PhoneGap plugin (Android)
*
* Created by Tommy-Carlos Williams on 09/08/2011.
* Copyright 2011 Tommy-Carlos Williams. All rights reserved.
* MIT Licensed
*
*
* Update by Matt McGrath to work with Cordova version of PhoneGap 1.6 upwards - 01/06/2012
*
*/
package com.example.plugin;  

import org.json.JSONArray;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.apache.cordova.networkinformation.NetworkManager;

/**
* @author Tommy-Carlos Williams,
* Huge chunks lifted/adapted from the NextworkManager core PhoneGap plugin
*/
public class PhoneListener extends CordovaPlugin {

    public static final String TYPE_NONE = "NONE";
    private static final String LOG_TAG = "PhoneListener";

    private CallbackContext phoneListenerCallbackId;

    BroadcastReceiver receiver;
    private boolean registered = false;

    /**
    * Constructor.
    */
    public PhoneListener()	{
        this.receiver = null;
    }

    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);

        this.phoneListenerCallbackId = null;
        IntentFilter intentFilter = new IntentFilter() ;
        intentFilter.addAction(TelephonyManager.ACTION_PHONE_STATE_CHANGED);
        if (this.receiver == null) {
            this.receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if(intent != null && intent.getAction().equals(TelephonyManager.ACTION_PHONE_STATE_CHANGED)) {
                    // State has changed
                    String phoneState = intent.hasExtra(TelephonyManager.EXTRA_STATE) ? intent.getStringExtra(TelephonyManager.EXTRA_STATE) : null;
                    String state;
                    // See if the new state is 'ringing', 'off hook' or 'idle'
                    if(phoneState != null && phoneState.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                    // phone is ringing, awaiting either answering or canceling
                    state = "RINGING";
                    Log.i(LOG_TAG,state);
                    } else if (phoneState != null && phoneState.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                    // actually talking on the phone... either making a call or having answered one
                    state = "OFFHOOK";
                    Log.i(LOG_TAG,state);
                    } else if (phoneState != null && phoneState.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                    // idle means back to no calls in or out. default state.
                    state = "IDLE";
                    Log.i(LOG_TAG,state);
                    } else {
                    state = TYPE_NONE;
                    Log.i(LOG_TAG,state);
                    }
                    updatePhoneState(state,true);
                    }
                }
            };
            // register the receiver... this is so it doesn't have to be added to AndroidManifest.xml
            cordova.getActivity().registerReceiver(this.receiver, intentFilter);
            this.registered = true;
        }

    }


    /**
    * Create a new plugin result and send it back to JavaScript
    *
    * @param phone state sent back to the designated success callback
    */
    private void updatePhoneState(String phoneState, boolean keepCallback) {
      if (this.phoneListenerCallbackId != null) {
        PluginResult result = new PluginResult(PluginResult.Status.OK, phoneState);
        result.setKeepCallback(keepCallback);
        this.phoneListenerCallbackId.sendPluginResult(result);
      }
      webView.postMessage("networkconnection", phoneState);
    }


    /* (non-Javadoc)
    * @see com.phonegap.api.Plugin#execute(java.lang.String, org.json.JSONArray, java.lang.String)
    */
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) {

        if (action.equals("startMonitoringPhoneState")) {
          this.phoneListenerCallbackId = callbackContext;
          PluginResult pluginResult = new PluginResult(PluginResult.Status.NO_RESULT);
          pluginResult.setKeepCallback(true);
          callbackContext.sendPluginResult(pluginResult);
          return true;
        }
        else if (action.equals("stopMonitoringPhoneState")) {
            removePhoneListener();
            this.updatePhoneState("", false); // release status callback
            this.phoneListenerCallbackId = null;
            PluginResult pluginResult = new PluginResult(PluginResult.Status.NO_RESULT);
            callbackContext.sendPluginResult(pluginResult);
        }

        return false; // no valid action called
    }


    /**
    * Stop the phone listener receiver and set it to null.
    */
    private void removePhoneListener() {
        if (this.receiver != null && this.registered) {
            try {
            	this.cordova.getActivity().unregisterReceiver(this.receiver);
                this.registered = false;
                this.receiver = null;
            } catch (Exception e) {
                Log.e(LOG_TAG, "Error unregistering phone listener receiver: " + e.getMessage(), e);
            }
        }
    }


    /**
    * Stop phone listener receiver.
    */
    public void onDestroy() {
        removePhoneListener();
    }

}