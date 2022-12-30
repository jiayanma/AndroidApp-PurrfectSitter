package com.example.team21.PurrfectSitter.Activities;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.team21.MainActivity;
import com.example.team21.PurrfectSitter.FirebaseClass.Apply;
import com.example.team21.PurrfectSitter.FirebaseClass.Config;
import com.example.team21.PurrfectSitter.FirebaseClass.Post;
import com.example.team21.PurrfectSitter.Util.PostBriefRecyclerView.PostBriefAdapter;
import com.example.team21.PurrfectSitter.Util.ItemClickListenerInterface;
import com.example.team21.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainPostActivity extends AppCompatActivity implements ItemClickListenerInterface {
    private FirebaseDatabase db;
    private MainPostActivity activity;
    private ArrayList<Post> postBriefList;
    private RecyclerView postRecyclerView;
    private PostBriefAdapter postAdapter;
    private BottomNavigationView bnv;
    private FloatingActionButton fab;
    private SearchView searchView;
    private FirebaseUser curUser;
    private List<String> states;
    private long lastUpdateTimestamp;
    private static long lastNotificationTime;
    private final String TAG = "MainPostActivity";
    public static final String SEARCH_INPUT = "search_input";
    public static final String USER_ID = "user_id";
    public static final String POST_ID = "post_id";
    public final String CHANNEL_ID = "application channel";

    private boolean validateZipcode(String query) {
        String regex = "^[0-9]{5}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(query);
        return matcher.matches();
    }

    private boolean validateCity(String query) {
        String[] ss = query.split(",");
        if (ss.length == 1) {
            return true;
        }
        if (ss.length != 2) {
            return false;
        }
        String state = ss[1];
        return states.contains(state.toUpperCase());
    }

    private void sendNotification(Apply apply) {
        Intent notificationIntent = new Intent(this, ReceivedApplyActivity.class);
        notificationIntent.putExtra("userId", curUser.getUid());
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(this,0,notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground_21)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_foreground_21))
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher_foreground_21))
                        .bigLargeIcon(null))
                .setContentTitle("You have a new application!")
                .setContentText("From " + apply.applicantName)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVibrate(new long[]{100,1000,200,340})
                .setAutoCancel(false)//true touch on notificaiton menu dismissed, but swipe to dismiss
                .setTicker("Notification");
        builder.setContentIntent(contentIntent);
        NotificationManagerCompat m = NotificationManagerCompat.from(getApplicationContext());
        //id to generate new notification in list notifications menu
        m.notify(new Random().nextInt(),builder.build());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_post);
        db = FirebaseDatabase.getInstance();
        states = Arrays.asList(getResources().getStringArray(R.array.States));
        activity = this;
        lastUpdateTimestamp = 0;
        curUser = FirebaseAuth.getInstance().getCurrentUser();
        createNotificationChannel();
        lastNotificationTime = System.currentTimeMillis();

        // set bottom navigation
        bnv = findViewById(R.id.bottomNavigationView);
        bnv.setSelectedItemId(R.id.homePageMenu);
        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.homePageMenu:
                        return true;
                    case R.id.personMenu:
                        startActivity(new Intent(getApplicationContext(), MyProfileActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newPostIntent = new Intent(activity, AddNewPostActivity.class);
                newPostIntent.putExtra(LogInActivity.USERNAME, curUser);
                startActivity(newPostIntent);
            }
        });

        // set search bar actions
        searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Intent intent = new Intent(activity, PostSearchResultActivity.class);
                if (!validateZipcode(s) && !validateCity(s)) {
                    Toast.makeText(activity, "Please enter valid zipcode or city, state", Toast.LENGTH_SHORT).show();
                    return true;
                }
                intent.putExtra(SEARCH_INPUT, s);
                intent.putExtra(LogInActivity.USERNAME, curUser);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        // set RecyclerView actions
        postBriefList = new ArrayList<Post>();
        postRecyclerView = findViewById(R.id.postRV);
        LinearLayoutManager layoutManager = new LinearLayoutManager(MainPostActivity.this );
        postRecyclerView.setLayoutManager(layoutManager);
        postRecyclerView.addItemDecoration(new DividerItemDecoration(activity, DividerItemDecoration.VERTICAL));

        DatabaseReference postsRef = db.getReference();
        postsRef.child(Config.POSTS)
                .orderByChild("applied")
                .equalTo(false)
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot postSnapshot: snapshot.getChildren()) {
                    String postId = postSnapshot.getKey();
                    Post post = postSnapshot.getValue(Post.class);
                    if (post.timestamp > lastUpdateTimestamp && post.requestEndTimestamp > System.currentTimeMillis()) {
                        post.postId = postId;
                        postBriefList.add(post);
                    }
                }
                lastUpdateTimestamp = System.currentTimeMillis();
                Collections.sort(postBriefList);
                postAdapter = new PostBriefAdapter(postBriefList, MainPostActivity.this);
                postAdapter.setOnItemClickListener(activity);
                postRecyclerView.setAdapter(postAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "read post list cancelled");
            }
        });

        db.getReference(Config.APPLY).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> applyItr = snapshot.getChildren();
                Apply recentApply = null;
                for (DataSnapshot application : applyItr) {
                    Apply applyItem = application.getValue(Apply.class);
                    Log.v(TAG, applyItem.applyCreatorId);
                    Log.v(TAG, applyItem.timeStamp.toString());
                    if (applyItem.applyCreatorId.equals(curUser.getUid())) {
                        if (applyItem.timeStamp.longValue() > lastNotificationTime && (recentApply == null || applyItem.timeStamp > recentApply.timeStamp)) {
                            recentApply = applyItem;
                        }
                    }
                }
                if (recentApply != null) {
                    lastNotificationTime = recentApply.timeStamp;
                    sendNotification(recentApply);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.v("UserProfileActivity", "db call cancelled");
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        Post postBrief = postBriefList.get(position);
        if (postBrief != null) {
            Intent intent = new Intent(this, PostDetailActivity.class);
            intent.putExtra(USER_ID, curUser.getUid());
            intent.putExtra(POST_ID, postBrief.postId);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    // default create notification channel function from https://developer.android.com/reference/android/app/NotificationManager
    public void createNotificationChannel() {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = manager.getNotificationChannel(CHANNEL_ID);
            if(channel ==null) {
                CharSequence name = getString(R.string.channel_name);
                String description = getString(R.string.channel_description);
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                channel = new NotificationChannel(getString(R.string.channel_id), name, importance);
                channel.setDescription(description);
                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }
        }
    }
}
