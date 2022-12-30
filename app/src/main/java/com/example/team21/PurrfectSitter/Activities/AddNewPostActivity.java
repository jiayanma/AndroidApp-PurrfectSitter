package com.example.team21.PurrfectSitter.Activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.location.Address;
import android.location.Geocoder;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.example.team21.PurrfectSitter.FirebaseClass.Config;
import com.example.team21.PurrfectSitter.FirebaseClass.Location;
import com.example.team21.PurrfectSitter.FirebaseClass.Post;
import com.example.team21.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpEntity;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpResponse;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.ClientProtocolException;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.HttpClient;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.methods.HttpGet;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.impl.client.DefaultHttpClient;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class AddNewPostActivity extends AppCompatActivity {
    private EditText titleEt;
    private EditText startDateET;
    private EditText endDateET;
    private EditText addressET;
    private EditText cityET;
    private EditText descriptionET;
    private AutoCompleteTextView stateAT;
    private ImageSwitcher imageIS;
    private EditText zipcodeET;
    private Button previousBtn;
    private Button nextBtn;
    private Button uploadBtn;
    private Button cameraBtn;
    private Button submitBtn;
    private Button deleteBtn;
    private DatePickerDialog datePickerDialog1;
    private DatePickerDialog datePickerDialog2;
    private ArrayList<Uri> imageUris;
    private ProgressBar progressBar;
    private static final int PICK_IMAGES_CODE = 0;
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    ActivityResultLauncher<Intent> pickImageActivityResultLauncher;
    ActivityResultLauncher<Intent> cameraActivityResultLauncher;
    // position of selected image
    int position = 0;
    Uri cam_uri;

    private int startYear;
    private int startMonth;
    private int startDay;
    private int endYear;
    private int endMonth;
    private int endDay;

    private String title;
    private String startDate;
    private String endDate;
    private String address;
    private String city;
    private String state;
    private String zipcode;
    private String description;
    private Location location;
    private Long startDateTimestamp;
    private Long endDateTimestamp;
    private List<String> imagesList;

    private FirebaseDatabase db;
    private DatabaseReference postRef;
    private FirebaseAuth mAuth;
    private StorageReference storage;
    private Post post;
    String downloadUriStr;
    String creatorId;
    List<Double> locationCoord;
    private final String TAG = "AddNewPostActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_post);
        titleEt = findViewById(R.id.et_title);

        deleteBtn = findViewById(R.id.btn_delete);
        deleteBtn.setBackgroundResource(R.drawable.ic_trash_can_100);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        startDateET = findViewById(R.id.et_startDate);
        endDateET = findViewById(R.id.et_endDate);
        initStartDatePicker();
        initEndDatePicker();
        startDateET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker1(v);
            }
        });
        endDateET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker2(v);
            }
        });

        addressET = findViewById(R.id.et_address);
        cityET = findViewById(R.id.et_city);
        stateAT = findViewById(R.id.et_state);
        zipcodeET = findViewById(R.id.et_zipcode);
        descriptionET = findViewById(R.id.et_description);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, getResources()
                .getStringArray(R.array.States));
        String selection;
        stateAT.setAdapter(arrayAdapter);
        stateAT.setCursorVisible(false);
        stateAT.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                stateAT.showDropDown();
                String selection = (String) parent.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(), selection,
                        Toast.LENGTH_SHORT);
            }
        });

        stateAT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View arg0) {
                stateAT.showDropDown();
            }
        });

        imageIS = findViewById(R.id.iv_imgGallery);
        previousBtn = findViewById(R.id.bt_previous);
        nextBtn = findViewById(R.id.bt_next);
        uploadBtn = findViewById(R.id.btn_upload);
        cameraBtn = findViewById(R.id.btn_camera);

        imageUris = new ArrayList<>();
        imageIS.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView = new ImageView(getApplicationContext());
                return imageView;
            }
        });
        pickImageActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes
                            Intent data = result.getData();
                            if (data.getClipData() != null) {
                                int count = data.getClipData().getItemCount();
                                for (int i = 0; i < count; i++) {
                                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                                    imageUris.add(imageUri);
                                }
                                imageIS.setImageURI(imageUris.get(0));
                                position = 0;
                            } else {
                                Uri imageUri = data.getData();
                                imageUris.add(imageUri);
                                imageIS.setImageURI(imageUris.get(0));
                                position = 0;
                            }
                        }
                    }
                });

        cameraActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            imageUris.add(cam_uri);
                            imageIS.setImageURI(imageUris.get(0));
                            position = 0;
                        }
                    }
                });

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImagesIntent();
            }
        });

        previousBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position > 0) {
                    position--;
                    imageIS.setImageURI(imageUris.get(position));
                } else {
                    Toast.makeText(AddNewPostActivity.this, "No previous images...",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position < imageUris.size() - 1) {
                    position++;
                    imageIS.setImageURI(imageUris.get(position));
                } else {
                    Toast.makeText(AddNewPostActivity.this, "No more images...",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUris.size() == 0) {
                    Toast.makeText(AddNewPostActivity.this, "No image to delete.",
                            Toast.LENGTH_SHORT).show();
                } else if (imageUris.size() == 1) {
                    imageUris.remove(0);
                    imageIS.setImageDrawable(null);
                } else {
                    Uri currUri = imageUris.get(position);
                    imageUris.remove(currUri);
                    if (position < imageUris.size() - 1) {
                        imageIS.setImageURI(imageUris.get(position));
                    } else {
                        imageIS.setImageURI(imageUris.get(imageUris.size() - 1));
                    }
                }
            }
        });

        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                }
                else
                {
                    openCamera();
                }
            }
        });

        db = FirebaseDatabase.getInstance();
        postRef = db.getReference(Config.POSTS);
        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance().getReference();

        submitBtn = findViewById(R.id.bt_submit);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = titleEt.getText().toString().trim();
                startDate = startDateET.getText().toString().trim();
                endDate = endDateET.getText().toString().trim();
                address = addressET.getText().toString().trim();
                city = cityET.getText().toString().trim();
                state = stateAT.getText().toString().trim();
                zipcode = zipcodeET.getText().toString().trim();
                description = descriptionET.getText().toString().trim();
                if (!validateFields()) return;
                location = new Location(address, city, state, zipcode);
                FirebaseUser user = mAuth.getCurrentUser();
                creatorId = user.getUid();
                try {
                    startDateTimestamp = getTimeStamp(startYear, startMonth, startDay);
                    if (startDateTimestamp < System.currentTimeMillis()) {
                        Toast.makeText(AddNewPostActivity.this, "Choose a future date.",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                try {
                    endDateTimestamp = getTimeStamp(endYear, endMonth, endDay);
                    if (endDateTimestamp < startDateTimestamp) {
                        Toast.makeText(AddNewPostActivity.this, "Invalid date range",
                                Toast.LENGTH_SHORT).show();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                locationCoord = getGeoLocation();

                if(creatorId != null && !imageUris.isEmpty()) {
                    imagesList = new ArrayList<>();
                    for (Uri imageUri: imageUris) {
                        if (imageUri != null) {
                            progressBar.setVisibility(View.VISIBLE);
                            uploadToFirebase(imageUri);
//                            imagesList.add(downloadUriStr);
                        }
                     }
//                    createPost(creatorId, locationCoord);
                }
            }
        });
    }

    private void uploadToFirebase(Uri uri) {
        StorageReference fileRef = storage.child(System.currentTimeMillis() + "." + getFileExtension(uri));
        UploadTask uploadTask = fileRef.putFile(uri);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();

                        }
                        // Continue with the task to get the download URL
                        return fileRef.getDownloadUrl();

                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            downloadUriStr = task.getResult().toString();
                            imagesList.add(downloadUriStr);
                            if (imagesList.size() == imageUris.size()) {
                                if (locationCoord != null && locationCoord.size() > 0) {
                                    createPost(creatorId, locationCoord);
                                } else {
                                    String fullAddress = address + "," + city + "," + state + "," + "United States";
                                    try {
                                        locationCoord = getLocationFromString(fullAddress);
                                        createPost(creatorId, locationCoord);
                                    } catch (JSONException e) {
                                        Toast.makeText(AddNewPostActivity.this, "Address not found",
                                                Toast.LENGTH_LONG).show();
                                    } catch (UnsupportedEncodingException e) {
                                        Toast.makeText(AddNewPostActivity.this, "Address not found",
                                                Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        }
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }

    public static List<Double> getLocationFromString(String address)
            throws JSONException, UnsupportedEncodingException {

        HttpGet httpGet = new HttpGet(
                "http://maps.google.com/maps/api/geocode/json?address="
                        + URLEncoder.encode(address, "UTF-8") + "&ka&sensor=false");
        HttpClient client = new DefaultHttpClient();
        HttpResponse response;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            InputStream stream = entity.getContent();
            int b;
            while ((b = stream.read()) != -1) {
                stringBuilder.append((char) b);
            }
        } catch (ClientProtocolException e) {
        } catch (IOException e) {
        }

        JSONObject jsonObject = new JSONObject(stringBuilder.toString());

        double lng = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                .getJSONObject("geometry").getJSONObject("location")
                .getDouble("lng");

        double lat = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                .getJSONObject("geometry").getJSONObject("location")
                .getDouble("lat");

        List<Double> coordinates = new ArrayList<>();
        coordinates.add(lat);
        coordinates.add(lng);
        return coordinates;
    }

    private String getFileExtension(Uri mUri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));
    }

    private Long getTimeStamp(int year, int month, int day) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String str = day + "/" + month + "/" + year;
        Date date = dateFormat.parse(str);
        return date.getTime();
    }

    private List<Double> getGeoLocation(){
        Geocoder geocoder = new Geocoder(this);
        List<Address> addressList;

        String fullAddress = address + "," + city + "," + state + "," + "United States";
        try {
            addressList = geocoder.getFromLocationName(fullAddress, 1);
            if (addressList == null) {
                Toast.makeText(AddNewPostActivity.this, "Address not found",
                        Toast.LENGTH_LONG).show();
                return null;
            }
            double latitude = addressList.get(0).getLatitude();
            double longitude = addressList.get(0).getLongitude();
            List<Double> coordinates = new ArrayList<>();
            coordinates.add(latitude);
            coordinates.add(longitude);
            return coordinates;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void createPost(String creatorId, List<Double> locationCoord) {
        post = new Post(creatorId, title, description, startDateTimestamp, endDateTimestamp,
                location, locationCoord, imagesList);
        String keyId = postRef.push().getKey();
        postRef.child(keyId).setValue(post) .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: ");
                        progressBar = findViewById(R.id.progressBar);
                        progressBar.setVisibility(View.GONE);
                        Intent maintIntent = new Intent(AddNewPostActivity.this, MainPostActivity.class);
                        maintIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(maintIntent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: " + e.getMessage());
                    }
                });
    }

    public void openMainPost() {
        Intent intent = new Intent(this, MainPostActivity.class);
        startActivity(intent);
    }

    private boolean validateFields() {
        if (TextUtils.isEmpty(title)) {
            Toast.makeText(AddNewPostActivity.this, "Title cannot be empty",
                    Toast.LENGTH_LONG).show();
            return false;
        }

        if (TextUtils.isEmpty(startDate)) {
            Toast.makeText(AddNewPostActivity.this, "Start date cannot be empty",
                    Toast.LENGTH_LONG).show();
            return false;
        }

        if (TextUtils.isEmpty(endDate)) {
            Toast.makeText(AddNewPostActivity.this, "End date cannot be empty",
                    Toast.LENGTH_LONG).show();
            return false;
        }

        if (TextUtils.isEmpty(address)) {
            Toast.makeText(AddNewPostActivity.this, "Address cannot be empty",
                    Toast.LENGTH_LONG).show();
            return false;
        }

        if (TextUtils.isEmpty(city)) {
            Toast.makeText(AddNewPostActivity.this, "City cannot be empty",
                    Toast.LENGTH_LONG).show();
            return false;
        }

        if (TextUtils.isEmpty(state)) {
            Toast.makeText(AddNewPostActivity.this, "State cannot be empty",
                    Toast.LENGTH_LONG).show();
            return false;
        }

        if (TextUtils.isEmpty(zipcode)) {
            Toast.makeText(AddNewPostActivity.this, "Zip code cannot be empty",
                    Toast.LENGTH_LONG).show();
            return false;
        }

        if (TextUtils.isEmpty(description)) {
            Toast.makeText(AddNewPostActivity.this, "Description cannot be empty",
                    Toast.LENGTH_LONG).show();
            return false;
        }

        if (imageUris.isEmpty()) {
            Toast.makeText(AddNewPostActivity.this, "Upload at least one image",
                    Toast.LENGTH_LONG).show();
            return false;
        }

        return true;


    }

    private void openCamera() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From Camera");
        cam_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cam_uri);
        cameraActivityResultLauncher.launch(cameraIntent);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                openCamera();
            }
            else
            {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void pickImagesIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        pickImageActivityResultLauncher.launch(intent);
    }

    private void initStartDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                startDay = day;
                startMonth = month;
                startYear = year;
                startDateET.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        startYear = cal.get(Calendar.YEAR);
        startMonth = cal.get(Calendar.MONTH);
        startDay = cal.get(Calendar.DAY_OF_MONTH);

        datePickerDialog1 = new DatePickerDialog(this, dateSetListener, startYear, startMonth, startDay);
    }

    private void initEndDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                endDay = day;
                endMonth = month;
                endYear = year;
                endDateET.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        endYear = cal.get(Calendar.YEAR);
        endMonth = cal.get(Calendar.MONTH);
        endDay = cal.get(Calendar.DAY_OF_MONTH);

        datePickerDialog2 = new DatePickerDialog(this, dateSetListener, endYear, endMonth, endDay);
    }

    private String makeDateString(int day, int month, int year) {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month) {
        switch (month) {
            case 2:
                return "FEB";
            case 3:
                return "MAR";
            case 4:
                return "APR";
            case 5:
                return "MAY";
            case 6:
                return "JUN";
            case 7:
                return "JUL";
            case 8:
                return "AUG";
            case 9:
                return "SEP";
            case 10:
                return "OCT";
            case 11:
                return "NOV";
            case 12:
                return "DEC";
            default:
                return "JAN";
        }
    }

    private void openDatePicker1(View view) {
        datePickerDialog1.show();
    }

    private void openDatePicker2(View view) {
        datePickerDialog2.show();
    }
}