package com.example.foody.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.foody.fragment.CommentFragment;
import com.example.foody.fragment.IngredientFragment;
import com.example.foody.fragment.OverviewFragment;

import java.util.ArrayList;

public class ViewPageAdapter extends FragmentStateAdapter {

    private String recipeId;

    public ViewPageAdapter(@NonNull FragmentActivity fragmentActivity, String reciId) {
        super(fragmentActivity);
        this.recipeId = reciId;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0: return new OverviewFragment(recipeId);
            case 1: return new IngredientFragment(recipeId);
            case 2: return new CommentFragment();
            default: return new OverviewFragment(recipeId);
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }



}
