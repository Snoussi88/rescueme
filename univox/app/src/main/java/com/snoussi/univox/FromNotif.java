package com.snoussi.univox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FromNotif extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TextView content;
    private TextView Author;
    private ImageView type;
    private TextView numLikes;
    private TextView numComments;
    private CommentAdapter adapter;
    private List<Comment> comments = new ArrayList<>();
    private String postId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_from_notif);

        Intent i = getIntent();
        Bundle b = i.getExtras();

        if (b!=null) {
            postId = b.getString("postId");
        }


        recyclerView = findViewById(R.id.commentRecycler2);
        content = findViewById(R.id.Discpost_textView2);
        Author = findViewById(R.id.Discuser2);
        type = findViewById(R.id.Discprofilepic2);
        numComments = findViewById(R.id.commentView3);
        numLikes = findViewById(R.id.tV_likes2);
        adapter = new CommentAdapter(comments, getApplicationContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),1);
        recyclerView.addItemDecoration(dividerItemDecoration);

        FirebaseDatabase.getInstance().getReference("posts").child(postId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Post post = dataSnapshot.getValue(Post.class);
                        content.setText(post.getTxt());
                        Author.setText(post.getAuthor());
                        numComments.setText(""+post.getNumComms());
                        numLikes.setText(""+post.getLikes());
                        //type for later


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        FirebaseDatabase.getInstance().getReference("comments").child(postId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Comment comment = dataSnapshot.getValue(Comment.class);
                comments.add(comment);
                adapter.notifyDataSetChanged();

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
