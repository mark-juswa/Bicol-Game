package com.example.anobayanbicol;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class DifficultyFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout
        View view = inflater.inflate(R.layout.fragment_difficulty, container, false);

        ImageButton backButton = view.findViewById(R.id.buttonBack);
        backButton.setOnClickListener(v ->
                requireActivity().getOnBackPressedDispatcher().onBackPressed()
        );

        CardView cardEasy = view.findViewById(R.id.cardEasy);
        cardEasy.setOnClickListener(v -> {
            Fragment categoriesFragment = new CategoriesFragment();
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.main_frame, categoriesFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        return view;
    }
}