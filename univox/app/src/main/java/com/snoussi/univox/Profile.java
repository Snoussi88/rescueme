package com.snoussi.univox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Profile extends AppCompatActivity {
    private FirebaseUser user;
    private TextView username;
    private Button follow_button;

    private TextView helped;
    private TextView followers;
    private TextView following;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        init();

        follow_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference("users").child(user.getUid())
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (!dataSnapshot.child("followers").exists()){
                                   ArrayList<String> newFollower = new ArrayList<>();
                                   newFollower.add(user.getUid());
                                    FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).
                                            child("followers").setValue(newFollower);
                                } else{
                                    GenericTypeIndicator<ArrayList<String>> t = new GenericTypeIndicator<ArrayList<String>>() {};
                                    ArrayList<String> followers = dataSnapshot.child("followers").getValue(t);
                                    if (!followers.contains(user.getUid())){
                                        followers.add(user.getUid());
                                        FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).
                                                child("followers").setValue(followers);

                                    }



                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
            }
        });
        FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).child("followers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                GenericTypeIndicator<ArrayList<String>> t = new GenericTypeIndicator<ArrayList<String>>() {};
                if (dataSnapshot.exists()){
                    ArrayList<String> followers1 = dataSnapshot.getValue(t);
                    followers.setText(""+followers1.size());
                }else{
                    followers.setText(""+0);
                }




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    private void init(){
        user = FirebaseAuth.getInstance().getCurrentUser();
        username = findViewById(R.id.user);
        follow_button = findViewById(R.id.follow_button);
        followers = findViewById(R.id.followers_text);
        following = findViewById(R.id.following_text);



    }
}
