package com.example.anobayanbicol;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class LeaderboardFragment extends Fragment {

    private TextView totalUsers, myImgText, myScore, myRank;
    private RecyclerView usersView;
    private RankAdapter adapter;

    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "LeaderboardPrefs";
    private static final String KEY_LAST_RANK = "last_rank";
    private static final String KEY_LAST_SCORE = "last_score";


    public LeaderboardFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Leaderboard");

        initViews(view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        usersView.setLayoutManager(layoutManager);

        adapter = new RankAdapter(DBQuery.g_userList);
        usersView.setAdapter(adapter);

        DBQuery.getTopUsers(new MyCompleteListener() {
            @Override
            public void onSuccess() {
                adapter.notifyDataSetChanged();

                if (DBQuery.myPerformance.getScore() != 0){
                    if (! DBQuery.meTopList){
                        calculateRank();
                    }

                    // Inside onSuccess() callback after DBQuery.getTopUsers()
                    myScore.setText("Score: " + DBQuery.myPerformance.getScore());
                    myRank.setText("Rank: " + DBQuery.myPerformance.getRank());

                    int previousRank = sharedPreferences.getInt(KEY_LAST_RANK, -1);
                    int previousScore = sharedPreferences.getInt(KEY_LAST_SCORE, -1);
                    int currentRank = DBQuery.myPerformance.getRank();
                    int currentScore = DBQuery.myPerformance.getScore();

                    if (previousRank != -1 && currentRank > previousRank) {
                        sendNotification("Alert!", "Someone surpassed your score!");
                    } else if (!DBQuery.g_userList.isEmpty()) {
                        RankModel topUser = DBQuery.g_userList.get(0);  // assuming list is sorted by rank
                        if (!topUser.getName().equals(DBQuery.myPerformance.getName())) {
                            if (previousRank == 1 && currentRank != 1) {
                                sendNotification("You've been dethroned!", topUser.getName() + " is the new rank 1.");
                            }
                        }

                    }

// Save current rank and score
                    sharedPreferences.edit()
                            .putInt(KEY_LAST_RANK, currentRank)
                            .putInt(KEY_LAST_SCORE, currentScore)
                            .apply();

                }
            }

            @Override
            public void onFailure() {
                Toast.makeText(getContext(), "Something went wrong! Please try again later", Toast.LENGTH_SHORT).show();
            }
        });

        totalUsers.setText("Total Users: " + DBQuery.g_usersCount);
        myImgText.setText(DBQuery.myPerformance.getName().toUpperCase().substring(0, 1));
        return view;
    }

    private void initViews(View view){
        totalUsers = view.findViewById(R.id.total_users);
        myImgText = view.findViewById(R.id.my_img_text);
        myScore = view.findViewById(R.id.total_score);
        myRank = view.findViewById(R.id.rank);
        usersView = view.findViewById(R.id.recyclerView);
        sharedPreferences = getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);


    }

    private void calculateRank() {
        int lowTopScore = DBQuery.g_userList.get(DBQuery.g_userList.size() - 1).getScore();
        int remainingSlots = DBQuery.g_usersCount - 20;
        int mySlot = (DBQuery.myPerformance.getScore() * remainingSlots) / lowTopScore;
        int rank;

        if (lowTopScore != DBQuery.myPerformance.getScore()) {
            rank = DBQuery.g_usersCount - mySlot;
        } else {
            rank = 21;
        }

        DBQuery.myPerformance.setRank(rank);
    }

    private void sendNotification(String title, String message) {
        NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = "leaderboard_channel";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId, "Leaderboard Alerts", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Notifications for leaderboard changes");
            channel.enableLights(true);
            channel.enableVibration(true);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), channelId)
                .setSmallIcon(R.drawable.logo) // Change this icon if needed
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        notificationManager.notify((int) System.currentTimeMillis(), builder.build());
    }


}