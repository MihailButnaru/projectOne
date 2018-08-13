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

import java.io.Console;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import android.content.pm.*;
import java.util.Arrays;


/**
 *  Login Activity [Facebook | Twitter]
 *
 *  This activity will allow you to login using the Facebook and Twitter account.
 *
 */

public class LoginActivity extends AppCompatActivity {


    private CallbackManager callManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    private LoginButton loginfbButton;
    private Button newButton;
    private static final String TAG = "Login Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        callManager = CallbackManager.Factory.create();

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
    }



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

    public void onClick(View v){
        if(v == newButton){
            loginfbButton.performClick();
        }
    }
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
        callManager.onActivityResult(requestCode,resultCode,data);
    }
}
