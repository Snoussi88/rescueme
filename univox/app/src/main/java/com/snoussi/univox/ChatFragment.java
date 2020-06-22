package com.snoussi.univox;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class ChatFragment extends Fragment {
    private DatabaseReference refconversations_user;
    private DatabaseReference refconversations_with;
    private String conversatorKey;
    private ImageView send;
    private EditText message;
    private ScrollView scrollView;
    private LinearLayout layout;
    private ArrayList<Message> messages;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.chat_layout,container,false);


        init(view);


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message_text = message.getText().toString();
                if (!message_text.equals("")){
                    Message message1 = new Message(message_text,FirebaseAuth.getInstance().getCurrentUser().getUid(),new Date().getTime());
                    refconversations_user.child("with_"+conversatorKey).push().setValue(message1);
                    refconversations_with.child("with_"+FirebaseAuth.getInstance().getCurrentUser().getUid()).push().setValue(message1);
                    message.setText("");
                }
            }
        });

        refconversations_user.child("with_"+conversatorKey).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                messages.clear();
                Message message1 = dataSnapshot.getValue(Message.class);
                messages.add(message1);
                Collections.sort(messages, new Comparator<Message>() {
                    @Override
                    public int compare(Message o1, Message o2) {
                        return Long.compare(o1.getMessageTime(), o2.getMessageTime());
                    }
                });

                for (Message i : messages){
                    if (i.getFrom().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                        addTextView(i.getMessageText(),2,view);
                    }else{
                        addTextView(i.getMessageText(),1,view);
                    }
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        return view;
    }

    private void addTextView(String messageText, int type,View view) {
        TextView textView = new TextView(view.getContext());
        textView.setText(messageText);
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.weight = 7.0f;

        if(type == 2) {
            lp2.gravity = Gravity.LEFT;
            textView.setTextColor(Color.BLACK);
            textView.setTextSize(22);
        }
        else{
            lp2.gravity = Gravity.RIGHT;
            textView.setTextColor(Color.BLACK );
            textView.setTextSize(22);
        }
        textView.setLayoutParams(lp2);
        layout.addView(textView);
        scrollView.fullScroll(View.FOCUS_DOWN);
    }


    private void init(View view) {
        conversatorKey = getArguments().getString("conversator");
        refconversations_user = FirebaseDatabase.getInstance().getReference("conversations")
                .child("conv_"+FirebaseAuth.getInstance().getCurrentUser().getUid());

        refconversations_with = FirebaseDatabase.getInstance().getReference("conversations")
                .child("conv_"+conversatorKey);



        send = view.findViewById(R.id.sendButton);
        message = view.findViewById(R.id.messageArea);
        scrollView = view.findViewById(R.id.scrollView);
        layout = view.findViewById(R.id.layout1);

        messages = new ArrayList<>();
    }
}
