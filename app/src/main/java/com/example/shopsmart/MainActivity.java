package com.example.shopsmart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import androidx.core.content.ContextCompat;

import com.example.shopsmart.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

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

        replaceFragment(new HomeFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.favourite:
                    replaceFragment(new FavouriteFragment());
                    break;
                case R.id.more:
                    replaceFragment(new MenuFragment());
                    break;
                // Add cases for other tabs as needed
            }
            return true;
        });

        // Set up the bottom navigation menu
        setupBottomNavigationView();
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    private void setupBottomNavigationView() {
        // Clear existing menu items
        binding.bottomNavigationView.getMenu().clear();

        List<BottomTabItemType> bottomTabList = getBottomTabItemTypes();
        for (BottomTabItemType tab : bottomTabList) {
            switch (tab) {
                case HOME:
                    binding.bottomNavigationView.getMenu().add(0, R.id.home, 0, "Home").setIcon(R.drawable.img_home);
                    break;
                case FAVOURITE:
                    binding.bottomNavigationView.getMenu().add(0, R.id.favourite, 0, "Favourite").setIcon(R.drawable.img_home_bottom_tab_favorite);
                    break;
                case MORE:
                    binding.bottomNavigationView.getMenu().add(0, R.id.more, 1, "More").setIcon(R.drawable.img_home_bottom_tab_more);
                    break;
                // Add cases for other tabs as needed
            }
        }
    }

    public List<BottomTabItemType> getBottomTabItemTypes() {
        List<BottomTabItemType> bottomTabList = new ArrayList<>();
        bottomTabList.add(BottomTabItemType.HOME);
        bottomTabList.add(BottomTabItemType.FAVOURITE);
        bottomTabList.add(BottomTabItemType.MORE);
        // Add more items as needed
        return bottomTabList;
    }
}
