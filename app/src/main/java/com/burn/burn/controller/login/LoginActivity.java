package com.burn.burn.controller.login;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.burn.burn.R;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import java.io.Console;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import android.content.pm.*;
import java.util.Arrays;


/**
 *
 *  Created by Mihail Butnaru
 *  Login Activity [Facebook | Twitter]
 *
 *  This activity will allow you to login using the Facebook and Twitter account.
 *
 */

public class LoginActivity extends AppCompatActivity {

    private CallbackManager callManager;              // CallbankManager handles login response from the Factory.create()
    private AccessTokenTracker accessTokenTracker;      // TokenTracker of the user [UNIQUE]
    private ProfileTracker profileTracker;              // ProfileTracker [FName,SName]
    private LoginButton loginfbButton;                  // LoginButton - Facebook Library
    private Button newButton;
    private Button new_button_twitter;
    private static final String TAG = "Login Activity";         // Simple tag for testing

    //Twitter
    TwitterLoginButton loginTwitter;
    private static final String TWITTER_KEY = "BeVSZlzqbOEWo56O7OBYRCca9";
    private static final String TWITTER_SECRET = "tPL3PWy6rFMazllusLBTYZ3PwzRglLxjuhR8dqwQxKBxSxoRo4";

    // onCreate is the main method that is calling everything | Do not Modify
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        Twitter.initialize(this);
        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET))
                .debug(true)
                .build();
        Twitter.initialize(config);


        setContentView(R.layout.activity_login);
        callManager = CallbackManager.Factory.create();

        // Generates a keyhash when the user logins.
        try{
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.burn.burn",
                    PackageManager.GET_SIGNATURES
            );
            for(Signature signature : info.signatures){
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash: " , Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        }catch(PackageManager.NameNotFoundException e){

        }catch(NoSuchAlgorithmException e){

        }

        // Access Token Tracker
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {

            }
        };
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
            }
        };

        accessTokenTracker.startTracking();
        profileTracker.startTracking();
        loginfbButton = (LoginButton) findViewById(R.id.loginfbButton);
        newButton = (Button) findViewById(R.id.fb);
        newButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                loginfbButton.performClick();
            }
        });

        loginfbButton.registerCallback(callManager, new FacebookCallback<LoginResult>(){
            @Override
            public void onSuccess(LoginResult loginResult){
                if(Profile.getCurrentProfile() == null){
                    profileTracker  = new ProfileTracker() {
                        @Override
                        protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                            passInformation(currentProfile);
                            profileTracker.stopTracking();
                        }
                    };
                }else{
                    Profile profile = Profile.getCurrentProfile();
                    passInformation(profile);
                }
            }
            @Override
            public void onCancel(){

            }
            @Override
            public void onError(FacebookException error){

            }
        });
        loginfbButton.setReadPermissions(Arrays.asList("public_profile"));



        new_button_twitter = (Button) findViewById(R.id.tw);
        new_button_twitter.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                loginTwitter.performClick();
            }
        });
        loginTwitter = (TwitterLoginButton) findViewById(R.id.loginTwitter);
        loginTwitter.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // Do something with result, which provides a TwitterSession for making API calls
                TwitterSession session = result.data;
                passInformationTwitter(session);
            }

            @Override
            public void failure(TwitterException exception) {
                // Do something on failure
            }
        });
    }

    /*
        PassInformation is used in order to pass the data to the object
        This method can be used to send the data to the Database | Twitter & Facebook
    */
    private void passInformation(Profile profile){
        String id = (profile != null) ? profile.getId() : "User not logged in";
        String firstName = (profile != null) ? profile.getFirstName() : "User not logged in";
        String secondName = (profile != null) ? profile.getLastName() : "User not logged in";

        if(id != null && firstName != null && secondName != null){
            Login loginFB = new Login();
            loginFB.setUser_Id(id);
            loginFB.setFirst_Name(firstName);
            loginFB.setLast_Name(secondName);

            Log.d("First Name: ", " " + loginFB.getFirst_Name());
            Log.d("Second Name: ", " " + loginFB.getLast_Name());
            Log.d("ID: ", " " + loginFB.getUser_Id());
        }else{
            Log.d("Problem: ", " Empty field from the user.");
        }
    }
    private void passInformationTwitter(TwitterSession session){
        Login loginTwitter = new Login();
        if(session != null) {
            Long id = session.getUserId();
            String username = session.getUserName();
            String idT = id.toString();
            loginTwitter.setUser_Id(idT);
            loginTwitter.setFirst_Name(username);
        }
    }

    // Not Used
    public void onClick(View v){
        if(v == newButton){
            loginfbButton.performClick();
        }
    }

    // onResume | onPause | onStop is used to control the state of the activity | Do not Modify
    @Override
    protected void onResume(){
        super.onResume();
    }
    @Override
    protected void onPause(){
        super.onPause();
    }
    @Override
    protected void onStop(){
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
        super.onStop();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        loginTwitter.onActivityResult(requestCode,resultCode,data);
        callManager.onActivityResult(requestCode,resultCode,data);
    }
}
