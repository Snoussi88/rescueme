package com.snoussi.univox;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FollowersList extends Fragment {
    private RecyclerView followerRecycler;
    private ArrayList<User> followerList;
    private RecyclerView.LayoutManager layoutManager;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.followerlist,container,false);

        initView(view);

        followerList  = new ArrayList<>();
        followerRecycler.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(followerRecycler.getContext(), 1);
        followerRecycler.addItemDecoration(dividerItemDecoration);




        FirebaseDatabase.getInstance().getReference("users")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        followerList.clear();
                        for (DataSnapshot user: dataSnapshot.getChildren()){
                            User follower = user.getValue(User.class);
                            followerList.add(follower);


                            AdapterFollowers adapter = new AdapterFollowers(followerList,view.getContext());
                            adapter.notifyDataSetChanged();
                            followerRecycler.setAdapter(adapter);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


        return view;
    }

    private void initView(View view) {
        followerRecycler = view.findViewById(R.id.followerRecycler);
        layoutManager = new LinearLayoutManager(view.getContext());
    }
}
