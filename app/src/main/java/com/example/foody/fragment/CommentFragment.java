package com.example.foody.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.foody.R;
import com.example.foody.adapter.ListCommentAdapter;
import com.example.foody.helper.Contain;
import com.example.foody.model.CommentRecipe;
import com.example.foody.model.Recipe;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class CommentFragment extends Fragment {

    public static int SELECT_IMAGE_CODE = 1;

    boolean showImage;

    ImageButton camera, send, cancel;
    ImageView image ;
    RecyclerView recyclerView;
    ConstraintLayout layoutImage ;
    EditText comment;
    List<CommentRecipe> listComment;
    View view;
    Recipe recipe;
    DatabaseReference mReference;


    public CommentFragment(Recipe recipe) {
        this.recipe = recipe;
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_comment, container, false);
        mReference = FirebaseDatabase.getInstance(Contain.REALTIME_DATABASE).getReference();
        showImage = false;
        mapView();
        listComment = new ArrayList<>();
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
                }
                else {
                    send.setImageResource(R.drawable.ic_send);
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
                }
                else {
                    send.setImageResource(R.drawable.ic_send);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    public void  setImage(Intent data){

        Uri uri = data.getData();
        image.setImageURI(uri);
        layoutImage.setVisibility(View.VISIBLE);
        send.setImageResource(R.drawable.ic_send_active);
        showImage = true;
    }


    void getListComment(){
        // get comment
        DatabaseReference comment = mReference.child("RecipeDetail").child(recipe.id).child("CommentRecipe");
        comment.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CommentFragment.SELECT_IMAGE_CODE) {
            setImage(data);
        }
    }

}