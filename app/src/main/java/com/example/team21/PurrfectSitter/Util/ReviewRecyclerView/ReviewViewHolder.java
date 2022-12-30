package com.example.team21.PurrfectSitter.Util.ReviewRecyclerView;

import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.team21.R;

public class ReviewViewHolder extends RecyclerView.ViewHolder{
    public TextView date;
    public TextView reviewContent;
    public TextView reviewerName;
    public ImageView reviewerPhoto;

    public ReviewViewHolder(@NonNull View itemView) {
        super(itemView);
        this.date = itemView.findViewById(R.id.dateTV);
        this.reviewContent = itemView.findViewById(R.id.reviewContentTV);
        this.reviewerName = itemView.findViewById(R.id.reviewerName);
        this.reviewerPhoto = itemView.findViewById(R.id.reviewerPhotoIV);
        this.reviewContent.setMovementMethod(new ScrollingMovementMethod());
    }
}
