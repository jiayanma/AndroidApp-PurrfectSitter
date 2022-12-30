package com.example.team21.PurrfectSitter.Util.ReviewRecyclerView;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.team21.PurrfectSitter.FirebaseClass.Config;
import com.example.team21.PurrfectSitter.FirebaseClass.Review;
import com.example.team21.PurrfectSitter.FirebaseClass.User;
import com.example.team21.PurrfectSitter.Util.Helper;
import com.example.team21.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ReviewAdaptor extends RecyclerView.Adapter<ReviewViewHolder> {
    private List<Review> reviewList;
    private Context context;
    private final Format format;
    private final String TAG = "ReviewAdapter";
    private User user;

    public ReviewAdaptor(List<Review> reviewList, Context context) {
        this.reviewList = reviewList;
        this.context = context;
        this.format = new SimpleDateFormat("yyyy/MM/dd");
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ReviewViewHolder(LayoutInflater.from(context).inflate(R.layout.review_item, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = reviewList.get(position);
        Date date = new Date(review.timeStamp);
        holder.date.setText("Reviewed Date " + this.format.format(date));
        holder.reviewContent.setText(review.reviewText);


        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference userDataRef = db.getReference(Config.USERS);

        userDataRef.child(review.reviewerId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> getTask) {
                if (!getTask.isSuccessful()) {
                    Log.e("reviews", "Error getting data", getTask.getException());
                } else {
                    user = getTask.getResult().getValue(User.class);
                    String userName = user.displayName;
                    holder.reviewerName.setText(userName);
                    holder.reviewerPhoto.setImageResource(R.drawable.default_home);
                    Helper.loadImageViewFromURL(context, user.avatar, holder.reviewerPhoto);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }
}
