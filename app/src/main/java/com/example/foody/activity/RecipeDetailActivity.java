package com.example.foody.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.foody.R;
import com.example.foody.adapter.ViewPageAdapter;
import com.example.foody.fragment.CommentFragment;
import com.example.foody.fragment.IngredientFragment;
import com.example.foody.fragment.OverviewFragment;
import com.example.foody.model.Recipe;
import com.example.foody.model.User;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.List;

public class RecipeDetailActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private  Recipe recipe ;
    private  boolean formLocal;
    User user;
    private ImageView backIcon , favoriteIcon ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        getData();
        formLocal = false;
        tabLayout = findViewById(R.id.tabRecipeDetail);
        viewPager2 = findViewById(R.id.viewPagerDetail);
        ViewPageAdapter adapter = new ViewPageAdapter(this, recipe, user);
        viewPager2.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position){
                    case 0: tab.setText("Overview"); break;
                    case 1: tab.setText("Ingren"); break;
                    case 2: tab.setText("Comment"); break;
                }
            }
        }).attach();
        setActionbar();

//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                int id = tab.getId();
//                if(id == R.id.overview){
//                    viewPager2.setCurrentItem(0);
//                }else if(id == R.id.ingredients){
//                    viewPager2.setCurrentItem(1);
//                }else if(id == R.id.comment){
//                    viewPager2.setCurrentItem(2);
//                }
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
    }




    void setActionbar(){
        getSupportActionBar().hide();
        backIcon = findViewById(R.id.ic_back);
        favoriteIcon = findViewById(R.id.ic_favorite);

        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(100);
                Log.e("Click back icon ", "100");
                finish();
            }
        });
        favoriteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(200);
                Log.e("Click favorite icon ", "200");
                Log.e("resultFormMainActivity", recipe.id);
            }
        });

    }

    private void onOptionsItemSelected() {
    }

    void getData(){
        recipe = new Recipe();
        user = new User();
        Intent result  = getIntent();
        if (result.hasExtra("RecipeId")){
            Log.e("resultFormMainActivity", result.getStringExtra("RecipeId"));
            recipe.id = result.getStringExtra("RecipeId");
        }
        if (result.hasExtra("ImageType")){
            user.imageType = result.getStringExtra("ImageType");
        }
        if (result.hasExtra("ImageName")){
            user.imageName = result.getStringExtra("ImageName");
        }
        if (result.hasExtra("UserName")){
            user.userName = result.getStringExtra("UserName");
        }
        if (result.hasExtra("UserID")){
            user.id = result.getStringExtra("UserID");
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if(fragment instanceof CommentFragment) {
                    fragment.onActivityResult(requestCode, resultCode, data);
                }
            }
        }


        super.onActivityResult(requestCode, resultCode, data);
    }
}