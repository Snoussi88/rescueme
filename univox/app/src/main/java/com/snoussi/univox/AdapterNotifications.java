package com.snoussi.univox;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AdapterNotifications extends RecyclerView.Adapter<AdapterNotifications.ViewHolder> {
    private List<NotificationClassifier> notif;
    private Context mContext;

    public AdapterNotifications(List<NotificationClassifier> notif, Context mContext) {
        this.notif = notif;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public AdapterNotifications.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notif_list,parent,false);
        return new AdapterNotifications.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterNotifications.ViewHolder holder, final int position) {
        switch (notif.get(position).getType()){
            case "request":
                holder.type.setImageResource(R.drawable.ic_party);
                holder.content.setText("new help request from: "+notif.get(position).getContent());
                holder.content.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogHelpRequest helpRequest = new DialogHelpRequest();
                        Bundle bundle = new Bundle();
                        bundle.putString("from",notif.get(position).getContent());
                        helpRequest.setArguments(bundle);
                        helpRequest.show(((FragmentActivity) mContext).getSupportFragmentManager(),"dialog");
                    }
                });
                holder.linearLayout.setAnimation(AnimationUtils.loadAnimation(mContext,R.anim.fade_scale_animation));
                break;
            case "comment":
                holder.type.setImageResource(R.drawable.ic_money);
                holder.content.setText(notif.get(position).getContent()+"comment on post "+notif.get(position).getPostId());
                holder.content.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext,FromNotif.class);
                        intent.putExtra("postId",notif.get(position).getPostId());
                        mContext.startActivity(intent);
                    }
                });
                holder.linearLayout.setAnimation(AnimationUtils.loadAnimation(mContext,R.anim.fade_scale_animation));
                break;
            case "nearPost":
                holder.type.setImageResource(R.drawable.ic_elder);
                holder.content.setText(notif.get(position).getContent()+"a post near you, id: "+notif.get(position).getPostId());
                holder.content.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext,FromNotif.class);
                        intent.putExtra("postId",notif.get(position).getPostId());
                        mContext.startActivity(intent);
                    }
                });

                holder.linearLayout.setAnimation(AnimationUtils.loadAnimation(mContext,R.anim.fade_scale_animation));
                break;
            case "requestAccepted":
                holder.type.setImageResource(R.drawable.ic_party);
                holder.content.setText("help request accepted: "+notif.get(position).getContent());
                holder.content.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext,HelpActivity.class);
                        intent.putExtra("helpTo",notif.get(position).getContent());
                        mContext.startActivity(intent);

                    }
                });
                holder.linearLayout.setAnimation(AnimationUtils.loadAnimation(mContext,R.anim.fade_scale_animation));
                break;

        }

    }

    public void filterNotif(ArrayList<NotificationClassifier> filteredlist){
        notif = filteredlist;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return notif.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView content;
        private ImageView type;
        private LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            content = itemView.findViewById(R.id.notifContent);
            type = itemView.findViewById(R.id.picture_type);
            linearLayout = itemView.findViewById(R.id.linearlayout_notif);
        }
    }
}
