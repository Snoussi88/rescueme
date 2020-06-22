package com.snoussi.univox;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class InboxFragment extends Fragment {
    private RecyclerView conversationRecycler;
    private FloatingActionButton sendMail;
    private ArrayList<String> conversations;
    private DatabaseReference refconversations;
    private FirebaseUser firebaseUser;
    private AdapterConversations adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.inbox_layout, container, false);

        initViews(view);

        sendMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment write = new FollowersList();
                getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_down, R.anim.enter_from_down).replace(R.id.fragment_container, write, "write").addToBackStack("write").commit();
            }
        });

        refconversations.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot conversation : dataSnapshot.getChildren()){
                    conversations.add(conversation.getKey());
                    conversationRecycler.setAdapter(adapter);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

    private void initViews(View view) {
        conversations = new ArrayList<>();
        adapter = new AdapterConversations(view.getContext(),conversations);
        conversationRecycler = view.findViewById(R.id.conversations);
        conversationRecycler.setLayoutManager(new LinearLayoutManager(view.getContext()));
        sendMail = view.findViewById(R.id.floating_send_mail);

         firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
         refconversations = FirebaseDatabase.getInstance()
                 .getReference("conversations").child("conv_"+firebaseUser.getUid());
    }
}
