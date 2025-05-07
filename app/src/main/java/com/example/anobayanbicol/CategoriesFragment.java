package com.example.anobayanbicol;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageButton;


public class CategoriesFragment extends Fragment {

    private GridView catView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_categories, container, false);

        catView = view.findViewById(R.id.cat_Grid);

        //loadCategories();
        CategoryAdapter adapter = new CategoryAdapter(DBQuery.g_catlist);
        catView.setAdapter(adapter);

        ImageButton backButton = view.findViewById(R.id.buttonBack);
        backButton.setOnClickListener(v ->
                requireActivity().getOnBackPressedDispatcher().onBackPressed()
        );

        return view;
    }

    /*
    private void loadCategories() {
        catlist.clear();
        catlist.add(new CategoryModel("1", "Bicol Basics", 4));
        catlist.add(new CategoryModel("2", "Timplado!", 4));
        catlist.add(new CategoryModel("3", "Sisay Sinda?", 4));
        catlist.add(new CategoryModel("4", "Saro, Duwa, Tulo", 4));
        catlist.add(new CategoryModel("5", "Tradisyon", 4));
        catlist.add(new CategoryModel("6", "Turismo", 4));


    }

     */
}