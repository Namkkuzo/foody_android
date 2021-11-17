package com.example.foody.fragment;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.foody.R;
import com.example.foody.adapter.ListIngredientsAdapter;
import com.example.foody.adapter.ListRecipeAdapter;
import com.example.foody.helper.Contain;
import com.example.foody.helper.DatabaseLocal;
import com.example.foody.model.Ingredients;
import com.example.foody.model.Process;
import com.example.foody.model.Recipe;
import com.example.foody.model.RecipeDetail;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IngredientFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IngredientFragment extends Fragment {

    private String recipeId;

    public IngredientFragment(String recId) {
        this.recipeId = recId;
        // Required empty public constructor
    }

    View view;
    DatabaseReference mReference;
    private RecyclerView recyclerView;
    private ListIngredientsAdapter listIngredientsAdapter;
    List<Ingredients> ingredientsList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_ingredient, container, false);
        mapview();
        LinearLayoutManager myLayout= new LinearLayoutManager(getContext());
        myLayout.setStackFromEnd(true);
        recyclerView.setLayoutManager(myLayout);
        recyclerView.setHasFixedSize(true);
        getListIngredients(recipeId);
        return view;
    }

    void mapview(){
        recyclerView = view.findViewById( R.id.list_ingredients);
        mReference = FirebaseDatabase.getInstance(Contain.REALTIME_DATABASE).getReference();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ingredientsList = new ArrayList<Ingredients>();

    }

     void getListIngredients(String recipeId){
        RecipeDetail recipeDetail = new RecipeDetail();
        DatabaseReference mRecipeDetail = mReference.child("RecipeDetail");
        mRecipeDetail.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item : snapshot.getChildren()){
                    if(item.child("RecipeId").getValue().toString().equalsIgnoreCase(recipeId)){
                        String id = item.child("Id").getValue().toString();
                        DatabaseReference mRecipeDetailChild = mRecipeDetail.child(id);
                        DatabaseReference mIngredients = mRecipeDetailChild.child("Ingredients");
                        mIngredients.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot1) {
                                List<Ingredients> ingredientsList = new ArrayList<>();
                                for (DataSnapshot itemIng : snapshot1.getChildren()){
                                    Ingredients ingredients = new Ingredients();
                                    ingredients.setId(Integer.parseInt(itemIng.child("Id").getValue().toString()));
                                    ingredients.setImageName(itemIng.child("ImageName").getValue().toString());
                                    ingredients.setImageType(itemIng.child("ImageType").getValue().toString());
                                    ingredients.setName(itemIng.child("Name").getValue().toString());
                                    ingredients.setUnit(itemIng.child("Unit").getValue().toString());
                                    ingredients.setWeight(Integer.parseInt(itemIng.child("Weight").getValue().toString()));
                                    ingredientsList.add(ingredients);
                                }
                                listIngredientsAdapter = new ListIngredientsAdapter(ingredientsList,Contain.LIST_INGREDIENTS);
                                recyclerView.setAdapter(listIngredientsAdapter);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });




                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}