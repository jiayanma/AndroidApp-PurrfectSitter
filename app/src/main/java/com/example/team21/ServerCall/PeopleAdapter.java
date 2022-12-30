package com.example.team21.ServerCall;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.team21.R;

import java.util.List;

public class PeopleAdapter extends RecyclerView.Adapter<PeopleViewHolder> {
    private List<People> people;
    private Context context;

    public PeopleAdapter(List<People> people, Context context) {
        this.people = people;
        this.context = context;
    }


    @NonNull
    @Override
    public PeopleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PeopleViewHolder(LayoutInflater.from(context).inflate(R.layout.search_result_item_people, null));
    }

    @Override
    public void onBindViewHolder(@NonNull PeopleViewHolder holder, int position) {
        holder.tv_people_name.setText(people.get(position).getName());
        holder.tv_gender.setText(people.get(position).getGender());
        holder.tv_eye_color.setText(people.get(position).getEye_color());
        holder.tv_hair_color.setText(people.get(position).getHair_color());
        holder.image_name.setImageResource(R.drawable.name);
        holder.image_gender.setImageResource(R.drawable.gender);
        holder.image_hair.setImageResource(R.drawable.hair);
        holder.image_eye.setImageResource(R.drawable.eye);
    }

    @Override
    public int getItemCount() {
        return people.size();
    }
}
