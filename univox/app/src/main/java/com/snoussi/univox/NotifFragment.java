package com.snoussi.univox;



import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;


import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.List;

import static com.google.firebase.auth.FirebaseAuth.getInstance;


public class NotifFragment extends Fragment {

    private FirebaseDatabase database;
    private DatabaseReference postRef;
    private DatabaseReference comRef;
    private FirebaseAuth mAuth;
    private NotificationManagerCompat notificationManager;
    private RecyclerView notifRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private AdapterNotifications adapter;
    private RelativeLayout filter1;
    private RelativeLayout filter2;
    private RelativeLayout filter3;

    private List<NotificationClassifier> comments;

    private List<NotificationClassifier> filter11;
    private List<NotificationClassifier> filter22;
    private List<NotificationClassifier> filter33;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_notification,container,false);
         //database init
        comments = new ArrayList<>();
        notifRecyclerView = view.findViewById(R.id.notifRecycler);
        layoutManager = new LinearLayoutManager(view.getContext());
        notifRecyclerView.setLayoutManager(layoutManager);







        filter1 = view.findViewById(R.id.filter1);
        filter2 = view.findViewById(R.id.filter2);
        filter3 = view.findViewById(R.id.filter3);

        filter11= new ArrayList<>();
        filter22= new ArrayList<>();
        filter33= new ArrayList<>();




        database = FirebaseDatabase.getInstance();
        postRef = database.getReference("posts");
        comRef = database.getReference("comments");
        //user init
        mAuth = getInstance();
        final String username = mAuth.getCurrentUser().getDisplayName();
        final String userKey = mAuth.getCurrentUser().getUid();
        notificationManager = NotificationManagerCompat.from(view.getContext());



        //init an empty arraylist for comments

        comments = new ArrayList<>();

        //init my listview


        postRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                comments.clear();
                for (final DataSnapshot keynode : dataSnapshot.getChildren()){
                    String authorKey = keynode.child("keyAuthor").getValue(String.class);
                    if (authorKey.equals(userKey)){
                        final String key = keynode.getKey();
                        comRef.child(keynode.getKey()).addValueEventListener(new ValueEventListener() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                                for (DataSnapshot item: dataSnapshot1.getChildren()){
                                    String comment = item.child("comment").getValue(String.class);
                                    NotificationClassifier classifier = new NotificationClassifier(comment,"comment",key);
                                    comments.add(classifier);
                                    filter11.add(classifier);
                                    adapter = new AdapterNotifications(comments,view.getContext());
                                    notifRecyclerView.setAdapter(adapter);

                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        postRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot post:dataSnapshot.getChildren()){
                    if (post.child("helpRequestSent").exists() && post.child("keyAuthor").getValue(String.class).equals(userKey)){
                        Post post1 = post.getValue(Post.class);
                        for (String item:post1.getHelpRequestSent()){
                            NotificationClassifier classifier = new NotificationClassifier(item,"request");
                            comments.add(classifier);
                            filter22.add(classifier);
                            adapter = new AdapterNotifications(comments,view.getContext());
                            notifRecyclerView.setAdapter(adapter);

                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FirebaseDatabase.getInstance().getReference("help_requests").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            HelpRequest helpRequest = dataSnapshot.getValue(HelpRequest.class);
                            String helpTo = helpRequest.getTo();
                            NotificationClassifier classifier = new NotificationClassifier(helpTo,"requestAccepted");
                            comments.add(classifier);
                            filter33.add(classifier);
                            adapter = new AdapterNotifications(comments,view.getContext());
                            notifRecyclerView.setAdapter(adapter);
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


        String userIdNode = FirebaseAuth.getInstance().getCurrentUser().getUid();

        database.getReference("users").child(userIdNode)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User userData = dataSnapshot.getValue(User.class);
                        final LatLng userPosition = new LatLng(userData.getLat(),userData.getLongt());
                        postRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot item : dataSnapshot.getChildren()){
                                    Post post = item.getValue(Post.class);
                                    LatLng postPosition = new LatLng(post.getLat(),post.getLongt());
                                    if (SphericalUtil.computeDistanceBetween(userPosition,postPosition)<=100000){
                                        NotificationClassifier classifier = new NotificationClassifier(post.getTxt(),"nearPost",post.getKey());
                                        comments.add(classifier);
                                        filter33.add(classifier);
                                    }
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        filter1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.filterNotif((ArrayList<NotificationClassifier>) filter11);
            }
        });
        filter2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.filterNotif((ArrayList<NotificationClassifier>) filter22);
            }
        });
        filter3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.filterNotif((ArrayList<NotificationClassifier>) filter33);
            }
        });


        return  view;
    }
}
