package com.example.team21.Sticker;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.team21.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ReceiveHistoryActivity extends AppCompatActivity {
    private String curUsername;
    private FirebaseDatabase db;
    private ArrayList<ReceivedSticker> receivedStickers;
    private RecyclerView receivedRecyclerView;
    private RecyclerView.Adapter adapter;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_history);

        receivedStickers = new ArrayList<ReceivedSticker>();
        receivedRecyclerView = findViewById(R.id.rv_received);
        db = FirebaseDatabase.getInstance();
        curUsername = getIntent().getStringExtra("username");
        Log.e("user", curUsername);

        DatabaseReference userDataRef = db.getReference(FireBaseKeys.userData);
        userDataRef.child(curUsername).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> getTask) {
                if (!getTask.isSuccessful()) {
                    Log.e("ReceiveHistoryActivity", "Error getting data", getTask.getException());
                } else {
                    Log.e("received", String.valueOf(getTask.getResult().getValue()));
                    UserData userData = getTask.getResult().getValue(UserData.class);
                    List<Message> receivedList = new ArrayList<>(userData.received.values());
                    Collections.sort(receivedList);
                    for (int i = 0; i < receivedList.size(); i++) {
                        if (receivedList.get(i).timestamp > 0) {
                            receivedStickers.add(new ReceivedSticker(receivedList.get(i)));
                            if (receivedList.get(i).version > FireBaseKeys.VERSION && !FireBaseKeys.stickerNames.contains(receivedList.get(i).stickerName)) {
                                Toast.makeText(getApplicationContext(), "Update app to version " + receivedList.get(i).version + " for full message", Snackbar.LENGTH_LONG)
                                        .show();
                            }
                        }
                    }

                    LinearLayoutManager layoutManager = new LinearLayoutManager(ReceiveHistoryActivity.this );
                    receivedRecyclerView.setLayoutManager(layoutManager);
                    adapter = new ReceivedStickerAdapter(receivedStickers, ReceiveHistoryActivity.this);
                    receivedRecyclerView.setAdapter(adapter);
                }
            }
        });
    }
}
