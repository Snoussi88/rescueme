package com.snoussi.univox;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterMessage extends RecyclerView.Adapter<AdapterMessage.ViewHolder> {
    private List<Message> mMessages;
    private Context mContext;

    public AdapterMessage(Context mContext, List<Message> mMessages) {
        this.mContext = mContext;
        this.mMessages = mMessages;

    }

    @NonNull
    @Override



    public AdapterMessage.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_row,parent,false);
        return new AdapterMessage.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterMessage.ViewHolder holder, int position){
        holder.messageText.setText(mMessages.get(position).getMessageText());

    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView messageText;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.tv_message);
        }
    }
}

