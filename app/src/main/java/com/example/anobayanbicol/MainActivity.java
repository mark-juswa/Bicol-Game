package com.example.anobayanbicol;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.anobayanbicol.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private ActivityMainBinding binding;

    private BottomNavigationView bottomNavigationView;
    private FrameLayout main_frame;

    private TextView drawerProfileName, drawerProfileText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        DBQuery.g_firestore = FirebaseFirestore.getInstance();


        // Toolbar and Drawer setup
        setSupportActionBar(binding.appBarMain.toolbar);
        getSupportActionBar().setTitle("");

        drawerLayout = binding.drawerLayout;
        toggle = new ActionBarDrawerToggle(
                this, drawerLayout, binding.appBarMain.toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close
        );
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.sili_red));
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Bottom navigation setup
        bottomNavigationView = findViewById(R.id.bottom_nav_bar);
        main_frame = findViewById(R.id.main_frame);

        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavListener);

        // Drawer nav setup
        NavigationView navigationView = binding.navView;
        navigationView.setNavigationItemSelectedListener(this);

        // Set profile name in drawer
        drawerProfileName = navigationView.getHeaderView(0).findViewById(R.id.nav_drawer_name);
        drawerProfileText = navigationView.getHeaderView(0).findViewById(R.id.nav_drawer_img);

        String name = DBQuery.myProfile.getName();
        drawerProfileName.setText(name);
        drawerProfileText.setText(name.toUpperCase().substring(0, 1));

        // Set default fragment
        setFragment(new CategoryFragment());
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener bottomNavListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    int id = item.getItemId();
                    if (id == R.id.nav_home) {
                        setFragment(new CategoryFragment());
                        return true;
                    } else if (id == R.id.nav_leaderboard) {
                        setFragment(new LeaderboardFragment());
                        return true;
                    } else if (id == R.id.nav_account) {
                        setFragment(new AccountFragment());
                        return true;
                    }
                    return false;
                }
            };

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            setFragment(new CategoryFragment());
        } else if (id == R.id.nav_leaderboard) {
            setFragment(new LeaderboardFragment());
        } else if (id == R.id.nav_account) {
            setFragment(new AccountFragment());
        }else if (id == R.id.nav_bookmark) {
            Intent intent = new Intent(MainActivity.this, BookMarksActivity.class);
            startActivity(intent);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(main_frame.getId(), fragment);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
