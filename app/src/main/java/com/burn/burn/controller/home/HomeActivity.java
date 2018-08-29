package com.burn.burn.controller.home;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.burn.burn.R;
import com.burn.burn.controller.comicDetails.ComicProfile;

public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView mMainNav;
    private FrameLayout mMainFrame;
    private HomeFragment homeFragment;
    private NotificationFragment notificationFragment;
    private ProfileFragment profileFragment;
    private Button comic_profile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mMainFrame = (FrameLayout) findViewById(R.id.main_frame);
        mMainNav = (BottomNavigationView) findViewById(R.id.main_nav);
        final Menu menu = mMainNav.getMenu();
        menu.findItem(R.id.nav_home).setIcon(R.drawable.baseline_home_black_24dp);

        homeFragment = new HomeFragment();
        notificationFragment = new NotificationFragment();
        profileFragment = new ProfileFragment();

        setFragment(homeFragment);



        mMainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                menu.findItem(R.id.nav_home).setIcon(R.drawable.outline_home_black_24dp);
                menu.findItem(R.id.nav_notif).setIcon(R.drawable.outline_notifications_black_24dp);
                menu.findItem(R.id.nav_profile).setIcon(R.drawable.outline_person_black_24dp);

                switch (item.getItemId()) {
                    case R.id.nav_home:
                        item.setIcon(R.drawable.baseline_home_black_24dp);
                        setFragment(homeFragment);
                        return true;
                    case R.id.nav_notif:
                        item.setIcon(R.drawable.baseline_notifications_black_24dp);
                        setFragment(notificationFragment);
                        return true;
                    case R.id.nav_profile:
                        item.setIcon(R.drawable.baseline_person_black_24dp);
                        setFragment(profileFragment);
                        return true;
                }
                return false;
            }
        });

        comic_profile = (Button) findViewById(R.id.item_profile);
        comic_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextComicProfile();
            }
        });
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();
    }
    private void nextComicProfile(){
        Intent next_profile = new Intent(HomeActivity.this, ComicProfile.class);
        next_profile.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(next_profile);
    }
}
