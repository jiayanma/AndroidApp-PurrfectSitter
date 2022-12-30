package com.example.team21.PurrfectSitter.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.team21.PurrfectSitter.FirebaseClass.Apply;
import com.example.team21.PurrfectSitter.FirebaseClass.Config;
import com.example.team21.PurrfectSitter.FirebaseClass.Post;
import com.example.team21.PurrfectSitter.Util.MyAppliedRecyclerView.MyApplied;
import com.example.team21.PurrfectSitter.Util.MyAppliedRecyclerView.MyAppliedAdapter;
import com.example.team21.PurrfectSitter.Util.PostBriefRecyclerView.PostBriefAdapter;
import com.example.team21.PurrfectSitter.Util.ItemClickListenerInterface;
import com.example.team21.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class CurrentUserAppliedActivity extends AppCompatActivity {
    RecyclerView appliedRecyclerView;
    MyAppliedAdapter myAppliedAdapter;
    ArrayList<MyApplied> appliedList = new ArrayList<>();

    FirebaseDatabase db;
    DatabaseReference applyRef;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_user_applied);

        db = FirebaseDatabase.getInstance();
        applyRef = db.getReference(Config.APPLY);
        userID = FirebaseAuth.getInstance().getUid();

        appliedRecyclerView = findViewById(R.id.rv_cur_user_applied);
        LinearLayoutManager layoutManager = new LinearLayoutManager(CurrentUserAppliedActivity.this);
        appliedRecyclerView.setLayoutManager(layoutManager);
        appliedRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        applyRef.orderByChild("applicantId")
                .equalTo(userID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot applySnapshot : snapshot.getChildren()) {
                            Apply apply = applySnapshot.getValue(Apply.class);
                            appliedList.add(new MyApplied(apply));
                        }
                        Collections.sort(appliedList);
                        myAppliedAdapter = new MyAppliedAdapter(appliedList, CurrentUserAppliedActivity.this, userID);
                        appliedRecyclerView.setAdapter(myAppliedAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

//    @Override
//    public void onPointerCaptureChanged(boolean hasCapture) {
//        super.onPointerCaptureChanged(hasCapture);
//    }
}