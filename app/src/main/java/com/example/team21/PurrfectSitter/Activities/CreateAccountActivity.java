package com.example.team21.PurrfectSitter.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.team21.PurrfectSitter.FirebaseClass.Config;
import com.example.team21.PurrfectSitter.FirebaseClass.User;
import com.example.team21.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class CreateAccountActivity extends AppCompatActivity {
    private EditText et_password;
    private EditText et_email;
    private EditText et_confirm_password;
    private Button btn_create;
    private FirebaseDatabase db;
    private FirebaseAuth mAuth;
    private String email;
    private String password;
    private String confirmPassword;
    private DatabaseReference userRef;
    private static final String TAG = "CreateAccountActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        et_password = findViewById(R.id.et_title);
        et_confirm_password = findViewById(R.id.et_password2);
        et_email = findViewById((R.id.et_email));

        db = FirebaseDatabase.getInstance();
        userRef = db.getReference(Config.USERS);
        mAuth = FirebaseAuth.getInstance();

        btn_create = findViewById(R.id.bt_create_account);
        btn_create.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                email = et_email.getText().toString().trim();
                password = et_password.getText().toString();
                confirmPassword = et_confirm_password.getText().toString();
                if (password.length() < 6) {
                    Toast.makeText(CreateAccountActivity.this,
                            "Password must be at least 6 characters",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                if (confirmPassword() && isValidEmail((CharSequence)email)
                    && checkNotEmpty()) {
                    createAccount(email, password);
                }
            }
        });
    }

    public boolean isValidEmail(CharSequence email) {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Enter a valid email", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean confirmPassword() {
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Password not matching", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public boolean checkNotEmpty() {
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Enter email and password",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void createAccount(String email, String password) {
        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            Toast.makeText(CreateAccountActivity.this, "Account Created Successfully",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            // create a new User with default info in Firebase
                            String defaultAvatar = "https://firebasestorage.googleapis.com/v0/b/numad-team21.appspot.com/o/933-9332131_profile-picture-default-png.png?alt=media&token=fc445c9f-f2aa-499d-8669-2b7baccbe12b";
                            User userProf = new User(user.getUid(), "Display Name", defaultAvatar, new HashMap<>(), email);
                            userRef.child(user.getUid()).setValue(userProf);
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            try {
                                throw task.getException();
                            }
                            // if user enters wrong email.
                            catch (FirebaseAuthWeakPasswordException weakPassword)
                            {
                                Log.d(TAG, "onComplete: weak_password");

                                Toast.makeText(CreateAccountActivity.this, "Weak password",
                                        Toast.LENGTH_LONG).show();
                            }
                            catch (FirebaseAuthUserCollisionException existEmail)
                            {
                                Log.d(TAG, "onComplete: exist_email");

                                Toast.makeText(CreateAccountActivity.this, "Account already exists",
                                        Toast.LENGTH_LONG).show();
                            }
                            catch (Exception e)
                            {
                                Log.d(TAG, "onComplete: " + e.getMessage());
                            }
                            updateUI(null);
                        }
                    }
                });
        // [END create_user_with_email]
    }

    public void updateUI(FirebaseUser currentUser) {
        if (currentUser !=  null) {
            Intent loginIntent = new Intent(this, LogInActivity.class);
            startActivity(loginIntent);
        }
    }
}