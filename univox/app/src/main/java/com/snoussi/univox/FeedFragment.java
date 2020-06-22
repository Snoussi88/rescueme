package com.snoussi.univox;


import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.LongDef;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.SphericalUtil;

import static com.google.maps.android.SphericalUtil.computeDistanceBetween;


public class FeedFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private View view;
    private FloatingActionButton floatingActionButton;
    private DatabaseReference myRef;
    private FirebaseDatabase database;
    private MyAdapter adapter;
    private List<Post> posts;
    private List<String> keys;
    private RecyclerView.LayoutManager layoutManager;
    private RelativeLayout filterView;
    private FirebaseUser user;
    private List<Post> filterPosts;
    private long currentFirstVisible;
    private ProgressBar progressBar;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_feed,container,false);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("posts");
        posts = new ArrayList<>();
        keys = new ArrayList<>();
        user = FirebaseAuth.getInstance().getCurrentUser();
        filterView = view.findViewById(R.id.viewbydist);
        progressBar = view.findViewById(R.id.loading);


        layoutManager = new LinearLayoutManager(view.getContext());
        mRecyclerView = view.findViewById(R.id.mylistview);
        mRecyclerView.setLayoutManager(layoutManager);
        (mRecyclerView.getLayoutManager()).scrollToPosition((int)currentFirstVisible);
        adapter = new MyAdapter(posts,keys,FeedFragment.this,user);
        mRecyclerView.setAdapter(adapter);





        FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("scrollPosition").exists()){
                    currentFirstVisible = (long) dataSnapshot.child("scrollPosition").getValue();
                    currentFirstVisible = currentFirstVisible + 3;
                }else{
                    currentFirstVisible =0;
                }
                Log.d("sos","lastVisibleItemPosition "+currentFirstVisible);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





















        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                posts.clear();
                keys.clear();
                for (DataSnapshot keyNode:dataSnapshot.getChildren()){
                    Post post = keyNode.getValue(Post.class);
                    keys.add(keyNode.getKey());
                    posts.add(post);
                }
                adapter.notifyItemChanged(posts.size()-1);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        filterView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter();
            }
        });




        floatingActionButton = view.findViewById(R.id.floating_action_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Fragment tweet = new SendPost();
               getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_down,R.anim.enter_from_down).replace(R.id.fragment_container,tweet,"tweet").addToBackStack("tweet").commit();
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("sos","OnDestroy called");
        FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("scrollPosition").setValue(((LinearLayoutManager)mRecyclerView.getLayoutManager()).findFirstVisibleItemPosition());

    }

    @Override
    public void onResume() {
        super.onResume();
        mRecyclerView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            boolean running = true;
            @Override
            public void run() {
                while(running){
                    progressBar.setVisibility(View.INVISIBLE);
                    mRecyclerView.getLayoutManager().scrollToPosition((int)currentFirstVisible);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    running = false;
                }

            }
        };
        handler.postDelayed(runnable,1000);

    }

    private void filter(){
        FirebaseDatabase.getInstance().getReference("users").child(user.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User updatedUser = dataSnapshot.getValue(User.class);
                        LatLng userPosition = new LatLng(updatedUser.getLat(),updatedUser.getLongt());
                        filterPosts = new ArrayList<>();
                        for (Post item : posts){
                            LatLng postPosition = new LatLng(item.getLat(),item.getLongt());
                            Double dist = computeDistanceBetween(userPosition,postPosition);
                            if (dist<= 400000.00){
                                filterPosts.add(item);


                            }
                        }
                        adapter.filterList((ArrayList<Post>) filterPosts);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }
}