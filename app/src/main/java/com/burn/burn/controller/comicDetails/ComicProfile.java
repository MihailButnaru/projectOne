package com.burn.burn.controller.comicDetails;

import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.burn.burn.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ComicProfile extends AppCompatActivity implements View.OnClickListener {
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final String TAG = "COMIC PROFILE";
    private StorageReference imagePath;
    private Button submit_comic;


    ImageView imageToUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comic_profile);

        imageToUpload = (ImageView) findViewById(R.id.firstImage);
        imageToUpload.setOnClickListener(this);


        // Access a Cloud Firestore instance from your Activity.
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();


        // Button Submit to the server FireBase
        submit_comic = findViewById(R.id.submit_comic);
        submit_comic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }




    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.firstImage:
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("request CODE", " " + requestCode);
        Log.d("DATANULL", "" + data);
        Log.d("result code", " " + resultCode);
        if(requestCode == RESULT_LOAD_IMAGE && data != null){
            Log.d("IMAGEVIEW", "WORKING");
            final Uri  selectedImage = data.getData();
            imageToUpload.setImageURI(selectedImage);

            // IMAGE IS UPLOADED WHEN IT IS SELECTED, NOW NEEDS TO BE CHANGED WHEN YOU CLICK THE BUTTON SUBMIT,, THIS WAS USED FOR TESTING
            // CHANGE THE METHOD IN THE SUBMIT BUTTON. IT WILL BE BETTER.
            // CONNECT THE IMAGE WITH THE PERSON WHO SUBMITTED BY PASSING THE ID AND THE NAME, LOCATION AND PRICE TO THE DATABASE, THE IMAGE NEEDS TO BE AN ID. TO BE BASSED TO THE STORAGE.
            imagePath = FirebaseStorage.getInstance().getReference().child("Comics").child(selectedImage.getLastPathSegment());
//            imagePath.putFile(selectedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    Toast.makeText(ComicProfile.this, "Uploaded", Toast.LENGTH_SHORT);
//
//                }
//            });

            imagePath.putFile(selectedImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if(task.isSuccessful()){
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        Map update_image = new HashMap<>();
                        update_image.put("Image", "test");
                        db.collection("comic_profile").add(update_image).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
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
            });
        }
    }


    @Override
    protected  void onResume(){
        super.onResume();

    }
    @Override
    protected void onPause(){
        super.onPause();

    }

    public void comics(){



    }
}
