package com.snoussi.univox;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<Post> mposts;
    private List<String> mkeys;
    private FeedFragment mContext;
    private long i = 0;
    private long d;
    private FirebaseUser mUser;
    private List<String> likedposts;


    public MyAdapter(List<Post> posts, List<String> keys, FeedFragment context, FirebaseUser user) {
        this.mposts = posts;
        this.mkeys = keys;
        this.mContext = context;
        this.mUser = user;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_list_item,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyAdapter.ViewHolder holder, final int position) {

        final LatLng postLoc =  new LatLng(mposts.get(position).getLat(),mposts.get(position).getLongt());

        FirebaseDatabase.getInstance().getReference("users").child(mUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User currentuser = dataSnapshot.getValue(User.class);
                LatLng userLoc = new LatLng(currentuser.getLat(),currentuser.getLongt());

                Double distance = SphericalUtil.computeDistanceBetween(userLoc,postLoc);

                holder.postLocation.setText(""+Math.round(distance/1000.00)+"Km");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        holder.author.setText(mposts.get(position).getAuthor());
        holder.content.setText(mposts.get(position).getTxt());

        holder.numLikes.setText(mposts.get(position).getLikes().toString());


        FirebaseDatabase.getInstance().getReference("comments").child(mkeys.get(position)).
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        i = 0;
                        for (DataSnapshot keynode : dataSnapshot.getChildren()){
                            if (keynode.hasChildren()){
                                i = i +1;
                            }

                        }
                        d = i;
                        holder.commentsview.setText(""+d);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        holder.commentsview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext.getContext(),CommentSection.class);
                intent.putExtra("postid",mkeys.get(position));
                mContext.startActivity(intent);
            }
        });


        FirebaseDatabase.getInstance().getReference("users").child(mUser.getUid()).child("postsLiked").
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>() {};
                        likedposts = dataSnapshot.getValue(t);
                        for (String i : likedposts){
                            if (mkeys.get(position).equals(i)){
                                holder.like.setImageResource(R.drawable.ic_favorite_black_24dp);
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });









        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyItemChanged(position);
                FirebaseDatabase.getInstance().getReference("users").child(mUser.getUid()).child("postsLiked").
                        addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (!dataSnapshot.exists()){
                                    ArrayList<String> likedposts = new ArrayList<>();
                                    likedposts.add(mkeys.get(position));
                                    FirebaseDatabase.getInstance().getReference("users").child(mUser.getUid()).child("postsLiked").setValue(likedposts);
                                    mposts.get(position).setLikes(mposts.get(position).getLikes()+1);
                                    FirebaseDatabase.getInstance().getReference("posts").child(mkeys.get(position)).setValue(mposts.get(position));




                                }
                                else{
                                    GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>() {};
                                    List<String> likedposts = dataSnapshot.getValue(t);
                                    if (!likedposts.contains(mkeys.get(position))){
                                        likedposts.add(mkeys.get(position));
                                        FirebaseDatabase.getInstance().getReference("users").child(mUser.getUid()).child("postsLiked").setValue(likedposts);
                                        mposts.get(position).setLikes(mposts.get(position).getLikes()+1);
                                        FirebaseDatabase.getInstance().getReference("posts").child(mkeys.get(position)).setValue(mposts.get(position));

                                    }

                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
            }
        });

        holder.commenticon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext.getContext(),CommentSection.class);
                intent.putExtra("postid",mkeys.get(position));
                mContext.startActivity(intent);
            }
        });

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext.getContext(),DiscussionPost.class);
                intent.putExtra("clickid",mkeys.get(position));
                intent.putExtra("click",mposts.get(position).getTxt());
                intent.putExtra("clickAuthor",mposts.get(position).getAuthor());
                intent.putExtra("clickcoms",mposts.get(position).getNumComms());
                intent.putExtra("likes",mposts.get(position).getLikes());
                mContext.startActivity(intent);
            }
        });




        holder.sendHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseDatabase.getInstance().getReference().child("posts").child(mkeys.get(position)).child("helpRequestSent")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (!dataSnapshot.hasChildren()){
                                    List<String> helpersdummy = new ArrayList<>();
                                    helpersdummy.add(mUser.getUid());
                                    FirebaseDatabase.getInstance().getReference().child("posts").child(mkeys.get(position)).child("helpRequestSent")
                                            .setValue(helpersdummy);

                                }
                                else{
                                    GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>() {};
                                    List<String> postHelpers = dataSnapshot.getValue(t);
                                    if (!postHelpers.contains(mUser.getUid())){
                                        postHelpers.add(mUser.getUid());
                                        FirebaseDatabase.getInstance().getReference().child("posts").child(mkeys.get(position))
                                                .child("helpRequestSent").setValue(postHelpers);
                                    }
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
            }
        });

        holder.relativeLayout.setAnimation(AnimationUtils.loadAnimation(mContext.getContext(),R.anim.fade_scale_animation));

    }

    @Override
    public int getItemCount() {
        return mposts.size();
    }

    public void filterList(ArrayList<Post> filteredList){
        mposts = filteredList;
        notifyDataSetChanged();

    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView author;
        private TextView content;
        private RelativeLayout relativeLayout;
        private ImageView like;
        private TextView numLikes;
        private TextView commentsview;
        private ImageView commenticon;
        private TextView postLocation;
        private ImageView sendHelp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
             author = itemView.findViewById(R.id.user);
             content = itemView.findViewById(R.id.post_textView);
             relativeLayout = itemView.findViewById(R.id.linealayout);
             like = itemView.findViewById(R.id.like);
             numLikes = itemView.findViewById(R.id.numlike);
             commentsview = itemView.findViewById(R.id.commentsview);
             commenticon = itemView.findViewById(R.id.commentView);
             postLocation = itemView.findViewById(R.id.post_location);
             sendHelp = itemView.findViewById(R.id.send_help);


        }
    }
}
