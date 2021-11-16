package com.example.foody.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.foody.activity.LoginActivity;
import com.example.foody.adapter.ListRecipeAdapter;
import com.example.foody.helper.Contain;
import com.example.foody.model.Recipe;
import com.example.foody.R;
import com.example.foody.model.User;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.huawei.agconnect.auth.AGConnectAuth;

import java.util.ArrayList;
import java.util.List;


public class RecipeFragment extends Fragment {


    public RecipeFragment() {
        // Required empty public constructor
    }

    View view;
    DatabaseReference mReference;
    private RecyclerView recyclerView;
    private ListRecipeAdapter  listRecipeAdapter;
    List<Recipe> listRecipe;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        listRecipe = new ArrayList<Recipe> ();
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_recipe, menu);  // Use filter.xml from step 1
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                AGConnectAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
                return true;
            case R.id.button_filter:
                return true;
            case R.id.button_search:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view= inflater.inflate(R.layout.fragment_recipe, container, false);
        mapview();
        LinearLayoutManager myLayout= new LinearLayoutManager(getContext());
        myLayout.setStackFromEnd(true);
        recyclerView.setLayoutManager(myLayout);
        recyclerView.setHasFixedSize(true);
        getListRecipe();
        return view;
    }


    void mapview(){
        recyclerView = view.findViewById( R.id.list_recipe);
        mReference = FirebaseDatabase.getInstance(Contain.REALTIME_DATABASE).getReference();
    }


    void getListRecipe(){
        DatabaseReference mRecipe = mReference.child("Recipe");
        mRecipe.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item : snapshot.getChildren()){
                    Recipe recipe = new Recipe();
                    recipe.id = item.child("Id").getValue().toString();
                    recipe.imageName = item.child("ImageName").getValue().toString();
                    recipe.imageType = item.child("ImageType").getValue().toString();
                    recipe.totalLike =  Integer.parseInt(item.child("Like").getValue().toString());
                    recipe.totalTime =  Integer.parseInt(item.child("TotalTime").getValue().toString());
                    recipe.summary = item.child("Summary").getValue().toString();
                    recipe.title = item.child("Title").getValue().toString();
                    recipe.vegan =(boolean) item.child("Vegan").getValue();
                    listRecipe.add(recipe);
                }
                listRecipeAdapter = new ListRecipeAdapter(listRecipe);
                recyclerView.setAdapter(listRecipeAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}