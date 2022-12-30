package com.example.team21.Sticker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.team21.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SendActivity extends AppCompatActivity {
    FirebaseDatabase db;
    TextView selectUser;
    TextView selectSticker;
    ArrayList<String> userArrayList = new ArrayList<>();
    Dialog userDialog;
    Dialog stickerDialog;
    ImageView selectedSticker;
    String stickerIdx;
    String stkName;
    int stkId;
    String receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        selectUser = findViewById(R.id.tv_select_user);
        selectSticker = findViewById(R.id.tv_select_sticker);
        selectedSticker = (ImageView)findViewById(R.id.iv_selected_sticker);

        db = FirebaseDatabase.getInstance();

        buildSelectUserDialog();

        buildSelectStickerDialog();
    }

    private void getUserList() {
        DatabaseReference userListRef = db.getReference(FireBaseKeys.userList);
        userListRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> userListItr = snapshot.getChildren();
                for (DataSnapshot user : userListItr) {
                    userArrayList.add(user.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void buildSelectUserDialog() {
        getUserList();
        selectUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userDialog = new Dialog(SendActivity.this);
                userDialog.setContentView(R.layout.dialog_select_user);
                userDialog.getWindow().setLayout(650, 800);
                userDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                userDialog.show();

                EditText search = userDialog.findViewById(R.id.et_search_user);
                ListView users = userDialog.findViewById(R.id.lv_user_list);

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(SendActivity.this,
                        android.R.layout.simple_list_item_1, userArrayList);

                users.setAdapter(arrayAdapter);

                search.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        arrayAdapter.getFilter().filter(charSequence);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

                users.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        selectUser.setText(arrayAdapter.getItem(i));
                        receiver = arrayAdapter.getItem(i);
                        userDialog.dismiss();
                    }
                });
            }
        });
    }

    private void buildSelectStickerDialog() {
        selectSticker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stickerDialog = new Dialog(SendActivity.this);
                stickerDialog.setContentView(R.layout.dialog_select_sticker);
                stickerDialog.getWindow().setLayout(650, 800);
                stickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                stickerDialog.show();

                GridView gridView = stickerDialog.findViewById(R.id.gv_stickers);
                ImageAdapter adaptor = new ImageAdapter(SendActivity.this);
                gridView.setAdapter(adaptor);

                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        selectSticker.setText(FireBaseKeys.stickerNames.get(i));
                        selectedSticker.setImageResource(adaptor.imageArray[i]);
                        stkId = adaptor.imageArray[i];
                        stickerIdx = String.valueOf(i);
                        stkName = FireBaseKeys.stickerNames.get(i);
                        stickerDialog.dismiss();
                    }
                });
            }
        });
    }

    public void onSendClick(View v) {
        if (selectUser.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Please select a user!",
                    Toast.LENGTH_LONG).show();
            return;
        }

        if (selectSticker.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Please select a sticker!",
                    Toast.LENGTH_LONG).show();
            return;
        }

        String username = UserLoginActivity.getUsername();

        DatabaseReference userDataRef = db.getReference(FireBaseKeys.userData);

        userDataRef.child(username).child("sent").child(stickerIdx).runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                StickerCounter counter = currentData.getValue(StickerCounter.class);
                if (counter == null) {
                    return Transaction.success(currentData);
                }

                counter.cnt ++;

                currentData.setValue(counter);

                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
                if (committed) {
                    Toast.makeText(SendActivity.this, "Sent.",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(SendActivity.this, "Something went wrong",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        Message msg = new Message(username, System.currentTimeMillis(), stkName);
        userDataRef.child(receiver).child("received").push().setValue(msg);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putCharSequence("user", selectUser.getText());
        outState.putCharSequence("sticker", selectSticker.getText());
        outState.putInt("iv_sticker", stkId);
        outState.putString("stickerIdx", stickerIdx);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        selectUser.setText(savedInstanceState.getCharSequence("user"));
        selectSticker.setText(savedInstanceState.getCharSequence("sticker"));
        selectedSticker.setImageResource(savedInstanceState.getInt("iv_sticker"));
        stickerIdx = savedInstanceState.getString("stickerIdx");
        stkId = savedInstanceState.getInt("iv_sticker");
        stkName = FireBaseKeys.stickerNames.get(Integer.valueOf(stickerIdx));
        receiver = (String) savedInstanceState.getCharSequence("user");
    }


}