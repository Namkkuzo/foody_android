package com.example.foody.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.foody.R;
import com.example.foody.adapter.ListRecipeAdapter;
import com.example.foody.fragment.FavoriteFragment;
import com.example.foody.fragment.RecipeFragment;
import com.example.foody.helper.Contain;
import com.example.foody.model.Recipe;
import com.example.foody.model.User;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.huawei.agconnect.auth.AGConnectAuth;
import com.huawei.agconnect.auth.AGConnectUser;

public class MainActivity extends AppCompatActivity {



    ViewPager viewPager;
    TabLayout tabLayout;
    DatabaseReference mReference;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCompat.requestPermissions(this, permissions, 1);
        setContentView(R.layout.activity_main);
        user = new User();
        mapview();
        getUser();
    }
    private String[] permissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Contain.LIST_RECIPE){
            Log.e("resultActivityPop", Integer.toString(resultCode));
        }
        if(requestCode == Contain.LIST_FAVORITE){
            Log.e("resultActivityPop", Integer.toString(resultCode));
        }
    }

    void  getUser (){

        AGConnectUser currentUser = AGConnectAuth.getInstance().getCurrentUser();
        String userId = currentUser.getUid();
        user.id = userId;
        DatabaseReference profile = mReference.child("User").child(userId).child("Profile");
        profile.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user.userName =  dataSnapshot.child("UserName").getValue().toString();
                user.email =  currentUser.getEmail();
                if (dataSnapshot.child("ImageName").exists())
                user.imageName =  dataSnapshot.child("ImageName").getValue().toString();
                if (dataSnapshot.child("ImageType").exists())
                user.imageType =  dataSnapshot.child("ImageType").getValue().toString();
                Log.e("MainActivity", "get user success");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.e("MainActivity", "loadPost:onCancelled", databaseError.toException());
            }
        });

    }


    void mapview (){
        viewPager = findViewById(R.id.Main_viewpager);
        tabLayout = findViewById(R.id.main_tablayout);
        mReference = FirebaseDatabase.getInstance(Contain.REALTIME_DATABASE).getReference();
        viewPager.setAdapter(new ScreenSlidePagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_baseline_menu_book_24);
        tabLayout.getTabAt(0).setText("Món ăn");
        tabLayout.getTabAt(1).setText("Yêu thích");
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_baseline_star_24);
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        final int  PAGE_NUMBER = 2;
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0)
            return new RecipeFragment();
            else return new FavoriteFragment();
        }

        @Override
        public int getCount() {
            return PAGE_NUMBER;
        }
    }

}