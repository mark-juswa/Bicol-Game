package com.example.anobayanbicol;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CategoryFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        // Find the CardView Button
        CardView cardViewButton = view.findViewById(R.id.cardViewButton);

        // Set Click Listener to Navigate to ChooseDifficultyFragment
        cardViewButton.setOnClickListener(v -> {
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.main_frame, new DifficultyFragment());
            transaction.addToBackStack(null); // Enables Back Navigation
            transaction.commit();
        });

        return view;
    }
}