package com.snoussi.univox;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.UnknownServiceException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AdapterConversations extends RecyclerView.Adapter<AdapterConversations.ViewHolder> {
    private Context mContext;
    private List<String> mConversations;

    public AdapterConversations(Context mContext, List<String> mConversations) {
        this.mContext = mContext;
        this.mConversations = mConversations;
    }

    @NonNull
    @Override
    public AdapterConversations.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.conversations_row,parent,false);
        return new AdapterConversations.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterConversations.ViewHolder holder, final int position) {
        String userKey = mConversations.get(position);
        String[] userKeyList = userKey.split("_");
        final List<String> al= Arrays.asList(userKeyList);

        holder.relativeLayout.setAnimation(AnimationUtils.loadAnimation(mContext,R.anim.fade_scale_animation));


        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment chat = new ChatFragment();
                Bundle bundle = new Bundle();
                bundle.putString("conversator",al.get(position));
                chat.setArguments(bundle);

                ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_down,R.anim.enter_from_down).replace(R.id.fragment_container,chat,"chat").addToBackStack("chat").commit();
            }
        });

        FirebaseDatabase.getInstance().getReference("users")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot item : dataSnapshot.getChildren()){
                                User user = item.getValue(User.class);
                                if (user.getKey().equals(al.get(1))){
                                    holder.from.setText(user.getName());

                                }
                            }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        FirebaseDatabase.getInstance().getReference("conversations")
                .child("conv_"+ FirebaseAuth.getInstance().getCurrentUser().getUid()).child(userKey)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ArrayList<Message> messages = new ArrayList<>();
                        Collections.sort(messages, new Comparator<Message>() {
                            @Override
                            public int compare(Message o1, Message o2) {
                                return Long.compare(o1.getMessageTime(), o2.getMessageTime());
                            }
                        });
                        for (DataSnapshot item : dataSnapshot.getChildren()){
                            Message messageObj = item.getValue(Message.class);
                            messages.add(messageObj);

                        }
                        holder.lastmessage.setText(messages.get(messages.size()-1).getMessageText());

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



    }

    @Override
    public int getItemCount() {
        return mConversations.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView lastmessage;
        private ImageView interlocutorPicture;
        private TextView from;
        private RelativeLayout relativeLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            from = itemView.findViewById(R.id.conversationFrom);
            interlocutorPicture = itemView.findViewById(R.id.conversatorpicture);
            lastmessage = itemView.findViewById(R.id.lastmessage);
            relativeLayout = itemView.findViewById(R.id.convlayout);
        }
    }
}
