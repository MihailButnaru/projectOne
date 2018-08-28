package com.burn.burn.controller.comicDetails;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.nfc.Tag;
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
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ComicProfile extends AppCompatActivity implements View.OnClickListener {
    private static final int FIRST_IMAGE = 1;
    private static final int SECOND_IMAGE = 2;
    private static final int THIRD_IMAGE = 3;
    private static final String TAG = "COMIC PROFILE";
    private StorageReference mStorageRef;                   // Database Reference
    private StorageTask mUploadTask;                    // Storage Task
    private Button submit_comic;

    // Image URI
    private Uri first_image;
    private Uri second_image;
    private Uri third_image;

    // Images
    private ImageView first_image_to_upload;
    private ImageView second_image_to_upload;
    private ImageView third_image_to_upload;

    // Database | Storage
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comic_profile);

        mStorageRef = FirebaseStorage.getInstance().getReference("Comics");


        first_image_to_upload = findViewById(R.id.firstImage);
        first_image_to_upload.setOnClickListener(this);

        second_image_to_upload = findViewById(R.id.second_image);
        second_image_to_upload.setOnClickListener(this);

        third_image_to_upload = findViewById(R.id.third_image);
        third_image_to_upload.setOnClickListener(this);

        // Button Submit to the server FireBase
        submit_comic = findViewById(R.id.submit_comic);
        submit_comic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mUploadTask != null && mUploadTask.isInProgress()){
                    Toast.makeText(ComicProfile.this, "Upload in progress", Toast.LENGTH_SHORT);
                }else{
                    uploadFile();
                }
            }
        });

    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.firstImage:
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, FIRST_IMAGE);
                break;
            case R.id.second_image:
                Intent second_gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(second_gallery, SECOND_IMAGE);
                break;
            case R.id.third_image:
                Intent third_gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(third_gallery, THIRD_IMAGE);
                break;
            default:
                Log.d(TAG,"Problem with the image action");
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == FIRST_IMAGE && data != null) {
                first_image = data.getData();
                first_image_to_upload.setImageURI(first_image);
            }else if(requestCode == SECOND_IMAGE && data != null){
                second_image = data.getData();
                second_image_to_upload.setImageURI(second_image);
            }else if(requestCode == THIRD_IMAGE && data != null){
                third_image = data.getData();
                third_image_to_upload.setImageURI(third_image);
            }else{
                Log.d(TAG, " Error No Image was selected.");
            }
        }
    }

    private void uploadFile(){
        if(first_image != null){
            mStorageRef = FirebaseStorage.getInstance().getReference().child("Comics").child(first_image.getLastPathSegment());
            mStorageRef.putFile(first_image).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                    // One image is working Multiple images are not working
                    // Need to fix this, as well as the name and price from the text need to fix, image download url not working.
                    if(task.isSuccessful()){
                        Log.d("TAG", " " + task.getResult());
                        Map update_images = new HashMap();
                        update_images.put("id", "fake_id");
                        update_images.put("image_one", task.getResult().toString());
                        update_images.put("comic_name", "Naruto");
                        update_images.put("comic_price", "10");

                        db.collection("comic_profile").add(update_images).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG,"FAILRE");

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

}
