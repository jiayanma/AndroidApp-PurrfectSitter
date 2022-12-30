package com.example.team21.PurrfectSitter.Util.MyAppliedRecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.team21.PurrfectSitter.Activities.PostDetailActivity;
import com.example.team21.PurrfectSitter.Activities.ViewOtherProfileActivity;
import com.example.team21.PurrfectSitter.FirebaseClass.Apply;
import com.example.team21.PurrfectSitter.FirebaseClass.Config;
import com.example.team21.PurrfectSitter.FirebaseClass.Post;
import com.example.team21.PurrfectSitter.FirebaseClass.Review;
import com.example.team21.PurrfectSitter.FirebaseClass.User;
import com.example.team21.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyAppliedAdapter extends RecyclerView.Adapter<MyAppliedViewHolder> {
    private List<MyApplied> myAppliedList;
    private Context context;
    Format format;
    private DatabaseReference reviewDb;
    private FirebaseAuth mAuth;
    private FirebaseDatabase db;
    private DatabaseReference postsRef;

    private final String curUID;
    private final String TAG = "MyAppliedAdapter";
    private final int TITLE_LIMIT = 20;

    public MyAppliedAdapter(List<MyApplied> myAppliedList, Context context, String curUID) {
        this.myAppliedList = myAppliedList;
        this.context = context;
        this.format = new SimpleDateFormat("yyyy/MM/dd");
        this.curUID = curUID;
    }

    @NonNull
    @Override
    public MyAppliedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyAppliedViewHolder(LayoutInflater.from(context).inflate(R.layout.my_applied, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyAppliedViewHolder holder, int position) {
        MyApplied myApplied = myAppliedList.get(position);
        String title = myApplied.getTitle();
        if (title.length() > TITLE_LIMIT) {
            title = title.substring(0, TITLE_LIMIT) + "...";
        }
        holder.title.setText(title);

        db = FirebaseDatabase.getInstance();
        postsRef = db.getReference(Config.POSTS);
        reviewDb = db.getReference(Config.REVIEW);
        mAuth = FirebaseAuth.getInstance();
        String postId = myApplied.getPostId();
        postsRef.child(postId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.e(TAG, "read post " + myApplied.getPostId() + "failed");
                        } else {
                            Post post = task.getResult().getValue(Post.class);
                            post.postId = postId;
                            Date startDate = new Date(post.requestStartTimestamp);
                            Date endDate = new Date(post.requestEndTimestamp);
                            holder.date.setText(format.format(startDate) + " - " + format.format(endDate));
                            if (post.sitter == null || post.sitter.equals(Config.SITTER_PLACEHOLDER)) {
                                if (post.requestEndTimestamp < System.currentTimeMillis()) {
                                    holder.status.setText("Expired");
                                } else {
                                    holder.status.setText("Pending");
                                }
                            } else {
                                if (post.sitter.equals(curUID)) {
                                    holder.status.setText("Accepted");
                                } else {
                                    holder.status.setText("Declined");
                                }
                            }
                            if (post.sitter.equals(curUID) && System.currentTimeMillis() > post.requestEndTimestamp) {
                                holder.writeReview.setVisibility(View.VISIBLE);
                                holder.writeReview.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (post.reviewedBySitter) {
                                            Toast.makeText(view.getContext(), "Already reviewed", Toast.LENGTH_LONG).show();
                                        } else {
                                            updateReview(view, post);
                                        }
                                    }
                                });
                            } else {
                                holder.writeReview.setVisibility(View.INVISIBLE);
                            }
                            db.getReference(Config.USERS)
                                    .child(post.creatorId)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DataSnapshot> task2) {
                                            if (!task2.isSuccessful()) {
                                                Log.e(TAG, "read user " + post.creatorId + "failed");
                                            } else {
                                                User creator = task2.getResult().getValue(User.class);
                                                holder.host.setText(creator.displayName + "(" + creator.email + ")");
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    public void updateReview(View view, Post post) {
        AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
        alert.setTitle("Please review");

        LinearLayout layout = new LinearLayout(view.getContext());
        layout.setOrientation(LinearLayout.VERTICAL);

        EditText message = new EditText(view.getContext());
        message.setLines(5);
        message.setFilters(new InputFilter[]{new InputFilter.LengthFilter(350)});
        layout.addView(message);

        alert.setView(layout);

        alert.setPositiveButton("Review", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                FirebaseUser user = mAuth.getCurrentUser();;
                String reviewerId = user.getUid();
                Review newReview;

                String revieweeId = post.creatorId;
                newReview = new Review(post.postId, reviewerId, revieweeId, 2, message.getText().toString().trim(), System.currentTimeMillis());

                String keyId = reviewDb.push().getKey();
                reviewDb.child(keyId).setValue(newReview).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        postsRef.child(post.postId).child("reviewedBySitter").setValue(true);
                    }
                });
                Toast.makeText(view.getContext(), "Review Successfully", Toast.LENGTH_LONG).show();
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });
        alert.show();
    }

    @Override
    public int getItemCount() {
        return myAppliedList.size();
    }
}
