package com.example.team21.ServerCall;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.team21.R;

public class PeopleViewHolder extends RecyclerView.ViewHolder {
    public TextView tv_people_name;
    public TextView tv_gender;
    public TextView tv_hair_color;
    public TextView tv_eye_color;
    public ImageView image_name;
    public ImageView image_gender;
    public ImageView image_hair;
    public ImageView image_eye;


    public PeopleViewHolder(@NonNull View itemView) {
        super(itemView);
        this.tv_people_name = itemView.findViewById(R.id.tv_people_name);
        this.tv_gender = itemView.findViewById(R.id.tv_people_gender);
        this.tv_hair_color = itemView.findViewById(R.id.tv_people_hair_color);
        this.tv_eye_color = itemView.findViewById(R.id.tv_people_eye_color);
        this.image_name = itemView.findViewById(R.id.imageViewName);
        this.image_gender = itemView.findViewById(R.id.imageViewGender);
        this.image_hair = itemView.findViewById(R.id.imageViewHair);
        this.image_eye = itemView.findViewById(R.id.imageViewEye);
    }
}
