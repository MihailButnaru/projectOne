package com.burn.burn.controller.profile;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Profile {

    private final static String TAG = "burn.profile.Profile";

    public static void save(Profile profile) {

        if (isProfileValid(profile)) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            Map<String, Object> pStore = new HashMap<>();
            pStore.put("fullname", profile.getFullname());
            pStore.put("email", profile.getEmail());
            pStore.put("phone", profile.getPhone());
            pStore.put("gender", profile.getGender());
            pStore.put("city", profile.getCity());

            db.collection("profile")
                    .add(pStore)
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

    private static boolean isProfileValid(Profile profile) {
        if (profile.fullname != null)
            return true; // TODO: Validate
        return false;
    }

    private String id;
    private String firstName;
    private String lastName;
    private String fullname;
    private String city;
    private String country;
    private String line1;
    private String line2;
    private String email;
    private String phone;
    private String gender;

    public Profile() {

    }

    public Profile(String fullname) {
        this.fullname = fullname;
    }

    public Profile(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Profile(String id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Profile(String firstName, String lastName, String city, String country, String line1, String line2) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.country = country;
        this.line1 = line1;
        this.line2 = line2;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {

        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLine1() {
        return line1;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public String getLine2() {
        return line2;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

}
