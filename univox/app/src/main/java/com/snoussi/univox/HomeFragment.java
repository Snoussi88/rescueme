package com.snoussi.univox;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private AutoCompleteTextView autoCompleteTextView;
    private List<User> userList;
    private ImageView search;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home,container,false);

        autoCompleteTextView = view.findViewById(R.id.actv);
        search = view.findViewById(R.id.searchview);

        //fill the list
        userList = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot item : dataSnapshot.getChildren()){
                    User user = item.getValue(User.class);
                    if (!userList.contains(user)){
                        userList.add(user);
                    }

                    AutoCompleteUsersAdapter autoCompleteUsersAdapter = new AutoCompleteUsersAdapter(view.getContext(),userList);
                    autoCompleteTextView.setAdapter(autoCompleteUsersAdapter);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        view.findViewById(R.id.card_google).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(),MapsActivity.class);
                //Intent intent = new Intent(view.getContext(),OsmMapActivity.class);
                startActivity(intent);
            }
        });

        view.findViewById(R.id.card_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(),Profile.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
