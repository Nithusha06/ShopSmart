package com.example.shopsmart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.shopsmart.adapter.DrawerAdapter;
import com.example.shopsmart.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import androidx.core.view.GravityCompat;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private DrawerAdapter drawerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Firebase Auth
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        // Initialize DrawerAdapter
        DrawerLayout drawerLayout = binding.drawerLayout;
        NavigationView navigationView = binding.navigationView;

        if (drawerLayout == null || navigationView == null) {
            throw new NullPointerException("DrawerLayout or NavigationView is not initialized properly.");
        }

        drawerAdapter = new DrawerAdapter(this, drawerLayout, navigationView);

        // Set up initial fragment
        replaceFragment(new HomeFragment());

        // Handle bottom navigation view item selection
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.favourite:
                    replaceFragment(new FavouriteFragment());
                    return true;
                case R.id.more:
                    drawerLayout.openDrawer(GravityCompat.END); // Open the drawer when "more" is clicked
                    return true;
                default:
                    return false;
            }
        });


        // Set up the bottom navigation menu
        setupBottomNavigationView();
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.addToBackStack(null); // Add fragment to back stack
        fragmentTransaction.commit();
    }

    private void setupBottomNavigationView() {
        // Clear existing menu items
        binding.bottomNavigationView.getMenu().clear();

        List<BottomTabItemType> bottomTabList = getBottomTabItemTypes();
        for (BottomTabItemType tab : bottomTabList) {
            switch (tab) {
                case FAVOURITE:
                    binding.bottomNavigationView.getMenu().add(0, R.id.favourite, 0, "Favourite")
                            .setIcon(R.drawable.img_home_bottom_tab_favorite);
                    break;
                case MORE:
                    binding.bottomNavigationView.getMenu().add(0, R.id.more, 1, "More")
                            .setIcon(R.drawable.img_home_bottom_tab_more);
                    break;
                // Add cases for other tabs as needed
            }
        }
    }

    public List<BottomTabItemType> getBottomTabItemTypes() {
        List<BottomTabItemType> bottomTabList = new ArrayList<>();
        bottomTabList.add(BottomTabItemType.FAVOURITE);
        bottomTabList.add(BottomTabItemType.MORE);
        // Add more items as needed
        return bottomTabList;
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}
