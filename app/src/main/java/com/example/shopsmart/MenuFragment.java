package com.example.shopsmart;

import android.graphics.PorterDuff;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class MenuFragment extends Fragment {

    public MenuFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        // Find the ImageButton in your layout
        ImageButton menuButton = view.findViewById(R.id.menu_arrow_img_btn);

        // Set a tint color programmatically
        int tintColor = getResources().getColor(R.color.arrow_img_btn);
        menuButton.setColorFilter(tintColor, PorterDuff.Mode.SRC_IN);

        // Set an OnClickListener for the ImageButton
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle button click action here
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });

        return view;
    }
}
