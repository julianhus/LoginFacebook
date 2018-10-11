package com.traffico.manhattan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class MainActivity extends AppCompatActivity {

    private CallbackManager callbackManager;
    private LoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        //
        callbackManager = CallbackManager.Factory.create();
        //
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Log.i("onSuccess", "UserId: " + loginResult.getAccessToken().getUserId());
                Log.i("onSuccess", "Token: " + loginResult.getAccessToken().getToken());
                Log.i("onSuccess", "Recently Granted Permissions " + loginResult.getRecentlyGrantedPermissions());
            }

            @Override
            public void onCancel() {
                // App code
                Log.i("onCancel", "Cancel");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.i("onError", "Error" + exception.getMessage());
            }
        });
        //

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
