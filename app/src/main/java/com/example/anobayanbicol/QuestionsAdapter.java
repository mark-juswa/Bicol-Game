package com.example.anobayanbicol;

import static com.example.anobayanbicol.DBQuery.ANSWERED;
import static com.example.anobayanbicol.DBQuery.REVIEW;
import static com.example.anobayanbicol.DBQuery.UNANSWERED;
import static com.example.anobayanbicol.DBQuery.g_questList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.ViewHolder> {

    private List<QuestionModel> questionsList;

    public QuestionsAdapter(List<QuestionModel> questionsList) {
        this.questionsList = questionsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.question_item_layout, viewGroup, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.setData(i);
    }

    @Override
    public int getItemCount() {
        return questionsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView ques;
        private Button optionA, optionB, optionC, optionD, prevSelectedB;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ques = itemView.findViewById(R.id.question_text);
            optionA = itemView.findViewById(R.id.option1);
            optionB = itemView.findViewById(R.id.option2);
            optionC = itemView.findViewById(R.id.option3);
            optionD = itemView.findViewById(R.id.option4);

            prevSelectedB = null;

        }
        private void setData(final int pos){
            ques.setText(questionsList.get(pos).getQuestion());
            optionA.setText(questionsList.get(pos).getOptionA());
            optionB.setText(questionsList.get(pos).getOptionB());
            optionC.setText(questionsList.get(pos).getOptionC());
            optionD.setText(questionsList.get(pos).getOptionD());

            setOption(optionA, 1, pos);
            setOption(optionB, 2, pos);
            setOption(optionC, 3, pos);
            setOption(optionD, 4, pos);

            optionA.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectOption(optionA, 1, pos);
                }
            });

            optionB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectOption(optionB, 2, pos);
                }
            });

            optionC.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectOption(optionC, 3, pos);
                }
            });

            optionD.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectOption(optionD, 4, pos);
                }
            });
        }

        private void selectOption(Button btn, int option_num, int quesID) {
            if (prevSelectedB == null) {
                btn.setBackgroundResource(R.drawable.selected_btn);
                btn.setTextColor(itemView.getContext().getResources().getColor(android.R.color.white));
                g_questList.get(quesID).setSelectedAns(option_num);

                changeStatus(quesID, ANSWERED);

                prevSelectedB = btn;
            } else {
                if (prevSelectedB.getId() == btn.getId()) {
                    btn.setBackgroundResource(R.drawable.rounded_button_white);
                    btn.setTextColor(itemView.getContext().getResources().getColor(android.R.color.black));
                    g_questList.get(quesID).setSelectedAns(-1);

                    changeStatus(quesID, UNANSWERED);
                    prevSelectedB = null;
                } else {
                    prevSelectedB.setBackgroundResource(R.drawable.rounded_button_white);
                    prevSelectedB.setTextColor(itemView.getContext().getResources().getColor(android.R.color.black));

                    btn.setBackgroundResource(R.drawable.selected_btn);
                    btn.setTextColor(itemView.getContext().getResources().getColor(android.R.color.white));
                    g_questList.get(quesID).setSelectedAns(option_num);

                    changeStatus(quesID, ANSWERED);
                    prevSelectedB = btn;
                }
            }
        }

        private void changeStatus(int id, int status){
            if (g_questList.get(id).getStatus() != REVIEW) {
                g_questList.get(id).setStatus(status);
            }
        }

        private void setOption(Button btn, int option_num, int quesID) {
            if (g_questList.get(quesID).getSelectedAns() == option_num) {
                btn.setBackgroundResource(R.drawable.selected_btn);
                btn.setTextColor(itemView.getContext().getResources().getColor(android.R.color.white));
            } else {
                btn.setBackgroundResource(R.drawable.rounded_button_white);
                btn.setTextColor(itemView.getContext().getResources().getColor(android.R.color.black));
            }
        }

    }
}
