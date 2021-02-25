/**
 * Copyright (C) 2017 Gimbal, Inc. All rights reserved.
 *
 * This software is the confidential and proprietary information of Gimbal, Inc.
 *
 * The following sample code illustrates various aspects of the Gimbal SDK.
 *
 * The sample code herein is provided for your convenience, and has not been
 * tested or designed to work on any particular system configuration. It is
 * provided AS IS and your use of this sample code, whether as provided or
 * with any modification, is at your own risk. Neither Gimbal, Inc.
 * nor any affiliate takes any liability nor responsibility with respect
 * to the sample code, and disclaims all warranties, express and
 * implied, including without limitation warranties on merchantability,
 * fitness for a specified purpose, and against infringement.
 */
package com.gimbal.android.sample;

import java.util.Arrays;
import java.util.List;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.gimbal.android.BeaconSighting;
import com.gimbal.android.Gimbal;
import com.gimbal.android.PlaceEventListener;
import com.gimbal.android.PlaceManager;
import com.gimbal.android.Visit;

public class GimbalIntegration extends AppCompatActivity {
    private static final String GIMBAL_APP_API_KEY = "d56b723f-1863-4230-80a3-3f818fde547f";
    private int rssiValue = -150;
    double[] intArray = new double[3];

    private Application app;
    private Context     appContext;
    private TcpClient mTcpClient;
    private PlaceEventListener placeEventListener;

    private static GimbalIntegration instance;

    public static GimbalIntegration init(Application app) {
        if (instance == null) {
            instance = new GimbalIntegration(app);
        }
        return instance;
    }

    public static GimbalIntegration instance() {
        if (instance == null) {
            throw new IllegalStateException("Gimbal integration not initialized from Application");
        }
        return instance;
    }

    private GimbalIntegration(Application app) {
        this.app = app;
        this.appContext = app.getApplicationContext();
    }

    public void onCreate() {
        Gimbal.setApiKey(app, GIMBAL_APP_API_KEY);
        new ConnectTask().execute("");
        placeEventListener = new PlaceEventListener() {
            @Override
            public void onBeaconSighting(BeaconSighting sighting, List<Visit> visits) {
                // This will be invoked when a beacon assigned to a place within a current visit is sighted.
                if (sighting.getBeacon().getName().equals("INNOVATION-LAB1")) {
//                    intArray[0] = sighting.getRSSI();
                    Log.i("INNOVATION-LAB1", sighting.getRSSI().toString());
                    rssiValue = sighting.getRSSI();
                }
//                if (sighting.getBeacon().getName().equals("INNOVATION-LAB2")) {
//                    intArray[1] = sighting.getRSSI();
//                    Log.i("INNOVATION-LAB2", sighting.getRSSI().toString());
//                }
//                if (sighting.getBeacon().getName().equals("INNOVATION-LAB3")) {
//                    intArray[2] = sighting.getRSSI();
//                    Log.i("INNOVATION-LAB3", sighting.getRSSI().toString());
//                }
//                Arrays.sort(intArray);
//                double median;
//                if (intArray.length % 2 == 0)
//                    median = (double)intArray[intArray.length/2] + (double)intArray[intArray.length/2 - 1]/2;
//                else
//                    median = ((double) intArray[intArray.length/2]);
//                rssiValue = toString().valueOf(median);
            }
        };
        PlaceManager.getInstance().addListener(placeEventListener);
    }

    public String getRssiValue() {
        String msg = "";
        if (rssiValue <= -90) {
            msg = "Cold";
            if (mTcpClient != null) {
                Log.v("Event", "Sending message to tcp server!");
                mTcpClient.sendMessage("0");
            }
        }
        else if (rssiValue > -60) {
            msg = "Try a tide pod today!";
            if (mTcpClient != null) {
                Log.v("Event", "Sending message to tcp server!");
                mTcpClient.sendMessage("3");
            }
        }
        else if (rssiValue > -75) {
            msg = "Hot";
            if (mTcpClient != null) {
                Log.v("Event", "Sending message to tcp server!");
                mTcpClient.sendMessage("2");
            }
        }
        else if (rssiValue > -90) {
            msg = "Warm";
            if (mTcpClient != null) {
                Log.v("Event", "Sending message to tcp server!");
                mTcpClient.sendMessage("1");
            }
        }
        return msg;
    }


    public void onTerminate() {
        PlaceManager.getInstance().removeListener(placeEventListener);
    }

    public class ConnectTask extends AsyncTask<String, String, TcpClient> {

        @Override
        protected TcpClient doInBackground(String... message) {

            //we create a TCPClient object
            mTcpClient = new TcpClient(new TcpClient.OnMessageReceived() {
                @Override
                //here the messageReceived method is implemented
                public void messageReceived(String message) {
                    //this method calls the onProgressUpdate
                    publishProgress(message);
                }
            });
            mTcpClient.run();

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            //response received from server
            Log.d("test", "response " + values[0]);
            //process server response here....

        }
    }
}
