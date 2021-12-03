package com.example.foody.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.foody.R;
import com.example.foody.fragment.CommentFragment;
import com.example.foody.helper.Contain;
import com.example.foody.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {

    public static int SELECT_IMAGE_CODE = 1;
    private ImageView backIcon;
    DatabaseReference mReference;
    CardView cardView;
    ImageView imageUser;
    TextView userNameBar, emailBar, userNamePr, emailPr, camera;
    String userId;
    Uri uri;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_profile);
        setSupportActionBar(toolbar);
        mapview();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userId = extras.getString("userId");
        }
        getUser(userId);
        setActionbar();
        setListenerView();

    }

    private void loadImgClient(User user) {
        if (user.imageName != null && !user.imageName.isEmpty()){
            try {
                final String name = user.imageName;
                final String type = user.imageType;
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageReference = storage.getReference();
                StorageReference reference = storageReference.child("ImageProfile/" + user.imageName + "." + user.imageType);
                final File localFile = File.createTempFile(name, type);
                reference.getFile(localFile).addOnSuccessListener(downloadResult -> {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    imageUser.setImageBitmap(bitmap);
                }).addOnFailureListener(e -> {
                    Log.e("Profile", " get image profile " + user.imageName + " fail");
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void setListenerView() {

        camera.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            ProfileActivity.this.startActivityForResult(Intent.createChooser(intent, "Chọn Ảnh"), SELECT_IMAGE_CODE);
        });

    }

    void mapview() {
        cardView = findViewById(R.id.cardView);
        imageUser = findViewById(R.id.image_user);
        userNameBar = findViewById(R.id.userNameBar);
        emailBar = findViewById(R.id.emailBar);
        userNamePr = findViewById(R.id.userNamePr);
        emailPr = findViewById(R.id.emailPr);
        camera = findViewById(R.id.change_photo);
        mReference = FirebaseDatabase.getInstance(Contain.REALTIME_DATABASE).getReference();
    }

    private void loadInfoUser(User user) {
        userNameBar.setText(user.userName);
        userNamePr.setText(user.userName);
        emailPr.setText(user.email);
        emailBar.setText(user.email);
    }

    void setActionbar() {
        backIcon = findViewById(R.id.back_home_profile);

        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("Click back icon ", "100");
                finish();
            }
        });

    }

    public void getUser(String userId) {

        DatabaseReference profile = mReference.child("User").child(userId).child("Profile");
        User user = new User();
        profile.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user.userName = dataSnapshot.child("UserName").getValue().toString();
                user.email = dataSnapshot.child("Email").getValue().toString();
                try {
                    user.imageName = dataSnapshot.child("ImageName").getValue().toString();
                    user.imageType = dataSnapshot.child("ImageType").getValue().toString();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                loadInfoUser(user);
                loadImgClient(user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.e("Profile:", "loadPost:onCancelled", databaseError.toException());
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            uri = data.getData();
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("ImageName", userId);
            String type = getExtension(uri);
            hashMap.put("ImageType", type);
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageReference = storage.getReference();
            StorageReference reference = storageReference.child("ImageProfile/" + userId + "." + type);
            UploadTask task = reference.putFile(uri);
            task.addOnFailureListener(exception -> Log.e("error aaaaaaaaaaaaaa", exception.getMessage())).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.e(">>>>>>>>>>>>>>>>>>>>>", "succes");
                }
            });
        }
    }

    String getExtension(Uri url) {
        ContentResolver cr = ProfileActivity.this.getContentResolver();
        MimeTypeMap mine = MimeTypeMap.getSingleton();
        return mine.getExtensionFromMimeType(cr.getType(url));
    }
}