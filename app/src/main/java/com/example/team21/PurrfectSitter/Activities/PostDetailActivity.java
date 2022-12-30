package com.example.team21.PurrfectSitter.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.team21.PurrfectSitter.FirebaseClass.Apply;
import com.example.team21.PurrfectSitter.FirebaseClass.Config;
import com.example.team21.PurrfectSitter.FirebaseClass.Pet;
import com.example.team21.PurrfectSitter.FirebaseClass.Post;
import com.example.team21.PurrfectSitter.FirebaseClass.Review;
import com.example.team21.PurrfectSitter.FirebaseClass.User;
import com.example.team21.PurrfectSitter.ImageSliderAdapter;
import com.example.team21.PurrfectSitter.Util.PetRecyclerView.PetItem;
import com.example.team21.PurrfectSitter.Util.PetRecyclerView.PetItemAdapter;
import com.example.team21.PurrfectSitter.Util.Helper;
import com.example.team21.PurrfectSitter.Util.ReceivedApplyRecyclerView.ReceivedApplyViewHolder;
import com.example.team21.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostDetailActivity extends AppCompatActivity {
    private FirebaseDatabase db;
    private Context context;
    private List<PetItem> petsList = new ArrayList<>();
    private String postId;
    Format format;
    private String creatorId;
    private String userId;
    ExtendedFloatingActionButton applyButton;
    SliderView sliderView;
    private List<String> imageList;
    private FirebaseAuth mAuth;
    private DatabaseReference applyDb;
    private DatabaseReference reviewDb;
    private Post postData;
    DatabaseReference postDb;

    private String sitter;
    private long endTime;

    private RecyclerView petView;
    private RecyclerView.Adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        TextView tv_postTitle = findViewById(R.id.textView_title);
        TextView tv_description = findViewById(R.id.textView_description);
        TextView tv_location = findViewById(R.id.textView_location);
        TextView tv_date = findViewById(R.id.textView_date);

        sliderView = findViewById(R.id.imageSlider);
        petView = findViewById(R.id.recyclerView_pet);
        format = new SimpleDateFormat("yyyy/MM/dd");
        db = FirebaseDatabase.getInstance();

        postId = getIntent().getStringExtra(MainPostActivity.POST_ID);
        Log.e("postId", postId);
        userId = getIntent().getStringExtra(MainPostActivity.USER_ID);
        Log.e("userId", userId);
        // userId from main post donot match current
        postDb = db.getReference().child(Config.POSTS);
        postDb.child(postId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> getTask) {
                if (!getTask.isSuccessful()) {
                    Log.e("posts", "Error getting data", getTask.getException());
                } else {
                    Log.e("posts", String.valueOf(getTask.getResult().getValue()));
                    postData = getTask.getResult().getValue(Post.class);
                    tv_postTitle.setText(postData.title);
                    tv_description.setText(postData.description);
                    tv_location.setText(postData.location.streetAddress + "," + postData.location.city + "," + postData.location.state + "," + postData.location.zipcode);
                    tv_date.setText(format.format(postData.requestStartTimestamp) + "-" + format.format(postData.requestEndTimestamp));

                    // image slider
                    imageList = postData.images;
                    ImageSliderAdapter imageSliderAdapter = new ImageSliderAdapter(imageList, PostDetailActivity.this);
                    sliderView.setSliderAdapter(imageSliderAdapter);
                    sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
                    sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                    sliderView.startAutoCycle();

                    // creator information
                    creatorId = postData.creatorId;
                    Log.e("creatorId", creatorId);

                    // post status
                    endTime = postData.requestEndTimestamp;
                    sitter = postData.sitter;


                    TextView tv_creatorName = findViewById(R.id.textView_ownerName);
                    ImageView iv_creatorPhoto = findViewById(R.id.imageView_ownerPhoto);
                    RecyclerView petRecyclerView = findViewById(R.id.recyclerView_pet);

                    DatabaseReference userRef = db.getReference().child(Config.USERS);
                    userRef.child(creatorId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> getTask) {
                            if (!getTask.isSuccessful()) {
                                Log.e("posts", "Error getting data", getTask.getException());
                            } else {
                                Log.e("posts", String.valueOf(getTask.getResult().getValue()));
                                User userData = getTask.getResult().getValue(User.class);
                                tv_creatorName.setText(userData.displayName);
                                String userPhotoUrl = userData.avatar;
                                Helper.loadImageViewFromURL(PostDetailActivity.this, userPhotoUrl, iv_creatorPhoto);

                                if (userData.pets != null) {
                                    HashMap<String, Pet> petData = userData.pets;
                                    for (Map.Entry<String, Pet> set : petData.entrySet()) {
                                        Pet item = set.getValue();
                                        petsList.add(new PetItem(item));
                                    }
                                }
                                Log.e("pets", String.valueOf(petsList));
                                LinearLayoutManager layoutManager = new LinearLayoutManager(PostDetailActivity.this);
                                petView.setLayoutManager(layoutManager);
                                adapter = new PetItemAdapter(petsList, PostDetailActivity.this);
                                petView.setAdapter(adapter);
                            }
                        }
                    });

                    // apply button
                    applyDb = db.getReference(Config.APPLY);
                    applyButton = findViewById(R.id.apply_button);
                    mAuth = FirebaseAuth.getInstance();
                    applyButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (creatorId.equals(userId)){
                                Toast.makeText(PostDetailActivity.this, "You cannot apply for your own post", Toast.LENGTH_LONG).show();
                            }
                            else {
                            AlertDialog.Builder alert = new AlertDialog.Builder(PostDetailActivity.this);
                            alert.setTitle("Please enter your message");

                            LinearLayout layout = new LinearLayout(PostDetailActivity.this);
                            layout.setOrientation(LinearLayout.VERTICAL);

                            final EditText message = new EditText(PostDetailActivity.this);
                            message.setFilters(new InputFilter[]{new InputFilter.LengthFilter(200)});
                            layout.addView(message);

                            alert.setView(layout);
                            alert.setPositiveButton("Apply", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    applyDb.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DataSnapshot> getTask) {
                                            if (!getTask.isSuccessful()) {
                                                Log.e("application", "Error getting data", getTask.getException());
                                            } else {
                                                if (getTask.getResult() != null) {
                                                    Boolean isApplied = false;
                                                    Iterable<DataSnapshot> applyItr = getTask.getResult().getChildren();
                                                    Apply recentApply = null;
                                                    for (DataSnapshot application : applyItr) {
                                                        Apply applyItem = application.getValue(Apply.class);
                                                        if (applyItem.postId.equals(postId) && applyItem.applicantId.equals(userId)) {
                                                            Toast.makeText(PostDetailActivity.this, "You've applied this post", Toast.LENGTH_LONG).show();
                                                            isApplied = true;
                                                            break;
                                                        }
                                                    }
                                                    if (!isApplied) {
                                                        DatabaseReference userRef = db.getReference().child(Config.USERS);
                                                        userRef.child(userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<DataSnapshot> getTask) {
                                                                if (!getTask.isSuccessful()) {
                                                                    Log.e("user", "Error getting data", getTask.getException());
                                                                } else {
                                                                    Log.e("user", String.valueOf(getTask.getResult().getValue()));
                                                                    User userData = getTask.getResult().getValue(User.class);
                                                                    String applicantName = userData.displayName;
                                                                    String email = userData.email;
                                                                    String avatar = userData.avatar;
                                                                    Apply newApply = new Apply(postId, postData.title, creatorId, userId, applicantName, email, message.getText().toString(), "applied", avatar);
                                                                    String keyId = applyDb.push().getKey();
                                                                    applyDb.child(keyId).setValue(newApply);
                                                                    Toast.makeText(PostDetailActivity.this, "Applied Successfully", Toast.LENGTH_LONG).show();
                                                                }
                                                            }
                                                        });
                                                    }
                                                }
                                            }
                                        }
                                    });
                                }
                            });
                            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.cancel();
                                }
                            });
                            alert.show();
                        }
                        }
                    });


                    iv_creatorPhoto.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!creatorId.equals(mAuth.getCurrentUser())) {
                                Intent intent = new Intent(getBaseContext(), ViewOtherProfileActivity.class);
                                intent.putExtra("userId", creatorId);
                                startActivity(intent);
                            }
                        }
                    });
                }
            }
        });
    }

    public void updateReview() {
        AlertDialog.Builder alert = new AlertDialog.Builder(PostDetailActivity.this);
        alert.setTitle("Please review");

        LinearLayout layout = new LinearLayout(PostDetailActivity.this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText message = new EditText(PostDetailActivity.this);
        message.setLines(5);
        message.setFilters(new InputFilter[]{new InputFilter.LengthFilter(350)});
        layout.addView(message);

        alert.setView(layout);
        alert.setPositiveButton("Review", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                FirebaseUser user = mAuth.getCurrentUser();
                String reviewerId = user.getUid();
                Review newReview;

                String revieweeId = postData.sitter;
                newReview = new Review(postId, reviewerId, revieweeId, 1, message.getText().toString().trim(), System.currentTimeMillis());

                String keyId = reviewDb.push().getKey();
                reviewDb.child(keyId).setValue(newReview).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        postDb.child(postId).child("reviewedByOwner").setValue(true);
                    }
                });
                Toast.makeText(PostDetailActivity.this, "Review Successfully", Toast.LENGTH_LONG).show();
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });
        alert.show();
    }
}