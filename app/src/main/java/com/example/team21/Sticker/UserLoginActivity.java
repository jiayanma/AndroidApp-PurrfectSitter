package com.example.team21.Sticker;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.team21.MainActivity;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.team21.R;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import java.util.Arrays;
import java.util.List;

public class UserLoginActivity extends AppCompatActivity {
    private FirebaseDatabase db;
    private EditText usernameET;
    private SignInButton signinButton;
    private GoogleSignInClient gsClient;
    private FirebaseAuth fbAuth;
    private static final int SIGN_IN_CODE = 1001;

    private static String username;

    private void onLoginSuccess(String username) {
        Log.v("UserLoginActivity", username + " login");
        Toast toast = Toast.makeText(getApplicationContext(), "Login success", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        Intent intent = new Intent(getBaseContext(), UserProfileActivity.class);
        intent.putExtra(FireBaseKeys.username, username);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        createNotificationChannel();

        usernameET = findViewById(R.id.usernameET);
        db = FirebaseDatabase.getInstance();
    }

    public void onUserLoginClick(View view) {
        username = usernameET.getText().toString();
        if (username.isEmpty()) {
            Toast toast = Toast.makeText(getApplicationContext(), "Please enter a valid username", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return;
        }
        DatabaseReference userListRef = db.getReference(FireBaseKeys.userList);

        userListRef.child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> getTask) {
                if (!getTask.isSuccessful()) {
                    Log.e("UserLoginActivity", "Error getting data", getTask.getException());
                }
                else {
                    if (String.valueOf(getTask.getResult().getValue()).equals("null")) {
//                        DatabaseReference dbRef = db.getReference();
//                        dbRef.runTransaction(new Transaction.Handler() {
//                            @NonNull
//                            @Override
//                            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
//                                currentData.child(FireBaseKeys.userList).child(username).setValue(true);
//                                currentData.child(FireBaseKeys.userData).child(username).setValue(new UserData());
//
//                                return Transaction.success(currentData);
//                            }
//
//                            @Override
//                            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
//                                if (committed) {
//                                    Log.v("UserLoginActivity", "new user added " + username);
//                                    onLoginSuccess(username);
//                                } else {
//                                    Log.e("UserLoginActivity", username + " login failed");
//                                    Log.e("UserLoginActivity", error.toString());
//                                    Toast toast = Toast.makeText(getApplicationContext(), "Login failed due to database error", Toast.LENGTH_SHORT);
//                                    toast.setGravity(Gravity.CENTER, 0, 0);
//                                    toast.show();
//                                }
//                            }
//                        });
                        Task<Void> registerTask1 = userListRef.child(username).setValue(true);
                        registerTask1.addOnCompleteListener(task1 -> {
                            if (!registerTask1.isSuccessful()) {
                                Log.v("UserLoginActivity", username + " login failed");
                                Toast toast = Toast.makeText(getApplicationContext(), "Login failed due to database error", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            } else {
                                DatabaseReference userDataRef = db.getReference(FireBaseKeys.userData);
                                Task<Void> registerTask2 = userDataRef.child(username).setValue(new UserData());
                                registerTask2.addOnCompleteListener(task2 -> {
                                    if (!registerTask2.isSuccessful()) {
                                        Log.v("UserLoginActivity", username + " login failed");
                                        Toast toast = Toast.makeText(getApplicationContext(), "Login failed due to database error", Toast.LENGTH_SHORT);
                                        toast.setGravity(Gravity.CENTER, 0, 0);
                                        toast.show();
                                    } else {
                                        onLoginSuccess(username);
                                    }
                                });
                            }
                        });
                    } else {
                        onLoginSuccess(username);
                    }
                }
            }
        });
    }

    // default create notification channel function from https://developer.android.com/reference/android/app/NotificationManager
    public void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(getString(R.string.channel_id), name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public static String getUsername() {
        return username;
    }
}
