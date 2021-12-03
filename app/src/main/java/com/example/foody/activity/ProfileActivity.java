package com.example.foody.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;


import com.example.foody.R;

public class ProfileActivity extends AppCompatActivity {

    private ImageView backIcon;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_profile);
        setSupportActionBar(toolbar);
    }

    void setActionbar(){
        getSupportActionBar().hide();
        backIcon = findViewById(R.id.ic_back);

        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("Click back icon ", "100");
                finish();
            }
        });


    }
}