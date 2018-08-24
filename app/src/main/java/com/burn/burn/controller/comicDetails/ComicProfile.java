package com.burn.burn.controller.comicDetails;

import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.burn.burn.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class ComicProfile extends AppCompatActivity implements View.OnClickListener {
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final String TAG = "COMIC PROFILE";
    private StorageReference imagePath;

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
            Uri  selectedImage = data.getData();
            imageToUpload.setImageURI(selectedImage);

            // IMAGE IS UPLOADED WHEN IT IS SELECTED, NOW NEEDS TO BE CHANGED WHEN YOU CLICK THE BUTTON SUBMIT,, THIS WAS USED FOR TESTING
            // CHANGE THE METHOD IN THE SUBMIT BUTTON. IT WILL BE BETTER.
            // CONNECT THE IMAGE WITH THE PERSON WHO SUBMITTED BY PASSING THE ID AND THE NAME, LOCATION AND PRICE TO THE DATABASE, THE IMAGE NEEDS TO BE AN ID. TO BE BASSED TO THE STORAGE.
            imagePath = FirebaseStorage.getInstance().getReference().child("Comics").child(selectedImage.getLastPathSegment());
            imagePath.putFile(selectedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(ComicProfile.this, "Uploaded", Toast.LENGTH_SHORT);
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
