package com.burn.burn.controller.login;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
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
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import android.widget.Toast;

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

    // ----- FACEBOOK LOGIN ----- [DO NOT MODIFY]
    private static final String FACEBOOKTAG = "FacebookLogin";
    private FirebaseAuth mAuth;
    private CallbackManager mCallbackManager;
    private LoginButton loginfbButton;
    private Button newButton;
    private AccessTokenTracker accessTokenTracker;      // TokenTracker of the user [UNIQUE]
    private ProfileTracker profileTracker;              // ProfileTracker [FName,SName]
    // ------ END ---------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);

        // -------- FACEBOOK LOGIN ------------
        mAuth = FirebaseAuth.getInstance();
        mCallbackManager = CallbackManager.Factory.create();
        loginfbButton = (LoginButton) findViewById(R.id.loginfbButton);
        newButton = (Button) findViewById(R.id.fb);
        newButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                loginfbButton.performClick();
            }
        });
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

        loginfbButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(FACEBOOKTAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
                if(Profile.getCurrentProfile() == null){
                    profileTracker  = new ProfileTracker() {
                        @Override
                        protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                            Log.d("LOGIN", "SUCCESSSS");
                            passInformation(currentProfile);        // Passing the information to the object
                            profileTracker.stopTracking();
                        }
                    };
                }else{
                    Log.d("LOGIN", "FAILEEED");
                    Profile profile = Profile.getCurrentProfile();
                    passInformation(profile);
                }
            }

            @Override
            public void onCancel() {
                Log.d(FACEBOOKTAG, "facebook:cancel:");

            }

            @Override
            public void onError(FacebookException error) {
                Log.d(FACEBOOKTAG, "facebook:error:");

            }
        });

        // --------- END -----------------------------
    }

    // ----- METHODS IMPLEMENTATIONS
    @Override
    public void onStart(){
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(FACEBOOKTAG, "handleFacebookAccessToken:" + token);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(FACEBOOKTAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(FACEBOOKTAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    // ------ INFORMTATION TO THE OBJECT -----
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
    // ------- END---------


    // ---- SIGNOUT FACEBOOK NOT WORKING
    public void signOut() {
        mAuth.signOut();
        LoginManager.getInstance().logOut();
        updateUI(null);
    }
    // ---- END SIGN OUT


    // ---- FACEBOOK UPDATE User INTERFACE
    private void updateUI(FirebaseUser user) {
        //hideProgressDialog();
        if (user != null) {
          //  mStatusTextView.setText(getString(R.string.facebook_status_fmt, user.getDisplayName()));
            //mDetailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));

            //findViewById(R.id.button_facebook_login).setVisibility(View.GONE);
            //findViewById(R.id.button_facebook_signout).setVisibility(View.VISIBLE);
        } else {
            //mStatusTextView.setText(R.string.signed_out);
            //mDetailTextView.setText(null);

            //findViewById(R.id.button_facebook_login).setVisibility(View.VISIBLE);
            //findViewById(R.id.button_facebook_signout).setVisibility(View.GONE);
        }
    }
    // END USER INTERFACE FACEBOOK
}
