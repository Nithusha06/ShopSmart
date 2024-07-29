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

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.view.GravityCompat;
import androidx.core.app.ActivityCompat;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CAMERA_PERMISSION = 200;
    private ActivityMainBinding binding;
    private DrawerAdapter drawerAdapter;
    private ImageUpload imageUpload;
    private ImageView profileImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Request camera permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
        }

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

        // Initialize ImageUpload
        profileImageView = binding.navigationView.getHeaderView(0).findViewById(R.id.profile_image);
        imageUpload = new ImageUpload(this, profileImageView);
        profileImageView.setOnClickListener(view -> showImageChoiceDialog());

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

    private void showImageChoiceDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.image_upload, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        Button chooseGallery = dialogView.findViewById(R.id.button_choose_gallery);
        Button useCamera = dialogView.findViewById(R.id.button_use_camera);
        Button cancel = dialogView.findViewById(R.id.button_cancel);

        chooseGallery.setOnClickListener(v -> {
            imageUpload.chooseImageFromGallery();
            dialog.dismiss();
        });

        useCamera.setOnClickListener(v -> {
            imageUpload.captureImageFromCamera();
            dialog.dismiss();
        });

        cancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageUpload.handleImageResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permissions granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Camera and storage permissions are required to use this feature", Toast.LENGTH_SHORT).show();
            }
        }
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
