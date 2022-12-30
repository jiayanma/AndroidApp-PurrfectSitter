package com.example.team21.PurrfectSitter.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.team21.MainActivity;
import com.example.team21.PurrfectSitter.FirebaseClass.Config;
import com.example.team21.PurrfectSitter.FirebaseClass.Post;
import com.example.team21.PurrfectSitter.Util.PostBriefRecyclerView.PostBriefAdapter;
import com.example.team21.PurrfectSitter.Util.ItemClickListenerInterface;
import com.example.team21.R;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class PostSearchResultActivity extends AppCompatActivity implements ItemClickListenerInterface {
    private FirebaseDatabase db;
    private PostSearchResultActivity activity;
    private PostBriefAdapter postAdapter;
    private RecyclerView postRecyclerView;
    private ArrayList<Post> postResList = new ArrayList<>();
    private FirebaseUser curUser;

    private final String TAG = "PostSearchResultActivity";

    private boolean isZipcode(String query) {
        return Character.isDigit(query.charAt(0));
    }

    private boolean isCityState(String query) {
        return query.split(",").length == 2;
    }

    private void searchCity(String query, ArrayList<Post> tmpList) {
        String sCity = query.toLowerCase();
        for (Post post : tmpList) {
            if (post.location.city.toLowerCase().equals(sCity)) {
                postResList.add(post);
            }
        }
    }

    private void searchCityState(String query, ArrayList<Post> tmpList) {
        String[] loc = query.split(",");
        String sCity = loc[0].toLowerCase();
        String sState = loc[1].toUpperCase();
        for (Post post : tmpList) {
            if (post.location.city.toLowerCase().equals(sCity) && post.location.state.equals(sState)) {
                postResList.add(post);
            }
        }
    }

    private void searchZipcode(String query, ArrayList<Post> tmpList) {
        int sZipcode = Integer.parseInt(query);
        for (Post post : tmpList) {
            if (Math.abs(Integer.parseInt(post.location.zipcode) - sZipcode) <= 3) {
                postResList.add(post);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseDatabase.getInstance();
        activity = this;

        Intent intent = getIntent();
        String query = intent.getStringExtra(MainPostActivity.SEARCH_INPUT);
        curUser = intent.getParcelableExtra(LogInActivity.USERNAME);

        DatabaseReference postsRef = db.getReference();
        ArrayList<Post> tmpList = new ArrayList<>();

        postsRef.child(Config.POSTS)
                .orderByChild("applied")
                .equalTo(false)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot postSnapshot: snapshot.getChildren()) {
                            String postId = postSnapshot.getKey();
                            Post post = postSnapshot.getValue(Post.class);
                            post.postId = postId;
                            tmpList.add(post);
                        }
                        if (isZipcode(query)) {
                            searchZipcode(query, tmpList);
                        } else if (isCityState(query)) {
                            searchCityState(query, tmpList);
                        } else {
                            searchCity(query, tmpList);
                        }

                        if (postResList.isEmpty()) {
                            setContentView(R.layout.activity_nil_search_result);
                            return;
                        }

                        setContentView(R.layout.activity_post_search_result);
                        postRecyclerView = findViewById(R.id.postRV);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(PostSearchResultActivity.this );
                        postRecyclerView.setLayoutManager(layoutManager);
                        postRecyclerView.addItemDecoration(new DividerItemDecoration(activity, DividerItemDecoration.VERTICAL));

                        Collections.sort(postResList);
                        postAdapter = new PostBriefAdapter(postResList, PostSearchResultActivity.this);
                        postAdapter.setOnItemClickListener(activity);
                        postRecyclerView.setAdapter(postAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e(TAG, "searchView.setOnQueryTextListener cancelled");
                    }
                });
    }

    @Override
    public void onItemClick(View view, int position) {
        Post post = postResList.get(position);
        if (post != null) {
            Intent intent = new Intent(this, PostDetailActivity.class);
            intent.putExtra(MainPostActivity.USER_ID, curUser.getUid());
            intent.putExtra(MainPostActivity.POST_ID, post.postId);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainPostActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
