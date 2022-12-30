package com.example.team21.Sticker;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.team21.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserProfileActivity extends AppCompatActivity {
    private FirebaseDatabase db;
    private String username;
    private TextView usernameTV;
    private FloatingActionButton fab;
    private static int notification_id;
    private static long lastNotificationTime;

    private void sendNotification(Message msg) {
        Intent intent = new Intent(this, ReceiveHistoryActivity.class);
        intent.putExtra(FireBaseKeys.username, username);
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

        Context context = getBaseContext();
        int resourceId = R.drawable.question;
        if (FireBaseKeys.stickerNames.contains(msg.stickerName)) {
            resourceId = context.getResources().getIdentifier("sticker_" + msg.stickerName, "drawable", context.getPackageName());
        }
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resourceId);
        String channelId = getString(R.string.channel_id);
        NotificationCompat.Builder notifyBuild = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_launcher_foreground_21)
                .setContentTitle("You have a new sticker message!")
                .setContentText("From " + msg.senderUsername)
                .setLargeIcon(bitmap)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(notification_id, notifyBuild.build());
        notification_id++;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        lastNotificationTime = System.currentTimeMillis();

        db = FirebaseDatabase.getInstance();

        Intent intent = getIntent();
        username = intent.getStringExtra(FireBaseKeys.username);
        usernameTV = findViewById(R.id.usernameTV);
        usernameTV.setText((CharSequence) username);

        Button button1 = findViewById(R.id.sendCounterBT);

        button1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                onClickSendHistoryListener();
            }
        });

        fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent(UserProfileActivity.this, SendActivity.class);
                startActivity(sendIntent);
            }
        });

        DatabaseReference userDataRef = db.getReference(FireBaseKeys.userData);
        userDataRef.child(username).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserData userData = snapshot.getValue(UserData.class);
                List<Message> receivedList = new ArrayList<>(userData.received.values());
                Collections.sort(receivedList);
                if (receivedList.get(0).timestamp > lastNotificationTime) {
                    sendNotification(receivedList.get(0));
                    lastNotificationTime = receivedList.get(0).timestamp;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.v("UserProfileActivity", "db call cancelled");
            }
        });
    }

    public void onClickReceiveHistoryListener(View view) {
        Intent intent = new Intent(this, ReceiveHistoryActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    public void onClickSendHistoryListener() {
        Intent intent = new Intent(this, SendHistoryActivity.class);
        startActivity((intent));
    }
}
