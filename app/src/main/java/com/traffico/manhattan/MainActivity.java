package com.traffico.manhattan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

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
                setFacebookData(loginResult);
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
            //
            //
            private void setFacebookData(final LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                // Application code
                                try {
                                    Log.i("Response", response.toString());

                                    String email = response.getJSONObject().getString("email");
                                    String firstName = response.getJSONObject().getString("first_name");
                                    String lastName = response.getJSONObject().getString("last_name");
                                    //String gender = response.getJSONObject().getString("gender");


                                    Profile profile = Profile.getCurrentProfile();
                                    String id = profile.getId();
                                    String link = profile.getLinkUri().toString();
                                    Log.i("Link", link);
                                    if (Profile.getCurrentProfile() != null) {
                                        Log.i("Login", "ProfilePic" + Profile.getCurrentProfile().getProfilePictureUri(200, 200));
                                    }

                                    Log.i("Login" + "Email", email);
                                    Log.i("Login" + "FirstName", firstName);
                                    Log.i("Login" + "LastName", lastName);
                                    //Log.i("Login" + "Gender", gender);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,email,first_name,last_name");
                //parameters.putString("fields", "id,email,first_name,last_name,gender");
                request.setParameters(parameters);
                request.executeAsync();
                //
            }
            //
        });
        //
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

}
