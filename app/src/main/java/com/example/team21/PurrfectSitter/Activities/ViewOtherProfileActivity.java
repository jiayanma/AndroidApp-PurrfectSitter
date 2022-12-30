package com.example.team21.PurrfectSitter.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.team21.PurrfectSitter.FirebaseClass.Config;
import com.example.team21.PurrfectSitter.FirebaseClass.Review;
import com.example.team21.PurrfectSitter.FirebaseClass.User;
import com.example.team21.PurrfectSitter.Util.Helper;
import com.example.team21.PurrfectSitter.Util.ReviewRecyclerView.ReviewAdaptor;
import com.example.team21.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewOtherProfileActivity extends AppCompatActivity {
    RecyclerView rv_reviews;
    ReviewAdaptor reviewAdapter;
    ArrayList<Review> reviewResList = new ArrayList<>();

    FirebaseDatabase db;
    DatabaseReference reviewRef;
    DatabaseReference userRef;
    TextView otherName;
    ImageView otherAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_other_profile);

        otherName = findViewById(R.id.otherName);
        otherAvatar = findViewById(R.id.otherAvatarIV);

        db = FirebaseDatabase.getInstance();
        reviewRef = db.getReference(Config.REVIEW);
        userRef = db.getReference(Config.USERS);
        String userID = getIntent().getExtras().getString("userId");
        userRef.child(userID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> getTask) {
                if (!getTask.isSuccessful()) {
                    Log.e("reviews", "Error getting data", getTask.getException());
                } else {
                    User user = getTask.getResult().getValue(User.class);
                    String userName = user.displayName;
                    otherName.setText(userName);
                    Helper.loadImageViewFromURL(ViewOtherProfileActivity.this, user.avatar, otherAvatar);
                }
            }
        });

        rv_reviews = findViewById(R.id.otherProfileRV);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ViewOtherProfileActivity.this);
        rv_reviews.setLayoutManager(layoutManager);
        rv_reviews.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        reviewRef.orderByChild("revieweeId").equalTo(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot reviewSnapshot : snapshot.getChildren()) {
                    Review review = reviewSnapshot.getValue(Review.class);
                    reviewResList.add(review);
                }
                reviewAdapter = new ReviewAdaptor(reviewResList, ViewOtherProfileActivity.this);
                rv_reviews.setAdapter(reviewAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}