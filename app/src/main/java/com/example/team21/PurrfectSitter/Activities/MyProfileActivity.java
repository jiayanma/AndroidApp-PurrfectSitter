package com.example.team21.PurrfectSitter.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.team21.PurrfectSitter.Util.EditPetRecyclerView.EditPetItem;
import com.example.team21.PurrfectSitter.Util.EditPetRecyclerView.EditPetItemAdapter;
import com.example.team21.PurrfectSitter.FirebaseClass.Config;
import com.example.team21.PurrfectSitter.FirebaseClass.Pet;
import com.example.team21.PurrfectSitter.Util.Helper;
import com.example.team21.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyProfileActivity extends AppCompatActivity{
    private BottomNavigationView bnv;
    TextView tv_logout;
    ImageView iv_avatar;
    TextView tv_name;
    TextView tv_email;
    ImageView iv_edit;
    ImageView iv_add_pet;
    TextView tv_posts;
    TextView tv_applied;
    TextView tv_host_reviews;
    TextView tv_sitter_reviews;
    TextView tv_receivedApply;
    RecyclerView rv_pets;
    EditPetItemAdapter petAdapter;

    FirebaseDatabase db;
    FirebaseAuth mAuth;
    DatabaseReference userRef;

    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        db = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        userRef = db.getReference(Config.USERS);

        tv_logout = findViewById(R.id.tv_logout);
        iv_avatar = findViewById(R.id.iv_avatar);
        tv_name = findViewById(R.id.tv_name);
        tv_email = findViewById(R.id.tv_pur_email);
        iv_edit = findViewById(R.id.iv_edit);
        iv_add_pet = findViewById(R.id.iv_add_pet);
        tv_posts = findViewById(R.id.tv_posts);
        tv_applied = findViewById(R.id.tv_applied);
        tv_receivedApply = findViewById(R.id.tv_application);
        rv_pets = findViewById(R.id.rv_pets);
        rv_pets.setLayoutManager(new LinearLayoutManager(MyProfileActivity.this));
        rv_pets.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        tv_host_reviews = findViewById(R.id.tv_host_reviews);
        tv_sitter_reviews = findViewById(R.id.tv_sitter_reviews);

        userID = mAuth.getCurrentUser().getUid();
        updateUI();

        tv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(MyProfileActivity.this, LogInActivity.class);
                startActivity(intent);
            }
        });

        tv_posts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyProfileActivity.this, CurrentUserPostsActivity.class);
                startActivity(intent);
            }
        });

        tv_applied.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyProfileActivity.this, CurrentUserAppliedActivity.class);
                startActivity(intent);
            }
        });

        iv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyProfileActivity.this, EditProfileActivity.class);
                startActivity(intent);
            }
        });

        iv_add_pet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyProfileActivity.this, AddPetActivity.class);
                startActivity(intent);
            }
        });

        tv_host_reviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), ReviewsActivity.class);
                intent.putExtra("buttonNumber", 1);
                startActivity(intent);
            }
        });

        tv_sitter_reviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), ReviewsActivity.class);
                intent.putExtra("buttonNumber", 2);
                startActivity(intent);
            }
        });

        bnv = findViewById(R.id.bottomNavigationView);
        bnv.setSelectedItemId(R.id.personMenu);
        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.homePageMenu:
                        startActivity(new Intent(getApplicationContext(), MainPostActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.personMenu:
                        return true;
                }
                return false;
            }
        });

        tv_receivedApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyProfileActivity.this, ReceivedApplyActivity.class);
                intent.putExtra("userId", userID);
                startActivity(intent);
            }
        });
    }

    private void updateUI() {
        tv_email.setText(mAuth.getCurrentUser().getEmail());
        userRef.orderByKey().equalTo(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataSnapshot mSnapshot = snapshot.getChildren().iterator().next();
                tv_name.setText((CharSequence) mSnapshot.child("displayName").getValue());
                Helper.loadImageViewFromURL(MyProfileActivity.this, mSnapshot.child("avatar").getValue(String.class), iv_avatar);
                getPetList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getPetList() {
        userRef.child(userID).child("pets").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> petListItr = snapshot.getChildren();
                ArrayList<EditPetItem> petList = new ArrayList<>();
                for (DataSnapshot pet : petListItr) {
                    Log.i("pet key", pet.getKey());
                    petList.add(new EditPetItem(pet.getValue(Pet.class), pet.getKey()));
                }

                petAdapter = new EditPetItemAdapter(petList, MyProfileActivity.this);
                rv_pets.setAdapter(petAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
