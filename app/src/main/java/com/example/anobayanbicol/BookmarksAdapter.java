package com.example.anobayanbicol;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BookmarksAdapter extends RecyclerView.Adapter<BookmarksAdapter.ViewHolder> {

    private List<QuestionModel> quesList;
    public BookmarksAdapter(List<QuestionModel> quesList){
        this.quesList = quesList;
    }

    @NonNull
    @Override
    public BookmarksAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.answer_item_layout, parent, false);

        return new BookmarksAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String ques = quesList.get(position).getQuestion();
        String a = quesList.get(position).getOptionA();
        String b = quesList.get(position).getOptionB();
        String c = quesList.get(position).getOptionC();
        String d = quesList.get(position).getOptionD();
        int ans =quesList.get(position).getCorrectans();

        holder.setData(position, ques, a,b,c,d,ans);

    }

    @Override
    public int getItemCount() {
        return quesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView quesNo, question, optionA, optionB, optionC, optionD, result;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            quesNo = itemView.findViewById(R.id.quesNo);
            question = itemView.findViewById(R.id.question);
            optionA = itemView.findViewById(R.id.optionA);
            optionB = itemView.findViewById(R.id.optionB);
            optionC = itemView.findViewById(R.id.optionC);
            optionD = itemView.findViewById(R.id.optionD);
            result = itemView.findViewById(R.id.result);

        }

        public void setData(int position, String ques, String a, String b, String c, String d, int ans) {
            quesNo.setText("Question " + (position + 1));
            question.setText(ques);
            optionA.setText("A: " + a);
            optionB.setText("B: " + b);
            optionC.setText("C: " + c);
            optionD.setText("D: " + d);

            if (ans == 1){
                result.setText("Answer: " + a);
            } else if (ans == 2) {
                result.setText("Answer: " + b);
            } else if (ans == 3) {
                result.setText("Answer: " + c);
            } else if (ans == 4) {
                result.setText("Answer: " + d);
            }
        }


    }
}
