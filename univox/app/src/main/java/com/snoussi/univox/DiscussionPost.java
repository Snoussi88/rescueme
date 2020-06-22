package com.snoussi.univox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

public class DiscussionPost extends AppCompatActivity {

    private String postId;
    private String postContent;
    private String postAuthor;
    private FirebaseDatabase database;
    private Integer postComms;
    private Integer postLikes;
    private List<Comment> comments = new ArrayList<>();
    private RecyclerView commentRecycler;
    private CommentAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion_post);


        Intent i = getIntent();
        Bundle b = i.getExtras();

        if (b!=null){
            postId= b.getString("clickid");
            postContent = b.getString("click");
            postAuthor = b.getString("clickAuthor");
            postComms = b.getInt("clickcoms");
            postLikes = b.getInt("likes");
        }

        TextView user = findViewById(R.id.Discuser);
        TextView content = findViewById(R.id.Discpost_textView);
        TextView  viewpostlikes = findViewById(R.id.tV_likes);

        user.setText(postAuthor);
        content.setText(postContent);
        viewpostlikes.setText(postLikes.toString()+" Likes");

        database = FirebaseDatabase.getInstance();
        DatabaseReference postRef = database.getReference("comments/"+postId);
        commentRecycler = findViewById(R.id.commentRecycler);
        commentRecycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CommentAdapter(comments,this);
        commentRecycler.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(commentRecycler.getContext(),1);
        commentRecycler.addItemDecoration(dividerItemDecoration);


        postRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Comment comment = dataSnapshot.getValue(Comment.class);
                comments.add(comment);
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


    }

}
