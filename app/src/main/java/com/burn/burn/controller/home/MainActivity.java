package com.burn.burn.controller.home;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.burn.burn.R;
import com.burn.burn.controller.comicDetails.ComicProfile;
import com.burn.burn.controller.login.LoginActivity;

public class MainActivity extends AppCompatActivity {

    private Button comic_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        comic_profile = (Button) findViewById(R.id.item_profile);
        comic_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextComicProfile();
            }
        });
    }

    private void nextComicProfile(){
        Intent next_profile = new Intent(MainActivity.this, ComicProfile.class);
        next_profile.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(next_profile);
    }
}
