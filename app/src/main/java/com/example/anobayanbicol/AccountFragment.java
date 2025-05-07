package com.example.anobayanbicol;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;


public class AccountFragment extends Fragment {

    private Button btnLogout;
    private TextView profile_img_text, name, tvUserName, rank, total_score, leaderB, bookmarkB, profileB;
    private BottomNavigationView bottomNavigationView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_account, container, false);

        init(view);

        String username = DBQuery.myProfile.getName();
        profile_img_text.setText(username.toUpperCase().substring(0, 1));
        total_score.setText(String.valueOf(DBQuery.myPerformance.getScore()));
        name.setText(username);

        if (DBQuery.g_userList.size() == 0){
            DBQuery.getTopUsers(new MyCompleteListener() {
                @Override
                public void onSuccess() {
                    if (DBQuery.myPerformance.getScore() != 0){
                        if (! DBQuery.meTopList){
                            calculateRank();
                        }

                        total_score.setText("" + DBQuery.myPerformance.getScore());
                        rank.setText("" + DBQuery.myPerformance.getRank());
                    }
                }

                @Override
                public void onFailure() {
                    Toast.makeText(getContext(), "Something went wrong! Please try again later", Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            total_score.setText(String.valueOf(DBQuery.myPerformance.getScore()));

            if (DBQuery.myPerformance.getScore() != 0)
                rank.setText(String.valueOf(DBQuery.myPerformance.getRank()));


    }

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                        .setTitle("Log Out")
                        .setMessage("Are you sure you want to log out?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            // Perform logout
                            FirebaseAuth.getInstance().signOut();

                            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                    .requestIdToken(getString(R.string.default_web_client_id))
                                    .requestEmail()
                                    .build();

                            GoogleSignInClient mGoogleClient = GoogleSignIn.getClient(getContext(), gso);
                            mGoogleClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Intent intent = new Intent(getContext(), LoginActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    requireActivity().finish();
                                }
                            });
                        })
                        .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                        .show();
            }
        });

        leaderB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomNavigationView.setSelectedItemId(R.id.nav_leaderboard);
            }
        });

        profileB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(getContext(), MyProfileActivity.class);
                startActivity(intent);
            }
        });

        bookmarkB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), BookMarksActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void init(View view){
        btnLogout = view.findViewById(R.id.btnLogOut);
        profile_img_text = view.findViewById(R.id.profile_img_text);
        name = view.findViewById(R.id.tvUserName);
        total_score = view.findViewById(R.id.total_score);
        rank = view.findViewById(R.id.rank);
        leaderB = view.findViewById(R.id.leaderB);
        bookmarkB = view.findViewById(R.id.bookmarkB);
        profileB = view.findViewById(R.id.profileB);
        bottomNavigationView = requireActivity().findViewById(R.id.bottom_nav_bar);
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
}