package com.example.anobayanbicol;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class CategoryAdapter extends BaseAdapter {

    private List<CategoryModel> cat_list;

    public CategoryAdapter(List<CategoryModel> cat_list) {
        this.cat_list = cat_list;
    }

    @Override
    public int getCount() {
        return cat_list.size();
    }

    @Override
    public Object getItem(int position) {
        return cat_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View myView;

        if (view == null) {
            myView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cat_item_layout, viewGroup, false);
        } else {
            myView = view;
        }

        TextView catName = myView.findViewById(R.id.catName);
        CategoryModel model = cat_list.get(i);
        catName.setText(model.getName());

        myView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DBQuery.g_selected_cat_index = i;
                Intent intent = new Intent(viewGroup.getContext(), TestActivity.class);
                //intent.putExtra("CAT_INDEX", i);
                //intent.putExtra("NO_OF_TESTS", model.getNoOfTests());
                viewGroup.getContext().startActivity(intent);
            }
        });



        return myView;
    }
}
