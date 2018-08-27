package com.example.prajw.hacknews;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.prajw.hacknews.BottomNavigationViewHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;


import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private FirebaseFirestore firebaseFirestore;
    private StorageReference firebaseStorage;

    private EditText name, place;
    private EditText something, dob, institution, git;
    private Button submit;
    private CircleImageView profilePic;
    private Uri mainImageUri =null;
    private Boolean isChanged = false;
    private String uid, NAME, INSTI, GIT, PLACE, DOB;
    private ImageView settings;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        profilePic = findViewById(R.id.profilepic);
        submit = findViewById(R.id.submit);
        dob = findViewById(R.id.dob);
        institution = findViewById(R.id.institution);
        git = findViewById(R.id.github);
        name = findViewById(R.id.tv_name);
        place = findViewById(R.id.tv_address);
        settings = findViewById(R.id.settings);

        Log.d("dadda","dddedcdccd");
        if(getIntent().getStringExtra("start").equals("1")){
            findViewById(R.id.bottom_nav).setVisibility(View.GONE);
            Log.d("dadda","ddde");
            submit.setVisibility(View.VISIBLE);
            submit.setEnabled(true);
        }else{
            settings.setVisibility(View.VISIBLE);
            submit.setVisibility(View.GONE);
            setupBottomNavigation();
        }

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance().getReference();
        uid = mAuth.getCurrentUser().getUid();
        firebaseFirestore.collection("Users").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {

                    if (task.getResult().exists()) {

                        Toast.makeText(ProfileActivity.this, "Data exists", Toast.LENGTH_LONG).show();
                        String nameF = task.getResult().getString("name");
                        String profilePicF = task.getResult().getString("profilePic");
                        String placeF = task.getResult().getString("place");
                        String dobF = task.getResult().getString("dob");
                        String institutionF = task.getResult().getString("institution");
                        String gitF = task.getResult().getString("git");

                        mainImageUri = Uri.parse(profilePicF);
                        name.setText(nameF);
                        dob.setText(dobF);
                        institution.setText(institutionF);
                        git.setText(gitF);
                        place.setText(placeF);

                        RequestOptions placeholderRequest = new RequestOptions();
                        placeholderRequest.placeholder(R.drawable.ic_launcher_background);
                        Glide.with(ProfileActivity.this).setDefaultRequestOptions(placeholderRequest).load(profilePicF).into(profilePic);

                    }else{

                        Toast.makeText(ProfileActivity.this,"Data doesn't exists",Toast.LENGTH_LONG).show();
                    }
                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(ProfileActivity.this, "(Firestore Retrieve)error: " + error, Toast.LENGTH_LONG).show();

                }
                //progress.setVisibility(View.INVISIBLE);
                //submit.setEnabled(true);
            }
        });


       /* firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user =FirebaseAuth.getInstance().getCurrentUser();
                if(user!=null){

                }
            }
        }*/


        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this,AccountSettingsActivity.class));
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NAME = name.getText().toString();
                DOB = dob.getText().toString();
                INSTI = institution.getText().toString();
                GIT = git.getText().toString();
                PLACE = place.getText().toString();

                if (!TextUtils.isEmpty(NAME)&& !TextUtils.isEmpty(DOB)&& !TextUtils.isEmpty(INSTI)&& !TextUtils.isEmpty(GIT) && !TextUtils.isEmpty(PLACE) && mainImageUri != null) {
                    //progress.setVisibility(View.VISIBLE);

                    if (isChanged) {

                        //uid = mAuth.getCurrentUser().getUid();
                        StorageReference imagePath = firebaseStorage.child("Profile Pictures").child(uid + ".jpg");
                        imagePath.putFile(mainImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if (task.isSuccessful()) {

                                    storeFirestore(task);
                                } else {
                                    String error = task.getException().getMessage();
                                    Toast.makeText(ProfileActivity.this, "error: " + error, Toast.LENGTH_LONG).show();

                                    //progress.setVisibility(View.INVISIBLE);
                                }
                            }
                        });


                    } else {
                        storeFirestore(null);
                    }
                }else{
                    Toast.makeText(ProfileActivity.this,"Fill all",Toast.LENGTH_SHORT);
                }
            }
        });

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

                    if(ContextCompat.checkSelfPermission(ProfileActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){

                        Toast.makeText(ProfileActivity.this,"Permission NOT    Granted",Toast.LENGTH_LONG).show();

                        ActivityCompat.requestPermissions(ProfileActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                    }
                    else
                    {
                        BringImagePicker();
                    }

                } else {

                    BringImagePicker();
                }
            }
        });

    }

    private void storeFirestore(@NonNull Task<UploadTask.TaskSnapshot> task ) {

        //progress.setVisibility(View.VISIBLE);
        Uri downloadUri;
        if(task!=null){
            downloadUri = task.getResult().getDownloadUrl();
        }
        else{
            downloadUri =mainImageUri;
        }

        Map<String,String> userMap = new HashMap<>();
        userMap.put("name", NAME);
        userMap.put("place", PLACE);
        userMap.put("institution", INSTI);
        userMap.put("dob", DOB);
        userMap.put("git", GIT);
        userMap.put("profilePic",downloadUri.toString());

        firebaseFirestore.collection("Users").document(uid).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){
                    Toast.makeText(ProfileActivity.this,"User settings are Updated",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(ProfileActivity.this,MainActivity.class));
                    finish();

                } else {

                    String error=task.getException().getMessage();
                    Toast.makeText(ProfileActivity.this,"error: "+error,Toast.LENGTH_LONG).show();
                }
                // progress.setVisibility(View.INVISIBLE);
            }
        });
        //Toast.makeText(SetupActivity.this,"The image is Uploaded",Toast.LENGTH_LONG).show();
    }

    private void BringImagePicker() {

        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1,1)
                .start(ProfileActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mainImageUri = result.getUri();
                profilePic.setImageURI(mainImageUri);
                isChanged = true;
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    public void setupBottomNavigation(){
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottom_nav);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(ProfileActivity.this, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(3);
        menuItem.setChecked(true);
    }

}
