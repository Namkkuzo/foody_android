package com.example.foody.fragment;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.foody.R;
import com.example.foody.adapter.ListCommentAdapter;
import com.example.foody.helper.Contain;
import com.example.foody.model.CommentRecipe;
import com.example.foody.model.Recipe;
import com.example.foody.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.huawei.agconnect.cloud.storage.core.AGCStorageManagement;
import com.huawei.agconnect.cloud.storage.core.FileMetadata;
import com.huawei.agconnect.cloud.storage.core.StorageReference;
import com.huawei.agconnect.cloud.storage.core.UploadTask;
import com.huawei.hmf.tasks.OnCompleteListener;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.chrono.MinguoChronology;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;


public class CommentFragment extends Fragment {

    public static int SELECT_IMAGE_CODE = 1;

    boolean showImage;
    boolean canSend;

    ImageButton camera, send, cancel;
    ImageView image ;
    RecyclerView recyclerView;
    ConstraintLayout layoutImage ;
    EditText comment;
    View view;
    Uri uri ;
    Recipe recipe;
    User user;
    DatabaseReference mReference;


    public CommentFragment(Recipe recipe , User user) {
        this.recipe = recipe;
        this.user = user ;
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_comment, container, false);
        mReference = FirebaseDatabase.getInstance(Contain.REALTIME_DATABASE).getReference();
        showImage = false;
        canSend = false;
        mapView();
        LinearLayoutManager myLayout= new LinearLayoutManager(getContext());
        myLayout.setStackFromEnd(true);
        recyclerView.setLayoutManager(myLayout);
        recyclerView.setHasFixedSize(true);
        setListenerView();
        getListComment();
        return view;
    }

    void mapView(){
        layoutImage = view.findViewById(R.id.image_container);
        image = view.findViewById(R.id.image_comment);
        recyclerView = view.findViewById(R.id.list_comment);
        camera = view.findViewById(R.id.button_camera);
        send = view.findViewById(R.id.button_send);
        send.setImageResource(R.drawable.ic_send);
        cancel = view.findViewById(R.id.button_cancel);
        comment = view.findViewById(R.id.txt_comment);
    }

    void setListenerView(){
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutImage.setVisibility(View.GONE);
                showImage = false;
                if (!comment.getText().toString().equals("") || showImage){
                    send.setImageResource(R.drawable.ic_send_active);
                    canSend = true;
                }
                else {
                    send.setImageResource(R.drawable.ic_send);
                    canSend = false;
                }
            }
        });
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                CommentFragment.this.startActivityForResult(Intent.createChooser(intent,"Chọn Ảnh"),SELECT_IMAGE_CODE);
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (canSend)
                sendComment();
                send.setImageResource(R.drawable.ic_send);
                canSend = false;
                layoutImage.setVisibility(View.GONE);
                showImage = false;
                comment.setText(null);
            }
        });

        comment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                String text = charSequence.toString();
                if (!text.trim().equals("") || showImage){
                    send.setImageResource(R.drawable.ic_send_active);
                    canSend = true;
                }
                else {
                    send.setImageResource(R.drawable.ic_send);
                    canSend = false;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    public void  setImage(Intent data){
        if (data != null){
            uri = data.getData();
            image.setImageURI(uri);
            layoutImage.setVisibility(View.VISIBLE);
            send.setImageResource(R.drawable.ic_send_active);
            canSend = true;
            showImage = true;
        }
    }


    void getListComment(){
        // get comment
        DatabaseReference comment = mReference.child("RecipeDetail").child(recipe.id).child("CommentRecipe");
        comment.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List <CommentRecipe> listComment = new ArrayList<>();
                for (DataSnapshot item: dataSnapshot.getChildren()){
                    CommentRecipe commentRecipe = new CommentRecipe();
                    try{
                        commentRecipe.imageName = item.child("ImageName").getValue().toString();
                        commentRecipe.imageType = item.child("ImageType").getValue().toString();
                    } catch (Exception e){
                        Log.e("CommentFragment Exception firebase", e.getMessage());
                    }
                    try{
                        commentRecipe.author.imageName = item.child("AuthorImageName").getValue().toString();
                        commentRecipe.author.imageType = item.child("AuthorImageType").getValue().toString();
                    } catch (Exception e){
                        Log.e("CommentFragment Exception firebase", e.getMessage());
                    }
                    commentRecipe.author.userName = item.child("AuthorName").getValue().toString();
                    commentRecipe.content = item.child("Content").getValue().toString();
                    commentRecipe.author.id = item.child("Author").getValue().toString();
                    listComment.add(commentRecipe);
                }
                ListCommentAdapter listCommentAdapter = new ListCommentAdapter(listComment,recipe.id);
                recyclerView.setAdapter(listCommentAdapter);
                // get author
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.e("MainActivity", "loadPost:onCancelled", databaseError.toException());
            }
        });

    }

    void sendComment (){

        DatabaseReference newComment  = mReference.child("RecipeDetail").child(recipe.id).child("CommentRecipe").push();
        String key =  newComment.getKey();
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("Author",user.id);
        hashMap.put("AuthorName",user.userName);
        hashMap.put("Content",comment.getText().toString());
        if (user.imageName != null){
            hashMap.put("AuthorImageName",user.imageName);
            hashMap.put("AuthorImageType",user.imageType);
        }
        if (showImage){
            hashMap.put("ImageName",key);
            String type = getExtension(uri);
            hashMap.put("ImageType",type);
            AGCStorageManagement storageManagement = AGCStorageManagement.getInstance();

            StorageReference reference = storageManagement.getStorageReference("ImageRecipe/" + recipe.id + "/Comment/" + key+"."+ type );
//            try {
//                final File localFile = File.createTempFile(recipe.imageName, recipe.imageType);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            //File file = new File(uri.getPath());
//            Bitmap bitmap = BitmapFactory.decodeFile(uri.getPath());
//
//            String type = getFileExtensionFromUrl(uri.getPath());
//            ByteArrayOutputStream stream = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 0, stream);
//            byte[] bitFile =  stream.toByteArray();
//            MimeTypeMap.getFileExtensionFromUrl(file.toString());
//            try
//            {
//                String path = uri.getPath();
//                FileOutputStream streamxxx = new FileOutputStream(path);
//                stream.write(array);
//            } catch (FileNotFoundException e1)
//            {
//                e1.printStackTrace();
//            }

            FileMetadata attribute = new FileMetadata();
            attribute.setContentType("image/"+type);
            String a =uri.toString();
            UploadTask task = reference.putFile(new File(uri.toString()),attribute);


            UploadTask tan = task;
            task.addOnFailureListener(new OnFailureListener(){
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.e("error aaaaaaaaaaaaaa",exception.getMessage());
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.UploadResult>(){
                @Override
                public void onSuccess(UploadTask.UploadResult uploadResult) {

                }
            }).addOnCompleteListener(new OnCompleteListener<UploadTask.UploadResult>() {
                @Override
                public void onComplete(Task<UploadTask.UploadResult> task) {
                    newComment.setValue(hashMap);
                }
            });
        }

    }

    public static String getFileExtensionFromUrl(String url) {
        if (!TextUtils.isEmpty(url)) {
            int fragment = url.lastIndexOf('#');
            if (fragment > 0) {
                url = url.substring(0, fragment);
            }

            int query = url.lastIndexOf('?');
            if (query > 0) {
                url = url.substring(0, query);
            }

            int filenamePos = url.lastIndexOf('/');
            String filename =
                    0 <= filenamePos ? url.substring(filenamePos + 1) : url;

            // if the filename contains special characters, we don't
            // consider it valid for our matching purposes:
            if (!filename.isEmpty() &&
                    Pattern.matches("[a-zA-Z_0-9\\.\\-\\(\\)\\%]+", filename)) {
                int dotPos = filename.lastIndexOf('.');
                if (0 <= dotPos) {
                    return filename.substring(dotPos + 1);
                }
            }
        }

        return "";
    }

    String getExtension(Uri url){
        ContentResolver cr = getContext().getContentResolver();
        MimeTypeMap mine = MimeTypeMap.getSingleton();
        return mine.getExtensionFromMimeType(cr.getType(url));
    }

//    public String getExtensionFromMimeType(String mimeType) {
//        return MimeUtils.guessExtensionFromMimeType(mimeType);
//    }


//    public void writeToFile(byte[] array)
//    {
//        try
//        {
//            String path = "/data/data/lalallalaa.txt";
//            FileOutputStream stream = new FileOutputStream(path);
//            stream.write(array);
//        } catch (FileNotFoundException e1)
//        {
//            e1.printStackTrace();
//        }
//    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CommentFragment.SELECT_IMAGE_CODE) {
            setImage(data);
        }
    }

}