package com.example.team21.PurrfectSitter.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.team21.PurrfectSitter.FirebaseClass.Config;
import com.example.team21.PurrfectSitter.FirebaseClass.Post;
import com.example.team21.PurrfectSitter.Util.PostBriefRecyclerView.PostBriefAdapter;
import com.example.team21.PurrfectSitter.Util.ItemClickListenerInterface;
import com.example.team21.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CurrentUserPostsActivity extends AppCompatActivity implements ItemClickListenerInterface {
    RecyclerView rv_posts;
    PostBriefAdapter postAdapter;
    ArrayList<Post> postResList = new ArrayList<>();

    FirebaseDatabase db;
    DatabaseReference postRef;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_user_posts);

        db = FirebaseDatabase.getInstance();
        postRef = db.getReference(Config.POSTS);
        userID = FirebaseAuth.getInstance().getUid();

        rv_posts = findViewById(R.id.rv_cur_user_posts);
        LinearLayoutManager layoutManager = new LinearLayoutManager(CurrentUserPostsActivity.this);
        rv_posts.setLayoutManager(layoutManager);
        rv_posts.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        postRef.orderByChild("creatorId").equalTo(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Post post = postSnapshot.getValue(Post.class);
                    post.postId = postSnapshot.getKey();
                    postResList.add(post);

                }
                postAdapter = new PostBriefAdapter(postResList, CurrentUserPostsActivity.this);
                postAdapter.setOnItemClickListener(CurrentUserPostsActivity.this);
                rv_posts.setAdapter(postAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        Post post = postResList.get(position);
        if (post != null) {
            Intent intent = new Intent(this, PostDetailActivity.class);
            intent.putExtra(MainPostActivity.USER_ID, userID);
            intent.putExtra(MainPostActivity.POST_ID, post.postId);
            startActivity(intent);
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}