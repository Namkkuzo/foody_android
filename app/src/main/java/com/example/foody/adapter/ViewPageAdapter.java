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
import com.example.foody.model.User;

import java.util.ArrayList;

public class ViewPageAdapter extends FragmentStateAdapter {

    Recipe recipe;
    User user ;

    public ViewPageAdapter(@NonNull FragmentActivity fragmentActivity, Recipe recipe, User user) {
        super(fragmentActivity);
        this.recipe = recipe;
        this.user = user;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0: return new OverviewFragment(recipe.id);
            case 1: return new IngredientFragment(recipe.id);
            case 2: return new CommentFragment(recipe, user);
            default: return new OverviewFragment(recipe.id);
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }



}
