package com.example.foody.fragment;

import android.content.Intent;
import android.os.Bundle;

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
import com.example.foody.model.Recipe;
import com.example.foody.R;
import com.example.foody.model.User;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.huawei.agconnect.auth.AGConnectAuth;

import java.util.ArrayList;
import java.util.List;


public class RecipeFragment extends Fragment {


    public RecipeFragment() {
        // Required empty public constructor
    }

    View view;
    private RecyclerView recyclerView;
    private ListRecipeAdapter  listRecipeAdapter;
    private List<Recipe> listRecipe;
    DatabaseReference databaseReference;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
        listRecipe = new ArrayList<Recipe>();
        readChat();
        return view;
    }


    void mapview(){
        recyclerView = view.findViewById( R.id.list_recipe);
    }


    private void readChat(){}
}