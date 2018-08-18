package com.burn.burn.controller.profile;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.burn.burn.R;
import com.burn.burn.controller.login.LoginActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private final String TAG = "burn.profileactivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Get the intent that started this activity.
        Intent intent = getIntent();
        String username = intent.getStringExtra(LoginActivity.EXTRA_USERNAME);
        Log.d(TAG, "Welcome, " + username + ".");

        // Save button.
        final Button btnSave = findViewById(R.id.btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TextView txtCity = findViewById(R.id.txt_city);
                TextView txtCountry = findViewById(R.id.txt_country);
                TextView txtStreet = findViewById(R.id.txtStreet);

                String fname = "Joe";
                String lname = "Bloggs";
                String houseNum = "25";
                String street = txtStreet.getText().toString();
                String city = txtCity.getText().toString();
                String country = txtCountry.getText().toString();

//                Profile profile = new Profile(fname, lname, city, country,
//                        houseNum+" "+street);
//
//                pushUser(profile);
            }
        });


    }

    private void pushUser(Profile p) {
        if(p == null)
            return;

        // Access a Cloud Firestore instance from your Activity.
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Create document to push to cloud.
        Map<String, Object> profile = new HashMap<>();
        profile.put("first_name", p.getFirstName());
        profile.put("last_name", p.getLastName());
        profile.put("city", p.getCity());
        profile.put("country", p.getCountry());
        profile.put("line_1", p.getLine1());
        if (p.getLine2() != null)
            profile.put("line_2", p.getLine2());

        // Send document to cloud.
        db.collection("profile")
                .add(profile)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

}
