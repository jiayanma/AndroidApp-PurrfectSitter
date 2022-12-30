package com.example.team21.Sticker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.team21.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class SendHistoryActivity extends AppCompatActivity {
    private FirebaseDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_history);

        TextView tv_booksCount = findViewById(R.id.tv_booksCount);
        TextView tv_loveCount = findViewById(R.id.tv_loveCount);
        TextView tv_madCount = findViewById(R.id.tv_madCount);
        TextView tv_musicCount = findViewById(R.id.tv_musicCount);
        TextView tv_pikachuCount = findViewById(R.id.tv_pikachuCount);
        TextView tv_pirateCount = findViewById(R.id.tv_pirateCount);
        TextView tv_proudCount = findViewById(R.id.tv_proudCount);
        TextView tv_smileCount = findViewById(R.id.tv_smileCount);
        TextView tv_sunglassesCount = findViewById(R.id.tv_sunglassesCount);
        TextView tv_tongueCount = findViewById(R.id.tv_tongueCount);
        TextView tv_treeCount = findViewById(R.id.tv_treeCount);

        db = FirebaseDatabase.getInstance();
        String username = UserLoginActivity.getUsername();
        DatabaseReference ref = db.getReference().child("userData").child(username);
        ref.addValueEventListener (new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserData userData = dataSnapshot.getValue(UserData.class);
                List<StickerCounter> stickerCounterList = userData.sent;

                tv_booksCount.setText(String.valueOf(stickerCounterList.get(0).cnt));
                tv_loveCount.setText(String.valueOf(stickerCounterList.get(1).cnt));
                tv_madCount.setText(String.valueOf(stickerCounterList.get(2).cnt));
                tv_musicCount.setText(String.valueOf(stickerCounterList.get(3).cnt));
                tv_pikachuCount.setText(String.valueOf(stickerCounterList.get(4).cnt));
                tv_pirateCount.setText(String.valueOf(stickerCounterList.get(5).cnt));
                tv_proudCount.setText(String.valueOf(stickerCounterList.get(6).cnt));
                tv_smileCount.setText(String.valueOf(stickerCounterList.get(7).cnt));
                tv_sunglassesCount.setText(String.valueOf(stickerCounterList.get(8).cnt));
                tv_tongueCount.setText(String.valueOf(stickerCounterList.get(9).cnt));
                tv_treeCount.setText(String.valueOf(stickerCounterList.get(10).cnt));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("SendHistoryActivity", String.valueOf(error));
            }
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                UserData user = dataSnapshot.getValue(UserData.class);
//                List<StickerCounter> stickerCounterList = user.sent;
//                tv_booksCount.setText(stickerCounterList.get(0).cnt);
//                tv_loveCount.setText(stickerCounterList.get(1).cnt);
//                tv_madCount.setText(stickerCounterList.get(2).cnt);
//                tv_musicCount.setText(stickerCounterList.get(3).cnt);
//                tv_pikachuCount.setText(stickerCounterList.get(4).cnt);
//                tv_pirateCount.setText(stickerCounterList.get(5).cnt);
//                tv_proudCount.setText(stickerCounterList.get(6).cnt);
//                tv_smileCount.setText(stickerCounterList.get(7).cnt);
//                tv_sunglassesCount.setText(stickerCounterList.get(8).cnt);
//                tv_tongueCount.setText(stickerCounterList.get(9).cnt);
//                tv_treeCount.setText(stickerCounterList.get(1).cnt);
//            }
        });

    }
}