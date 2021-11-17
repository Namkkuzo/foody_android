package com.example.foody.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

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


public class CommentFragment extends Fragment {

    public static int SELECT_IMAGE_CODE = 1;

    boolean showImage;

    ImageButton camera, send, cancel;
    ImageView image ;
    ConstraintLayout layoutImage ;
    EditText comment;
    View view;


    public CommentFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_comment, container, false);
        showImage = false;
        mapView();
        setListenerView();
        return view;
    }

    void mapView(){
        layoutImage = view.findViewById(R.id.image_container);
        image = view.findViewById(R.id.image_comment);
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CommentFragment.SELECT_IMAGE_CODE) {
            setImage(data);
        }
    }

}