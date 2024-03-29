package com.burn.burn.controller.login;

import android.content.Context;
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

import com.burn.burn.controller.home.HomeActivity;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.TwitterAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import android.content.pm.*;
import android.widget.Toast;


/**
 *
 *  Created by Mihail Butnaru
 *  Login Activity [Facebook | Twitter]
 *
 *  This activity will allow you to login using the Facebook and Twitter account.
 *
 */

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "burn.LoginActivity";

    // ----- FACEBOOK LOGIN ----- [DO NOT MODIFY]
    private static final String FACEBOOKTAG = "FacebookLogin";
    private FirebaseAuth mAuth;
    private CallbackManager mCallbackManager;
    private LoginButton loginfbButton;
    private Button newButton;
    private AccessTokenTracker accessTokenTracker;      // TokenTracker of the user [UNIQUE]
    private ProfileTracker profileTracker;              // ProfileTracker [FName,SName]
    // ------ END ---------------------------------

    // ------ TWITTER LOGIN --- [DO NOT MODIFY]
    private static final String TWITTERTAG = "TwitterLogin";
    private FirebaseAuth tAuth;
    private TwitterLoginButton loginTwitter;
    private static final String TWITTER_KEY = "BeVSZlzqbOEWo56O7OBYRCca9";
    private static final String TWITTER_SECRET = "tPL3PWy6rFMazllusLBTYZ3PwzRglLxjuhR8dqwQxKBxSxoRo4";
    private Button new_button_twitter;
    // ------ END ----------------------------

    public static final String EXTRA_USERNAME = "com.burn.burn.MESSAGE";
    final int duration = Toast.LENGTH_SHORT; // Duration for which toast is on-screen.
    private Button btnBackdoor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final Context context = getApplicationContext(); // Necessary to create a Toast.
        // Switch to HomeActivity upon successful sign-in.
        final Intent intent = new Intent(this, HomeActivity.class);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /*
         * WARNING: Remove this test backdoor before going into production!
         */
        btnBackdoor = (Button) findViewById(R.id.btn_backdoor);
        btnBackdoor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent); // open HomeActivity.
            }
        });

        // -------- FACEBOOK LOGIN ------------
        FacebookSdk.sdkInitialize(getApplicationContext());
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

        // ----- TWITTER LOGIN -----------------
        TwitterAuthConfig authConfig = new TwitterAuthConfig(
                TWITTER_KEY, TWITTER_SECRET);
        TwitterConfig twitterConfig = new TwitterConfig.Builder(this)
                .twitterAuthConfig(authConfig).build();
        Twitter.initialize(twitterConfig);

        new_button_twitter = (Button) findViewById(R.id.tw);
        new_button_twitter.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                loginTwitter.performClick();
            }
        });
        loginTwitter = findViewById(R.id.loginTwitter);
        loginTwitter.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                Log.d(TWITTERTAG, "twitterLogin:success" + result);
                twitterLogin(intent, context, result.data);

            }

            @Override
            public void failure(TwitterException exception) {
                Log.w(TWITTERTAG, "twitterLogin:failure", exception);

                CharSequence text = "Twitter sign-in failed!";
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });
        // ---- TWITTER LOGIN -----------
    }


    /**
     * Calls twitter API for authentication and transitions to HomeActivity upon successful
     * sign-in.
     * @param intent
     * @param context
     * @param session
     */
    private void twitterLogin(final Intent intent,
                              final Context context, final TwitterSession session) {
        Log.d(TWITTERTAG, "twitterLogin:" + session);
        AuthCredential credential = TwitterAuthProvider.getCredential(
                session.getAuthToken().token,
                session.getAuthToken().secret);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        CharSequence toastMsg = "Sign-in failed!";
                        int toastDuration = Toast.LENGTH_LONG;
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TWITTERTAG, "signInWithCredential:success");
                            toastMsg = "Sign-in complete.";
                            Toast.makeText(context, toastMsg, toastDuration).show();

                            startActivity(intent); // Open MainActivity.


                            // TODO: Session should be sent to Firebase on a background thread.
                            FirebaseUser user = mAuth.getCurrentUser();
                            recordSession(session);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TWITTERTAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(context, toastMsg, toastDuration).show();
                            updateUI(null);
                        }
                    }
                });
    }

    public void recordSession(TwitterSession twitterSession) {
        TwitterCore.getInstance().getApiClient(twitterSession).getAccountService().verifyCredentials(false,true,false).enqueue(new Callback<User>() {
            @Override
            public void success(Result<User> userResult) {
                User user = userResult.data;

                try {
//                   Login lDetails = new Login();
                   String fullname = user.name;
                   String twitterid = String.valueOf(user.id);
                   String lastName = fullname.substring(0, fullname.length()-1);

                    com.burn.burn.controller.profile.Profile profile1 =
                            new com.burn.burn.controller.profile.Profile(twitterid,fullname, lastName);

                    pushUser(profile1);
//                   lDetails.setFirst_Name(fullname);
//                   lDetails.setUser_Id(twitterid);
//                   lDetails.setLast_Name(lastName);


//                 String secondName = fullname.substring(fullname.lastIndexOf(" "));
//                 String firstName = fullname.substring(0, fullname.lastIndexOf(" "));
//                 Log.d("USER ID: ", " "+ twitterid);
//                 Log.d("FULL NAME, " , " " + fullname);
//                 Log.d("FIRST NAME", " " + firstName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void failure(TwitterException e) {
            }
        });
    }
    // ---- END SESSION -----


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
        loginTwitter.onActivityResult(requestCode, resultCode, data);
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
//            Login loginFB = new Login();
//            loginFB.setUser_Id(id);
//            loginFB.setFirst_Name(firstName);
//            loginFB.setLast_Name(secondName);


            com.burn.burn.controller.profile.Profile profile1 =
                    new com.burn.burn.controller.profile.Profile(id,firstName, secondName);

            pushUser(profile1);

//            Log.d("First Name: ", " " + loginFB.getFirst_Name());
//            Log.d("Second Name: ", " " + loginFB.getLast_Name());
//            Log.d("ID: ", " " + loginFB.getUser_Id());
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

    private void pushUser(com.burn.burn.controller.profile.Profile p) {
        if(p == null)
            return;

        // Access a Cloud Firestore instance from your Activity.
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Create document to push to cloud.
        Map<String, Object> profile = new HashMap<>();
        profile.put("id", p.getId());
        profile.put("first_name", p.getFirstName());
        profile.put("last_name", p.getLastName());

        // Send document to cloud.
        db.collection("profile")
                .add(profile)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
//                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
//                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }


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
