package com.snoussi.univox;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private FirebaseUser user;
    private DatabaseReference userRef;
    private TextView quote;
    private TextView auhtorquote;
    private RecyclerView notifrecyclerView;
    private BottomNavigationView navView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);


        navView = findViewById(R.id.bottom_nav);



        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        userRef = database.getReference("users").child(user.getUid());

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChildren()){
                    User currentUser = new User(user.getUid(),user.getDisplayName(),user.getEmail(),0,null,0.000,0.000);
                    userRef.setValue(currentUser);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        BadgeDrawable badgeDrawable = navView.getOrCreateBadge(R.id.notif_nav);
        badgeDrawable.setBackgroundColor(Color.BLUE);
        badgeDrawable.setBadgeTextColor(Color.WHITE);
        badgeDrawable.setMaxCharacterCount(3);
        badgeDrawable.setNumber(459);
        badgeDrawable.setVisible(true);


        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFrag = null;
                switch (item.getItemId()){
                    case R.id.home_nav:
                        //quote.setVisibility(View.GONE);
                        //auhtorquote.setVisibility(View.GONE);
                        selectedFrag = new  HomeFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFrag,"home").commit();
                        break;
                    case R.id.notif_nav:
                        //quote.setVisibility(View.GONE);
                        //auhtorquote.setVisibility(View.GONE);
                        selectedFrag = new NotifFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFrag,"notification").commit();
                        break;
                    case R.id.school_nav:
                        //quote.setVisibility(View.GONE);
                        //auhtorquote.setVisibility(View.GONE);
                        selectedFrag = new FeedFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFrag,"feed").commit();
                        break;
                    case R.id.inbox:
                        //quote.setVisibility(View.GONE);
                        //auhtorquote.setVisibility(View.GONE);
                        selectedFrag = new InboxFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFrag,"inbox").commit();
                        break;
                }

                //setCustomAnimation() for later use
                return true;
            }
        });


    }

}
