package com.snoussi.univox;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OthersProfiles extends AppCompatActivity {
    private TextView user;
    private TextView followers;
    private TextView following;
    private TextView helped;
    private TextView local;
    private String key;
    private User userPro;
    private Button followButt;
    private FirebaseUser currentUser;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.othersprofile);

        initViews();

        Intent i = getIntent();
        Bundle b = i.getExtras();
        if (b != null){
            key = b.getString("user_id");
        }

        FirebaseDatabase.getInstance().getReference("users").child(key)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        userPro = dataSnapshot.getValue(User.class);
                        user.setText(userPro.getName());
                        if (dataSnapshot.child("followers").exists()){
                            followers.setText(""+userPro.getFollowers().size());
                        }else{
                            followers.setText(""+0);
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        FirebaseDatabase.getInstance().getReference("users").child(key)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        userPro = dataSnapshot.getValue(User.class);
                        if (dataSnapshot.child("following").exists()){
                            following.setText(""+userPro.getFollowing().size());
                        }else{
                            following.setText(""+0);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


        followButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference("users").child(key)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.child("followers").exists()) {
                                    User user2 = dataSnapshot.getValue(User.class);
                                    ArrayList<String> userfollowers = new ArrayList<>(user2.getFollowers());
                                    if (!userfollowers.contains(currentUser.getUid())) {
                                        userfollowers.add(currentUser.getUid());
                                        FirebaseDatabase.getInstance().getReference("users").child(key)
                                                .child("followers").setValue(userfollowers);
                                        FirebaseDatabase.getInstance().getReference("users").child(currentUser.getUid())
                                                .child("following").addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        GenericTypeIndicator<ArrayList<String>> t = new GenericTypeIndicator<ArrayList<String>>() {
                                                        };
                                                        if (dataSnapshot.exists()) {
                                                            ArrayList<String> following1 = dataSnapshot.getValue(t);
                                                            if (!following1.contains(key)) {
                                                                following1.add(key);
                                                                FirebaseDatabase.getInstance().getReference("users").child(currentUser.getUid())
                                                                        .child("following").setValue(following1);
                                                            }
                                                            } else {
                                                            ArrayList<String> following1 = new ArrayList<>();
                                                            following1.add(key);
                                                            FirebaseDatabase.getInstance().getReference("users").child(currentUser.getUid())
                                                                    .child("following").setValue(following1);

                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });
                                    }


                                } else {
                                    ArrayList<String> userfollowers = new ArrayList<>();
                                    userfollowers.add(currentUser.getUid());
                                    FirebaseDatabase.getInstance().getReference("users").child(key)
                                            .child("followers").setValue(userfollowers);

                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
            }
        });

    }

    private void initViews() {
        user = findViewById(R.id.user2);
        followers = findViewById(R.id.followers_text2);
        following = findViewById(R.id.following_text2);
        helped = findViewById(R.id.helped_text2);
        local = findViewById(R.id.locali2);
        followButt = findViewById(R.id.follow_button2);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
    }
}
