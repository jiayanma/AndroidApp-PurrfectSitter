package com.example.team21.PurrfectSitter.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.team21.PurrfectSitter.FirebaseClass.Config;
import com.example.team21.PurrfectSitter.FirebaseClass.Review;
import com.example.team21.PurrfectSitter.Util.ReviewRecyclerView.ReviewAdaptor;
import com.example.team21.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ReviewsActivity extends AppCompatActivity {
    RecyclerView rv_reviews;
    ReviewAdaptor reviewAdapter;
    ArrayList<Review> reviewResList = new ArrayList<>();

    FirebaseDatabase db;
    DatabaseReference reviewRef;
    String userID;
    int pressedButtonNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);
        pressedButtonNumber = getIntent().getExtras().getInt("buttonNumber");

        db = FirebaseDatabase.getInstance();
        reviewRef = db.getReference(Config.REVIEW);
        userID = FirebaseAuth.getInstance().getUid();

        rv_reviews = findViewById(R.id.rv_reviews);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ReviewsActivity.this);
        rv_reviews.setLayoutManager(layoutManager);
        rv_reviews.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));


        reviewRef.orderByChild("revieweeId").equalTo(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot reviewSnapshot : snapshot.getChildren()) {
                    Review review = reviewSnapshot.getValue(Review.class);
                    if (pressedButtonNumber == 1 && review.role == 2) {
                        reviewResList.add(review);
                    } else if (pressedButtonNumber == 2 && review.role == 1) {
                        reviewResList.add(review);
                    }
                }
                reviewAdapter = new ReviewAdaptor(reviewResList, ReviewsActivity.this);
                rv_reviews.setAdapter(reviewAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}