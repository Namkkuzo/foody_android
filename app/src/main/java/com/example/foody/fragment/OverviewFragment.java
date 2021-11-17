package com.example.foody.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.foody.R;
import com.example.foody.helper.Contain;
import com.example.foody.model.Ingredients;
import com.example.foody.model.Process;
import com.example.foody.model.RecipeDetail;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class OverviewFragment extends Fragment {

    View view;
    DatabaseReference mReference;
    TextView btnDescription;

    public OverviewFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }

    }

    private void loadView(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_overview, container, false);
        mReference = FirebaseDatabase.getInstance(Contain.REALTIME_DATABASE).getReference();
        getRecipeDetailByReID("test1");


        return view;
    }

    public RecipeDetail getRecipeDetailByReID(String recipeId){
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
                                    ingredients.setImageName(itemIng.child("ImageName").getValue().toString());
                                    ingredients.setImageType(itemIng.child("ImageType").getValue().toString());
                                    ingredients.setName(itemIng.child("ImageType").getValue().toString());
                                    ingredients.setUnit(itemIng.child("Unit").getValue().toString());
                                    ingredients.setWeight(Integer.parseInt(itemIng.child("Weight").getValue().toString()));
                                    ingredientsList.add(ingredients);
                                }
                                recipeDetail.setIngredientsList(ingredientsList);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        DatabaseReference mProcess = mRecipeDetailChild.child("Process");
                        mProcess.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                List<Process> processList = new ArrayList<>();
                                for (DataSnapshot itemProcess : snapshot.getChildren()){
                                    Process process = new Process();
                                    process.setAction(itemProcess.child("Action").getValue().toString());
                                    process.setStep(Integer.parseInt(itemProcess.child("Step").toString()));
                                    processList.add(process);
                                }
                                recipeDetail.setProcessList(processList);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        recipeDetail.setCheap((boolean) item.child("Cheap").getValue());
                        recipeDetail.setDairyFree((boolean) item.child("DairyFree").getValue());
                        recipeDetail.setDescription(item.child("Description").getValue().toString());
                        recipeDetail.setGlutentFree((boolean) item.child("GlutentFree").getValue());
                        recipeDetail.setGlutentFree((boolean) item.child("Healthy").getValue());
                        recipeDetail.setId(item.child("Id").getValue().toString());
                        recipeDetail.setImageName(item.child("ImageName").getValue().toString());
                        recipeDetail.setImageType(item.child("ImageType").getValue().toString());
                        recipeDetail.setRecipeId(item.child("RecipeId").getValue().toString());
                        recipeDetail.setSummary(item.child("Summary").getValue().toString());
                        recipeDetail.setTotalTime(Integer.parseInt(item.child("TotalTime").getValue().toString()));
                        recipeDetail.setVegan((boolean) item.child("Vegan").getValue());
                        recipeDetail.setVegetarian((boolean) item.child("Vegetarian").getValue());

                        btnDescription = (TextView) view.findViewById(R.id.btnDescription);
                        btnDescription.setText(recipeDetail.getDescription());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return recipeDetail;
    }
}