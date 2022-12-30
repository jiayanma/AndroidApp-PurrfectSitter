package com.example.team21.PurrfectSitter.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.team21.PurrfectSitter.FirebaseClass.Apply;
import com.example.team21.PurrfectSitter.FirebaseClass.Config;
import com.example.team21.PurrfectSitter.Util.ReceivedApplyRecyclerView.ReceivedApply;
import com.example.team21.PurrfectSitter.Util.ReceivedApplyRecyclerView.ReceivedApplyAdapter;
import com.example.team21.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;

public class ReceivedApplyActivity extends AppCompatActivity {
    private String userId;
    private FirebaseDatabase db;
    private ArrayList<ReceivedApply> receivedApply;
    private RecyclerView receivedRecyclerView;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_received_apply);

        receivedApply = new ArrayList<ReceivedApply>();
        receivedRecyclerView = findViewById(R.id.rv_receivedApply);
        receivedRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        db = FirebaseDatabase.getInstance();
        userId = getIntent().getStringExtra("userId");
        Log.e("userId", userId);
//        userId = "-NHMmHIgLHzKxsNTX-km";

        DatabaseReference applyDataRef = db.getReference(Config.APPLY);
        applyDataRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> getTask) {
                if (!getTask.isSuccessful()) {
                    Log.e("received", "Error getting data", getTask.getException());
                } else {
                    Log.e("received", String.valueOf(getTask.getResult().getValue()));
                    if (getTask.getResult().getChildren() != null) {
                        Iterable<DataSnapshot> applyItr = getTask.getResult().getChildren();
                        for (DataSnapshot application : applyItr) {
                            Apply applyItem = application.getValue(Apply.class);
                            if (applyItem.applyCreatorId.equals(userId)) {
                                receivedApply.add(new ReceivedApply(applyItem));
                            }
                        }
                    }
                    // application sorting has not tested
                    Collections.sort(receivedApply);
                    Log.e("applylist", String.valueOf(receivedApply));
                    LinearLayoutManager layoutManager = new LinearLayoutManager(ReceivedApplyActivity.this);
                    receivedRecyclerView.setLayoutManager(layoutManager);
                    adapter = new ReceivedApplyAdapter(receivedApply, ReceivedApplyActivity.this, userId);
                    receivedRecyclerView.setAdapter(adapter);
                }
            }
        });
    }
}