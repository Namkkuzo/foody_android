package com.example.foody.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.foody.R;
import com.example.foody.fragment.FavoriteFragment;
import com.example.foody.fragment.RecipeFragment;
import com.example.foody.helper.Contain;
import com.example.foody.model.User;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    ViewPager viewPager;
    TabLayout tabLayout;
    DatabaseReference mReference;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapview();
        getUser();
    }


    void getListRecipe(){

    }

    void  getUser (){
        Intent result = getIntent();
        String idUser = "";
        if (result.hasExtra("UserId")){
            idUser = result.getStringExtra("UserId");
        }
        DatabaseReference profile = mReference.child("User").child(idUser).child("Profile");
        user.id =  idUser;
        profile.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user.userName =  dataSnapshot.child("UserName").getValue().toString();
                user.email =  dataSnapshot.child("E-mail").getValue().toString();
                user.picture =  dataSnapshot.child("Picture").getValue().toString();
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