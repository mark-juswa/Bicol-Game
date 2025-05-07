package com.example.anobayanbicol;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AnswersAdapter extends RecyclerView.Adapter<AnswersAdapter.ViewHolder> {

    private List<QuestionModel> quesList;
    public AnswersAdapter(List<QuestionModel> quesList){
        this.quesList = quesList;
    }

    @NonNull
    @Override
    public AnswersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.answer_item_layout, parent, false);

        return new AnswersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnswersAdapter.ViewHolder holder, int position) {

        String ques = quesList.get(position).getQuestion();
        String a = quesList.get(position).getOptionA();
        String b = quesList.get(position).getOptionB();
        String c = quesList.get(position).getOptionC();
        String d = quesList.get(position).getOptionD();
        int selected = quesList.get(position).getSelectedAns();
        int ans =quesList.get(position).getCorrectans();

        holder.setData(position, ques, a,b,c,d, selected,ans);

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

        public void setData(int position, String ques, String a, String b, String c, String d, int selected, int ans) {
            quesNo.setText("Question " + (position + 1));
            question.setText(ques);
            optionA.setText("A: " + a);
            optionB.setText("B: " + b);
            optionC.setText("C: " + c);
            optionD.setText("D: " + d);

            resetOptionStyles();

            if (selected == ans) {
                setCorrectOption(ans);
                result.setText("Your answer is correct.");
                result.setTextColor(itemView.getContext().getColor(R.color.green));
            } else {
                setCorrectOption(ans);
                setWrongOption(selected);
                result.setText("Your answer is incorrect.");
                result.setTextColor(itemView.getContext().getColor(R.color.lava_red));
            }
        }

        private void resetOptionStyles() {
            optionA.setBackgroundResource(R.drawable.bg_neutral_option);
            optionB.setBackgroundResource(R.drawable.bg_neutral_option);
            optionC.setBackgroundResource(R.drawable.bg_neutral_option);
            optionD.setBackgroundResource(R.drawable.bg_neutral_option);
        }

        private void setCorrectOption(int ans) {
            switch (ans) {
                case 1: optionA.setBackgroundResource(R.drawable.bg_correct_option); break;
                case 2: optionB.setBackgroundResource(R.drawable.bg_correct_option); break;
                case 3: optionC.setBackgroundResource(R.drawable.bg_correct_option); break;
                case 4: optionD.setBackgroundResource(R.drawable.bg_correct_option); break;
            }
        }

        private void setWrongOption(int selected) {
            switch (selected) {
                case 1: optionA.setBackgroundResource(R.drawable.bg_wrong_option); break;
                case 2: optionB.setBackgroundResource(R.drawable.bg_wrong_option); break;
                case 3: optionC.setBackgroundResource(R.drawable.bg_wrong_option); break;
                case 4: optionD.setBackgroundResource(R.drawable.bg_wrong_option); break;
            }
        }

    }
}
