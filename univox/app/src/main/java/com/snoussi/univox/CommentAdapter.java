package com.snoussi.univox;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private List<Comment> mComments;
    private Context mContext;


    public CommentAdapter(List<Comment> mComments, Context mContext) {
        this.mComments = mComments;
        this.mContext = mContext;
    }


    @NonNull
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_list_item,parent,false);

        return new CommentAdapter.ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.ViewHolder holder, int position) {
        holder.commentAuthor.setText(mComments.get(position).getUsername());
        holder.commentContent.setText(mComments.get(position).getComment());

    }

    @Override
    public int getItemCount() {
        return mComments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView commentAuthor;
        private TextView commentContent;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            commentAuthor = itemView.findViewById(R.id.commentauthor);
            commentContent = itemView.findViewById(R.id.commentContent);
        }
    }
}
