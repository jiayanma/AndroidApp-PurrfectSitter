package com.example.team21.PurrfectSitter.Util.EditPetRecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.team21.PurrfectSitter.FirebaseClass.Config;
import com.example.team21.PurrfectSitter.Util.Helper;
import com.example.team21.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class EditPetItemAdapter extends RecyclerView.Adapter<EditPetItemViewHolder> {
    private ArrayList<EditPetItem> petItemList;
    private Context context;

    private FirebaseDatabase db;
    private DatabaseReference userRef;

    public EditPetItemAdapter(ArrayList<EditPetItem> petItemList, Context context) {
        this.petItemList = petItemList;
        this.context = context;
    }

    @NonNull
    @Override
    public EditPetItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EditPetItemViewHolder(LayoutInflater.from(context).inflate(R.layout.item_edit_pet, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull EditPetItemViewHolder holder, int position) {
        holder.name.setText(petItemList.get(position).getName());
        holder.species.setText(petItemList.get(position).getSpecies());
        holder.age.setText(String.valueOf(petItemList.get(position).getAge()));
        Helper.loadImageViewFromURL(context, petItemList.get(position).getAvatar(), holder.avatar);
        holder.delete.setImageResource(R.drawable.ic_trash_can_100);

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase db = FirebaseDatabase.getInstance();
                DatabaseReference userRef = db.getReference(Config.USERS);
                String userId = FirebaseAuth.getInstance().getUid();

                final int pos = holder.getAdapterPosition();
                String key = petItemList.get(pos).getKey();
                userRef.child(userId).child("pets").child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "DELETED", Toast.LENGTH_LONG).show();
                            petItemList.remove(pos);
                            notifyItemRemoved(pos);
                        } else {
                            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return petItemList.size();
    }
}
