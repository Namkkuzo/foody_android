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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.huawei.agconnect.cloud.storage.core.AGCStorageManagement;
import com.huawei.agconnect.cloud.storage.core.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class FavoriteFragment extends Fragment {

    public FavoriteFragment() {
        // Required empty public constructor
    }

    View view;
    DatabaseReference mReference;
    private RecyclerView recyclerView;
    private ListRecipeAdapter listRecipeAdapter;
    List<Recipe> listRecipe;


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
        myLayout.setStackFromEnd(true);
        recyclerView.setLayoutManager(myLayout);
        recyclerView.setHasFixedSize(true);
        return view;
    }


    void mapview(){
        recyclerView = view.findViewById( R.id.list_favorite);
        mReference = FirebaseDatabase.getInstance(Contain.REALTIME_DATABASE).getReference();
    }


    void getListRecipe(){
        DatabaseLocal dbHelper = new DatabaseLocal(getContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        listRecipe =  DatabaseLocal.getListRecipe(db);
        listRecipeAdapter = new ListRecipeAdapter(listRecipe);
        recyclerView.setAdapter(listRecipeAdapter);
    }
}