/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.firebase.quickstart.fcm;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("TROL", "onCreate");
        Button logTokenButton = (Button) findViewById(R.id.logTokenButton);
        assert logTokenButton != null;
        logTokenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TROL", "onClick logTokenButton");
                logTokenIfInitialized();
            }
        });

        Button startRegistrationButton = (Button) findViewById(R.id.startRegistration);
        assert startRegistrationButton != null;
        startRegistrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TROL", "onClick startRegistrationButton");
                startRegistrationProcess();
            }
        });

        Button logAppsButton = (Button) findViewById(R.id.logAppsButton);
        assert logAppsButton != null;
        logAppsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TROL", "onClick logAppsButton");
                logAppsButton();
            }
        });

        final Button deleteInstanceIdButton = (Button) findViewById(R.id.deleteInstanceIdButton);
        assert deleteInstanceIdButton != null;
        deleteInstanceIdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TROL", "onClick deleteInstanceIdButton");
                clearToken();
            }
        });
    }

    private void logTokenIfInitialized() {
        if (isFirebaseAppExist()) {
            Log.d("TROL", "InstanceID token: " + FirebaseInstanceId.getInstance().getToken());
        } else {
            Log.d("TROL", "FirebaseApp not initialized");
        }
    }

    private void startRegistrationProcess() {
        if (isFirebaseAppExist()) {
            Log.d("TROL", "FirebaseApp already initialized");
            return;
        }
        Log.d("TROL", "initializing");
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                FirebaseOptions firebaseOptions = new FirebaseOptions.Builder()
                        .setGcmSenderId("947335389044")
                        .setApplicationId("com.google.firebase.quickstart.fcm")
                        .build();
                FirebaseApp.initializeApp(MainActivity.this.getApplicationContext(), firebaseOptions);
            }
        });
        Log.d("TROL", "initializing process started");
    }

    private void logAppsButton() {
        Log.d("TROL", "logAppsButton");
        List<FirebaseApp> apps = getApps();
        Log.d("TROL", "apps: " + apps.toString());
    }

    private List<FirebaseApp> getApps() {
        return FirebaseApp.getApps(MainActivity.this.getApplicationContext());
    }

    private boolean isFirebaseAppExist() {
        for (FirebaseApp firebaseApp : getApps()) {
            if (firebaseApp.getName().equals("[DEFAULT]")) {
                return true;
            }
        }
        return false;
    }

    private void clearToken() {
        if (!isFirebaseAppExist()) {
            Log.d("TROL", "FirebaseApp not initialized");
            return;
        }
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    FirebaseInstanceId.getInstance().deleteInstanceId();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
