package com.example.team21.PurrfectSitter.Activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.team21.PurrfectSitter.FirebaseClass.Config;
import com.example.team21.PurrfectSitter.FirebaseClass.Pet;
import com.example.team21.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddPetActivity extends AppCompatActivity {
    ImageView iv_pet_avatar;
    Button btn_pet_upload;
    Button btn_pet_camera;
    EditText et_pet_name;
    Spinner sp_pet_species;
    EditText et_pet_age;
    Button btn_cancel;
    Button btn_add;
    ProgressBar loading;

    String name;
    String species;
    String age;

    ActivityResultLauncher<Intent> cameraActivityResultLauncher;
    ActivityResultLauncher<Intent> uploadActivityResultLauncher;
    Uri imageUri;
    String uriString;

    FirebaseDatabase db;
    FirebaseAuth mAuth;
    DatabaseReference userRef;
    StorageReference storage;
    String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pet);

        db = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        userRef = db.getReference(Config.USERS);
        storage = FirebaseStorage.getInstance().getReference();

        userID = mAuth.getCurrentUser().getUid();
        //userID = "-NHMmHIgLHzKxsNTX-km";

        iv_pet_avatar = findViewById(R.id.iv_pet_avatar);
        btn_pet_upload = findViewById(R.id.btn_pet_uplaod_avt);
        btn_pet_camera = findViewById(R.id.btn_pet_camera);
        et_pet_name = findViewById(R.id.et_pet_name);
        sp_pet_species = findViewById(R.id.sp_pet_species);
        et_pet_age = findViewById(R.id.et_pet_age);
        btn_cancel = findViewById(R.id.btn_pet_cancel);
        btn_add = findViewById(R.id.btn_pet_add);
        loading = findViewById(R.id.pb_loading);

        setUpSpeciesSpinner();
        initLauncher();

        btn_pet_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickUpload();
            }
        });

        btn_pet_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickCamera();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickCancel();
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickAdd();
            }
        });

    }

    private void setUpSpeciesSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddPetActivity.this,
                R.layout.spinner_style, getResources().getStringArray(R.array.species));

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_pet_species.setAdapter(adapter);
    }

    private void initLauncher() {
        uploadActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            imageUri = data.getData();
                            iv_pet_avatar.setImageURI(data.getData());
                        }
                    }
                });

        cameraActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            iv_pet_avatar.setImageURI(imageUri);
                        }
                    }
                });
    }

    private boolean checkCameraPermission() {
        boolean res1 = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        boolean res2 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        return res1 && res2;
    }

    private void requestCameraPermission() {
        requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
    }

    private boolean checkStoragePermission() {
        boolean res = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        return res;
    }

    private void requestStoragePermission() {
        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
    }

    private void onClickAdd() {
        loading.setVisibility(View.VISIBLE);
        name = et_pet_name.getText().toString();
        age = et_pet_age.getText().toString();
        species = sp_pet_species.getSelectedItem().toString();

        // TODO validate et content
        if (name.length() == 0 || age.length() == 0 || species.length() == 0) {
            Toast.makeText(this, "Please enter all information required", Toast.LENGTH_LONG).show();
            return;
        }

        if (imageUri != null) {
            StorageReference fileRef = storage.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
            UploadTask uploadTask = fileRef.putFile(imageUri);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }

                            return fileRef.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                uriString = task.getResult().toString();
                                Pet pet = new Pet(name, species, Integer.parseInt(age), uriString);
                                userRef.child(userID).child("pets").push().setValue(pet);
                                Toast.makeText(AddPetActivity.this, "ADDED", Toast.LENGTH_SHORT).show();
                                onBackPressed();
                            }
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("upload avatar", e.toString());
                }
            });
        }

    }

    private String getFileExtension(Uri mUri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));
    }

    private void onClickCancel() {
        onBackPressed();
    }

    private void onClickUpload() {
        if (!checkStoragePermission()) {
            requestStoragePermission();
        }
        if (checkStoragePermission()) {
            //Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            uploadActivityResultLauncher.launch(intent);
        }
    }

    private void onClickCamera() {
        if (!checkCameraPermission()) {
            requestCameraPermission();
        }
        if (checkCameraPermission()) {
            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, "New Picture");
            values.put(MediaStore.Images.Media.DESCRIPTION, "From Camera");
            imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            cameraActivityResultLauncher.launch(intent);
        }
    }
}