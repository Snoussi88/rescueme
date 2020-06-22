package com.snoussi.univox;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterFollowers extends RecyclerView.Adapter<AdapterFollowers.ViewHolder> {
    private ArrayList<User> followers;
    private Context mContext;


    public AdapterFollowers(ArrayList<User> followers, Context mContext) {
        this.followers = followers;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public AdapterFollowers.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.followers_row,parent,false);
        return new AdapterFollowers.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterFollowers.ViewHolder holder, final int position) {
        holder.nickname.setText(followers.get(position).getName());
        holder.reputation.setText(""+followers.get(position).getReputation());


        holder.nickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment chat = new ChatFragment();
                Bundle bundle = new Bundle();
                bundle.putString("conversator",followers.get(position).getKey());
                chat.setArguments(bundle);

                ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_down,R.anim.enter_from_down).replace(R.id.fragment_container,chat,"chat").addToBackStack("chat").commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return followers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView reputation;
        private TextView nickname;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nickname = itemView.findViewById(R.id.followername);
            reputation = itemView.findViewById(R.id.follower_rep);
        }
    }
}
