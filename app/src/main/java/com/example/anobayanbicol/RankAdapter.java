package com.example.anobayanbicol;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RankAdapter extends RecyclerView.Adapter<RankAdapter.ViewHolder> {

    private List<RankModel> userList;

    public RankAdapter(List<RankModel> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public RankAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rank_item_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RankAdapter.ViewHolder holder, int position) {
        String name = userList.get(position).getName();
        int score = userList.get(position).getScore();
        int rank = userList.get(position).getRank();

        holder.setData(name, score, rank);
    }

    @Override
    public int getItemCount() {
        if (userList.size() > 20){
            return 10;
        }else{
            return userList.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtName, txtRank, txtScore, imgTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.name);
            txtRank = itemView.findViewById(R.id.rank);
            txtScore = itemView.findViewById(R.id.score);
            imgTV = itemView.findViewById(R.id.img_text);
        }
        private void setData(String name, int score, int rank){
            txtName.setText(name);
            txtScore.setText("Score: "+score);
            imgTV.setText(name.toUpperCase().substring(0,1));
            txtRank.setText("Rank: " + rank);

        }
    }


}
