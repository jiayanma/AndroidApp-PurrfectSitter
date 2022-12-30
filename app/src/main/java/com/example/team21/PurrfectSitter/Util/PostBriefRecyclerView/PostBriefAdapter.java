package com.example.team21.PurrfectSitter.Util.PostBriefRecyclerView;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.team21.PurrfectSitter.FirebaseClass.Config;
import com.example.team21.PurrfectSitter.FirebaseClass.Pet;
import com.example.team21.PurrfectSitter.FirebaseClass.Post;
import com.example.team21.PurrfectSitter.FirebaseClass.User;
import com.example.team21.PurrfectSitter.Util.Helper;
import com.example.team21.PurrfectSitter.Util.ItemClickListenerInterface;
import com.example.team21.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostBriefAdapter extends RecyclerView.Adapter<PostBriefViewHolder> {
    private List<Post> postList;
    private Context context;
    private final Format format;
    private ItemClickListenerInterface clickListener;
    private final int PET_LENGTH_LIMIT = 22;
    private final String TAG = "PostBriefAdapter";

    private String getPetsBrief(List<Pet> petList) {
        HashMap<String, Integer> petCnt = new HashMap<>();
        for (Pet pet : petList) {
            petCnt.put(pet.species, petCnt.getOrDefault(pet.species, 0) + 1);
        }
        String petStr = "";
        for (Map.Entry<String, Integer> pc : petCnt.entrySet()) {
            if (pc.getValue() == 1) {
                petStr += pc.getKey() + ", ";
            } else {
                petStr += pc.getKey() + "s, ";
            }
        }
        petStr = petStr.substring(0, petStr.length() - 2);
        if (petStr.length() > PET_LENGTH_LIMIT) {
            petStr = petStr.substring(0, PET_LENGTH_LIMIT) + "...";
        }
        return petStr;
    }

    public PostBriefAdapter(List<Post> postList, Context context) {
        this.postList = postList;
        this.context = context;
        this.format = new SimpleDateFormat("yyyy/MM/dd");
    }

    @NonNull
    @Override
    public PostBriefViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PostBriefViewHolder(LayoutInflater.from(context).inflate(R.layout.post_brief, null), clickListener);
    }


    @Override
    public void onBindViewHolder(@NonNull PostBriefViewHolder holder, int position) {
        Post post = postList.get(position);
        Date startDate = new Date(post.requestStartTimestamp);
        Date endDate = new Date(post.requestEndTimestamp);
        holder.requestDate.setText(this.format.format(startDate) + " - " + this.format.format(endDate));
        holder.location.setText(post.location.city + ", " + post.location.state);

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference userDataRef = db.getReference(Config.USERS);
        userDataRef.child(post.creatorId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> getTask) {
                if (!getTask.isSuccessful()) {
                    Log.e(TAG, "Error getting data", getTask.getException());
                } else {
                    Log.v(TAG, String.valueOf(getTask.getResult().getValue()));
                    User user = getTask.getResult().getValue(User.class);
                    String petStr = "N/A";
                    if (user != null && user.pets != null) {
                        List<Pet> petList = new ArrayList<>(user.pets.values());
                        petStr = getPetsBrief(petList);
                    }
                    holder.pets.setText(petStr);
                }
            }
        });
        holder.coverPhoto.setImageResource(R.drawable.default_home);
        Helper.loadImageViewFromURL(context, post.images.get(0), holder.coverPhoto);
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public void setOnItemClickListener(ItemClickListenerInterface clickListener) {
        this.clickListener = clickListener;
    }
}

