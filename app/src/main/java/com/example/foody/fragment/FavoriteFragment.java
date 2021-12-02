package com.example.foody.fragment;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.example.foody.R;
import com.example.foody.adapter.ListRecipeAdapter;
import com.example.foody.helper.Contain;
import com.example.foody.helper.DatabaseLocal;
import com.example.foody.model.Recipe;
import com.example.foody.model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.List;


public class FavoriteFragment extends Fragment {

    View view;
    DatabaseReference mReference;
    User user ;
    private RecyclerView recyclerView;
    private ListRecipeAdapter listRecipeAdapter;
    List<Recipe> listRecipe;
    public FavoriteFragment(User user) {
        // Required empty public constructor
        this.user = user;
    }
    public FavoriteFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        listRecipe = new ArrayList<Recipe>();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_favorite, menu);  // Use filter.xml from step 1
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete:
                boolean haveUpdate  = false;
                DatabaseLocal dbHelper = new DatabaseLocal(getContext());
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                for (String  id :listRecipeAdapter.getListPicked()) {
                    if(!id.equals("")){

                        DatabaseLocal.deleteRecipe(db,id);
                        haveUpdate = true;
                    }
                }
                if (!haveUpdate) {
                    DatabaseLocal.deleteRecipe(db,null);
                }
                getListRecipe();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_favorite, container, false);
        mapview();
        LinearLayoutManager myLayout= new LinearLayoutManager(getContext());
        myLayout.setStackFromEnd(false);
        recyclerView.setLayoutManager(myLayout);
        recyclerView.setHasFixedSize(true);
        getListRecipe();
        return view;
    }


    void mapview(){
        recyclerView = view.findViewById( R.id.list_favorite);
        mReference = FirebaseDatabase.getInstance(Contain.REALTIME_DATABASE).getReference();
    }


    public void getListRecipe(){
        DatabaseLocal dbHelper = new DatabaseLocal(getContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        listRecipe =  DatabaseLocal.getListRecipe(db);
        listRecipeAdapter = new ListRecipeAdapter(Contain.LIST_FAVORITE, user);
        recyclerView.setAdapter(listRecipeAdapter);
        listRecipeAdapter.newListData(listRecipe);
    }
}