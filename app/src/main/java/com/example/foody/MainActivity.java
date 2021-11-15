package com.example.foody;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.foody.Fragment.FavoriteFragment;
import com.example.foody.Fragment.RecipeFragment;
import com.example.foody.Model.User;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.huawei.agconnect.auth.AGConnectAuth;

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
        user = getUser("");
    }


    User getUser (String id){
        return new User();
    }


    void mapview (){
        viewPager = findViewById(R.id.Main_viewpager);
        tabLayout = findViewById(R.id.main_tablayout);
        mReference = FirebaseDatabase.getInstance().getReference();
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