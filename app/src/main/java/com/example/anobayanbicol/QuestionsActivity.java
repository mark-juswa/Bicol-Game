package com.example.anobayanbicol;

import static android.view.View.VISIBLE;
import static com.example.anobayanbicol.DBQuery.ANSWERED;
import static com.example.anobayanbicol.DBQuery.NOT_VISITED;
import static com.example.anobayanbicol.DBQuery.REVIEW;
import static com.example.anobayanbicol.DBQuery.UNANSWERED;
import static com.example.anobayanbicol.DBQuery.g_questList;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import java.util.concurrent.TimeUnit;

public class QuestionsActivity extends AppCompatActivity {

    private RecyclerView questionsView;
    private TextView tvQuesID, timerTV, catNameTV;
    private ImageView prevQuesB, nextQuesB, quesListB, markImage;
    private ImageButton drawerCloseB, bookmarkB;
    private int quesID;
    private Button  finishButton, clearButton, flagButton;
    private DrawerLayout drawer;
    QuestionsAdapter quesAdapter;
    private GridView quesListGV;
    private QuestionGridAdapter gridAdapter;
    private CountDownTimer timer;
    private long timeLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.questions_list_layout);

        init();

        quesAdapter = new QuestionsAdapter(DBQuery.g_questList);
        questionsView.setAdapter(quesAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        questionsView.setLayoutManager(layoutManager);

        gridAdapter = new QuestionGridAdapter(this, g_questList.size());
        quesListGV.setAdapter(gridAdapter);

        setSnapHelper();
        setClickListeners();
        startTimer();
    }

    private void init() {
        questionsView = findViewById(R.id.questions_view);
        tvQuesID = findViewById(R.id.tv_quesID);
        timerTV = findViewById(R.id.tv_timer);
        prevQuesB = findViewById(R.id.btnPrevious);
        nextQuesB = findViewById(R.id.btnNext);
        // If you need to access the "Finish" button or others, also include:
        finishButton = findViewById(R.id.btnSubmit);
        clearButton = findViewById(R.id.btnClear);
        flagButton = findViewById(R.id.btnMarkReview);
        quesListB = findViewById(R.id.ques_list_gridB);
        drawer = findViewById(R.id.drawer_layout);
        drawerCloseB = findViewById(R.id.drawerCloseB);
        markImage = findViewById(R.id.mark_image);
        quesListGV = findViewById(R.id.ques_list_gv);
        bookmarkB = findViewById(R.id.qa_bookmark);

        quesID = 0;
        tvQuesID.setText("1/"+String.valueOf(DBQuery.g_questList.size()));

        g_questList.get(0).setStatus(UNANSWERED);

        if (g_questList.get(0).isBookmarked()){
            bookmarkB.setImageResource(R.drawable.ic_bookmarked);
        }else{
            bookmarkB.setImageResource(R.drawable.ic_bookmark_empty);
        }

    }

    private void setSnapHelper(){
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(questionsView);

        questionsView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                View view = snapHelper.findSnapView(recyclerView.getLayoutManager());
                quesID = recyclerView.getLayoutManager().getPosition(view);

                if (DBQuery.g_questList.get(quesID).getStatus() == NOT_VISITED)
                    DBQuery.g_questList.get(quesID).setStatus(UNANSWERED);

                if (g_questList.get(quesID).getStatus() == REVIEW){
                    markImage.setVisibility(View.VISIBLE);
                }else{
                    markImage.setVisibility(View.GONE);
                }

                tvQuesID.setText(String.valueOf(quesID + 1) + "/" + String.valueOf(DBQuery.g_questList.size()));

                if (g_questList.get(quesID).isBookmarked()){
                    bookmarkB.setImageResource(R.drawable.ic_bookmarked);
                }else{
                    bookmarkB.setImageResource(R.drawable.ic_bookmark_empty);
                }

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    private void setClickListeners(){
        prevQuesB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quesID > 0){
                    questionsView.smoothScrollToPosition(quesID - 1);
                }
            }
        });

        nextQuesB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quesID < DBQuery.g_questList.size() - 1){
                    questionsView.smoothScrollToPosition(quesID + 1);
                }
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DBQuery.g_questList.get(quesID).setSelectedAns(-1);
                g_questList.get(quesID).setStatus(UNANSWERED);
                markImage.setVisibility(View.GONE);
                quesAdapter.notifyItemChanged(quesID);

            }
        });

        quesListB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (! drawer.isDrawerOpen(GravityCompat.END)){
                    gridAdapter.notifyDataSetChanged();
                    drawer.openDrawer(GravityCompat.END);
                }
            }
        });
        drawerCloseB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerOpen(GravityCompat.END)){
                    drawer.closeDrawer(GravityCompat.END);
                }
            }
        });
        flagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (markImage.getVisibility() != VISIBLE){
                    markImage.setVisibility(VISIBLE);
                    g_questList.get(quesID).setStatus(REVIEW);
                }else{
                    markImage.setVisibility(View.GONE);
                    if (g_questList.get(quesID).getSelectedAns() != -1){
                        g_questList.get(quesID).setStatus(ANSWERED);
                    }else {
                        g_questList.get(quesID).setStatus(UNANSWERED);
                    }
                }

            }
        });

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitTest();
            }
        });

        bookmarkB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToBookMark();
            }
        });

    }

    private void submitTest(){
        AlertDialog.Builder builder = new AlertDialog.Builder(QuestionsActivity.this);
        builder.setCancelable(true);

        View view = getLayoutInflater().inflate(R.layout.alert_dialog_layout, null);

        Button btnCancel = view.findViewById(R.id.btnCancel);
        Button btnConfirm = view.findViewById(R.id.btnYes);

        builder.setView(view);

        final AlertDialog alertDialog = builder.create();
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.cancel();
                alertDialog.dismiss();

                sendCompletionNotification();

                Intent intent = new Intent(QuestionsActivity.this, ScoreActivity.class);

                long totalTime = DBQuery.g_testList.get(DBQuery.g_selected_test_index).getTime() * 60 * 1000;
                intent.putExtra("TIME_TAKEN", totalTime - timeLeft);

                startActivity(intent);
                QuestionsActivity.this.finish();
            }
        });
        alertDialog.show();
    }

    public void goToQuestion(int position){
        questionsView.smoothScrollToPosition(position);
        if (drawer.isDrawerOpen(GravityCompat.END)){
            drawer.closeDrawer(GravityCompat.END);
        }
    }

    private void startTimer(){
        long totalTime = DBQuery.g_testList.get(DBQuery.g_selected_test_index).getTime()*60*1000;

        timer = new CountDownTimer(totalTime + 1000, 1000){

            @Override
            public void onTick(long remainingTime) {

                timeLeft = remainingTime;

                String time = String.format("%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(remainingTime),
                        TimeUnit.MILLISECONDS.toSeconds(remainingTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(remainingTime)));
                timerTV.setText(time);
            }

            @Override
            public void onFinish() {
                Intent intent = new Intent(QuestionsActivity.this, ScoreActivity.class);

                long totalTime = DBQuery.g_testList.get(DBQuery.g_selected_test_index).getTime() * 60 * 1000;
                intent.putExtra("TIME_TAKEN", totalTime - timeLeft);


                startActivity(intent);
                QuestionsActivity.this.finish();
            }
        };
        timer.start();
    }

    private void addToBookMark(){
        if (g_questList.get(quesID).isBookmarked()){

            g_questList.get(quesID).setBookmarked(false);
            bookmarkB.setImageResource(R.drawable.ic_bookmark_empty);

        }else{
            g_questList.get(quesID).setBookmarked(true);
            bookmarkB.setImageResource(R.drawable.ic_bookmarked);
        }
    }

    private void sendCompletionNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = "completion_channel";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId, "Quiz Completion", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Notifications for quiz completions");
            channel.enableLights(true);
            channel.enableVibration(true);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.logo)  // Change to your app icon if needed
                .setContentTitle("Congratulations!")
                .setContentText("You've successfully completed the quiz.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        notificationManager.notify((int) System.currentTimeMillis(), builder.build());
    }

}