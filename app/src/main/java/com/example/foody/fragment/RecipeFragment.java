package com.example.foody.fragment;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.foody.activity.LoginActivity;
import com.example.foody.adapter.ListRecipeAdapter;
import com.example.foody.helper.Contain;
import com.example.foody.helper.DatabaseLocal;
import com.example.foody.model.Recipe;
import com.example.foody.R;
import com.example.foody.model.User;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.huawei.agconnect.auth.AGConnectAuth;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class RecipeFragment extends Fragment {

    View view;
    User user ;
    DatabaseReference mReference;
    private RecyclerView recyclerView;
    private ListRecipeAdapter  listRecipeAdapter;
    List<Recipe> listRecipe;
    DatabaseLocal dbHelper ;
    SQLiteDatabase db ;

    public RecipeFragment(User user) {
        // Required empty public constructor
        this.user = user;
    }

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
                showBottomSheetDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showBottomSheetDialog() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
        bottomSheetDialog.setContentView(R.layout.filter);

        LinearLayout copy = bottomSheetDialog.findViewById(R.id.copyLinearLayout);
        LinearLayout share = bottomSheetDialog.findViewById(R.id.shareLinearLayout);
        LinearLayout upload = bottomSheetDialog.findViewById(R.id.uploadLinearLaySout);
        LinearLayout download = bottomSheetDialog.findViewById(R.id.download);
        LinearLayout delete = bottomSheetDialog.findViewById(R.id.delete);


        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "Share is Clicked", Toast.LENGTH_LONG).show();
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.show();
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view= inflater.inflate(R.layout.fragment_recipe, container, false);
        mapview();
        LinearLayoutManager myLayout= new LinearLayoutManager(getContext());
        myLayout.setStackFromEnd(false);
        recyclerView.setLayoutManager(myLayout);
        recyclerView.setHasFixedSize(true);
        listRecipeAdapter = new ListRecipeAdapter(Contain.LIST_RECIPE, user);
        recyclerView.setAdapter(listRecipeAdapter);
        getListRecipe();
        return view;
    }


    void mapview(){
        recyclerView = view.findViewById( R.id.list_recipe);
        mReference = FirebaseDatabase.getInstance(Contain.REALTIME_DATABASE).getReference();

    }


    void getListRecipe(){
        DatabaseReference mRecipe = mReference.child("RecipeDetail");
//        mRecipe.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot item : snapshot.getChildren()){
//                    Recipe recipe = new Recipe();
//                    recipe.id = item.child("Id").getValue().toString();
//                    recipe.imageName = item.child("ImageName").getValue().toString();
//                    recipe.imageType = item.child("ImageType").getValue().toString();
//                    recipe.totalLike =  Integer.parseInt(item.child("Like").getValue().toString());
//                    recipe.totalTime =  Integer.parseInt(item.child("TotalTime").getValue().toString());
//                    recipe.summary = item.child("Summary").getValue().toString();
//                    recipe.title = item.child("Title").getValue().toString();
//                    recipe.vegan =(boolean) item.child("Vegan").getValue();
//                    listRecipe.add(recipe);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
        mRecipe.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Recipe recipe = new Recipe();
                recipe.id = snapshot.child("Id").getValue().toString();
                recipe.imageName = snapshot.child("ImageName").getValue().toString();
                recipe.imageType = snapshot.child("ImageType").getValue().toString();
                recipe.totalLike =  Integer.parseInt(snapshot.child("Like").getValue().toString());
                recipe.totalTime =  Integer.parseInt(snapshot.child("TotalTime").getValue().toString());
                recipe.summary = snapshot.child("Summary").getValue().toString();
                recipe.title = snapshot.child("Title").getValue().toString();
                recipe.vegan =(boolean) snapshot.child("Vegan").getValue();
                listRecipe.add(recipe);
                listRecipeAdapter.newData(recipe);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}