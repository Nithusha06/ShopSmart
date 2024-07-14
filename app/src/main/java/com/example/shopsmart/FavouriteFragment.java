package com.example.shopsmart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.graphics.PorterDuff;
import android.widget.ImageButton;

public class FavouriteFragment extends Fragment {

    public FavouriteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favourite, container, false);

        // Find the ImageButton in your layout
        ImageButton backButton = view.findViewById(R.id.imageButton);

        // Set a tint color programmatically
        int tintColor = getResources().getColor(R.color.arrow_img_btn);
        backButton.setColorFilter(tintColor, PorterDuff.Mode.SRC_IN);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to HomeFragment
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });

        return view;
    }
}
