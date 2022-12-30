package com.example.team21.PurrfectSitter.Util.ReceivedApplyRecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.team21.PurrfectSitter.Activities.ViewOtherProfileActivity;
import com.example.team21.PurrfectSitter.FirebaseClass.Apply;
import com.example.team21.PurrfectSitter.FirebaseClass.Config;
import com.example.team21.PurrfectSitter.FirebaseClass.Post;
import com.example.team21.PurrfectSitter.FirebaseClass.Review;
import com.example.team21.PurrfectSitter.Util.Helper;
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
import java.util.Map;

public class ReceivedApplyAdapter extends RecyclerView.Adapter<ReceivedApplyViewHolder> {
    private ArrayList<ReceivedApply> received_Apply;
    private Context context;
    Format format;
    ArrayList<ReceivedApplyViewHolder> views = new ArrayList<>();
    private DatabaseReference reviewDb;
    private FirebaseAuth mAuth;
    private FirebaseDatabase db;
    private DatabaseReference postsRef;
    private final String curUID;
    private final String TAG = "ReceiveApplyAdapter";

    public ReceivedApplyAdapter(ArrayList<ReceivedApply> received_Apply, Context context, String curUID) {
        this.received_Apply = received_Apply;
        this.context = context;
        this.format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.curUID = curUID;
    }

    @NonNull
    @Override
    public ReceivedApplyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ReceivedApplyViewHolder(LayoutInflater.from(context).inflate(R.layout.received_apply, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ReceivedApplyViewHolder holder, int position) {
        holder.post_title.setText(received_Apply.get(position).getPost_title());
        Date date = new Date(received_Apply.get(position).getTime());
        holder.time.setText(this.format.format(date));
        holder.applicant_name.setText(received_Apply.get(position).getApplicant_name());
        holder.applicant_email.setText(received_Apply.get(position).getApplicant_email());
        holder.message.setText(received_Apply.get(position).getMessage());
        holder.status.setText(received_Apply.get(position).getStatus());
        String photoUrl = received_Apply.get(position).getAvatar();
        Helper.loadImageViewFromURL(context, photoUrl, holder.photo);
        String applicantId = received_Apply.get(position).getApplicantId();
        String postId = received_Apply.get(position).getPostId();
        views.add(holder);

        db = FirebaseDatabase.getInstance();
        postsRef = db.getReference(Config.POSTS);
        reviewDb = db.getReference(Config.REVIEW);
        mAuth = FirebaseAuth.getInstance();
        ReceivedApply currentApplication = received_Apply.get(position);
        if (currentApplication.getStatus().equals("applied")) {
            postsRef.child(postId)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (!task.isSuccessful()) {
                                Log.e(TAG, "read post failed");
                            } else {
                                Post currPost = task.getResult().getValue(Post.class);
                                if (currPost.requestEndTimestamp < System.currentTimeMillis()) {
                                    holder.status.setText("expired");
                                    holder.accept.setVisibility(Button.INVISIBLE);
                                } else {
                                    holder.accept.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            DatabaseReference applyRef = db.getReference(Config.APPLY);
                                            
                                            applyRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DataSnapshot> getTask) {
                                                    if (!getTask.isSuccessful()) {
                                                        Log.e("application", "Error getting data", getTask.getException());
                                                    } else {
                                                        if (getTask.getResult() != null) {
                                                            Iterable<DataSnapshot> applyItr = getTask.getResult().getChildren();
                                                            for (DataSnapshot application : applyItr) {
                                                                Apply applyItem = application.getValue(Apply.class);
                                                                if (postId.equals(applyItem.postId)) {
                                                                    if (applicantId.equals(applyItem.applicantId)) {
                                                                        applyItem.status = "accepted";
                                                                    } else{
                                                                        applyItem.status = "rejected";
                                                                    }
                                                                    applyRef.child(application.getKey()).setValue(applyItem);
                                                                }
                                                            }
                                                        }

                                                    }
                                                    for (int i = 0; i < views.size(); i++) {
                                                        ReceivedApplyViewHolder h = views.get(i);
                                                        if (received_Apply.get(i).getPostId().equals(postId)) {
                                                            if (received_Apply.get(i).getApplicantId().equals(applicantId)) {
                                                                h.status.setText("accepted");
                                                            } else {
                                                                h.status.setText("rejected");
                                                            }
                                                            h.accept.setVisibility(Button.INVISIBLE);
                                                        }
                                                    }
                                                }
                                            });

                                            DatabaseReference postRef = postsRef.child(postId);
                                            currPost.sitter = applicantId;
                                            currPost.applied = true;
                                            postRef.setValue(currPost);
                                        }
                                    });
                                }
                            }
                        }
                    });
        } else {
            holder.accept.setVisibility(Button.INVISIBLE);
        }

        postsRef.child(postId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                   @Override
                   public void onComplete(@NonNull Task<DataSnapshot> task) {
                       if (!task.isSuccessful()) {
                           Log.e(TAG, "read post failed");
                       } else {
                           Post currPost = task.getResult().getValue(Post.class);
                           currPost.postId = postId;
                           if (currentApplication.getStatus().equals("accepted") && System.currentTimeMillis() > currPost.requestEndTimestamp) {
                               holder.review.setVisibility(View.VISIBLE);
                               holder.review.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View view) {
                                       if (currPost.reviewedByOwner) {
                                           Toast.makeText(view.getContext(), "Already reviewed", Toast.LENGTH_LONG).show();
                                       } else {
                                           updateReview(view, currPost);
                                       }
                                   }
                               });
                           } else {
                               holder.review.setVisibility(View.INVISIBLE);
                           }
                       }
                   }
               });

        holder.photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ViewOtherProfileActivity.class);
                intent.putExtra("userId", applicantId);
                context.startActivity(intent);
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

                String revieweeId = post.sitter;
                newReview = new Review(post.postId, reviewerId, revieweeId, 1, message.getText().toString().trim(), System.currentTimeMillis());

                String keyId = reviewDb.push().getKey();
                reviewDb.child(keyId).setValue(newReview).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        postsRef.child(post.postId).child("reviewedByOwner").setValue(true);
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
        return received_Apply.size();
    }
}
