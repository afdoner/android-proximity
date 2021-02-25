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

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AppActivity extends AppCompatActivity {
    TextView rssiTextView;
    Button sendButton;
    final Handler handler = new Handler();
    final int delay = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        rssiTextView = (TextView)findViewById(R.id.rssiTextView);
        sendButton = (Button)findViewById(R.id.send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onLocationReached();
            }
        });
        final LinearLayout li = (LinearLayout) findViewById(R.id.myScreen);
        handler.postDelayed(new Runnable() {
            public void run() {
                String val = GimbalIntegration.instance().getRssiValue();
                rssiTextView.setText(val);
                handler.postDelayed(this, delay);
                if (val.equals("Try a tide pod today!")) {
                    GimbalIntegration.instance().onTerminate();
                    onLocationReached();
                    handler.removeCallbacksAndMessages(null);
                }
                else if (val.equals("Cold"))
                    li.setBackgroundResource(R.drawable.cold);
                else if (val.equals("Warm"))
                    li.setBackgroundResource(R.drawable.warm);
                else if (val.equals("Hot"))
                    li.setBackgroundResource(R.drawable.hot);
            }
        }, delay);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void onLocationReached() {
        startActivity(new Intent(this, VideoActivity.class));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
