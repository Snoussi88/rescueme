package com.snoussi.univox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.Key;
import java.util.HashMap;
import java.util.Map;

public class CommentSection extends AppCompatActivity {

    private DatabaseReference ref;
    private String postid;
    private Button commentbutt;
    private EditText commentText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_section);

        Intent i = getIntent();
        Bundle b = i.getExtras();

        if (b!=null){
             postid = b.getString("postid");
        }

        final FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference("comments");

        commentText = findViewById(R.id.comment_edit);
        commentbutt = findViewById(R.id.buttcomment);

        commentbutt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = commentText.getText().toString();
                Map<String, String> map = new HashMap<String, String>();
                map.put("comment", commentText.getText().toString());
                map.put("username", user1.getDisplayName());
                ref.child(postid).push().setValue(map);
            }
        });
    }
}


