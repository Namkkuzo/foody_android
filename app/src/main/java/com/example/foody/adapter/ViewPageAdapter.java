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
import com.example.foody.model.Recipe;

import java.util.ArrayList;

public class ViewPageAdapter extends FragmentStateAdapter {

    Recipe recipe;

    public ViewPageAdapter(@NonNull FragmentActivity fragmentActivity, Recipe recipe) {
        super(fragmentActivity);
        this.recipe = recipe;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0: return new OverviewFragment();
            case 1: return new IngredientFragment();
            case 2: return new CommentFragment(recipe);
            default: return new OverviewFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }



}
