package com.example.shopsmart.adapter;

import android.app.Activity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.shopsmart.R;
import com.google.android.material.navigation.NavigationView;

public class DrawerAdapter implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Activity activity;

    public DrawerAdapter(Activity activity, DrawerLayout drawerLayout, NavigationView navigationView) {
        this.activity = activity;
        this.drawerLayout = drawerLayout;
        this.navigationView = navigationView;

        // Set the navigation view listener
        this.navigationView.setNavigationItemSelectedListener(this);

        // Handle profile layout click
        View headerView = this.navigationView.getHeaderView(0);
        LinearLayout profileLayout = headerView.findViewById(R.id.layout_profile);
        profileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle profile click
                // Add actions to be taken when the profile is clicked
            }
        });

        // Handle drawer close button
        ImageButton drawerCloseButton = headerView.findViewById(R.id.drawer_close_button);
        drawerCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here
        switch (item.getItemId()) {
            case R.id.nav_home:
                // Handle home action
                break;

            case R.id.nav_entercode:
                // Handle profile action
                break;

            case R.id.nav_location:
                // Handle profile action
                break;
            case R.id.nav_settings:
                // Handle settings action
                break;
            default:
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START); // Close the drawer
        return true;
    }
}
