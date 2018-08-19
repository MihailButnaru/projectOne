package com.burn.burn.controller.home;


import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.burn.burn.R;

public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView mMainNav;
    private FrameLayout mMainFrame;
    private HomeFragment homeFragment;
    private NotificationFragment notificationFragment;
    private ProfileFragment profileFragment;

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
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();
    }
}
