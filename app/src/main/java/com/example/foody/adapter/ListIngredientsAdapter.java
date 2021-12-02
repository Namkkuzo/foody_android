package com.example.foody.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foody.R;
import com.example.foody.activity.RecipeDetailActivity;
import com.example.foody.helper.Contain;
import com.example.foody.model.Ingredients;
import com.example.foody.model.Recipe;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListIngredientsAdapter extends RecyclerView.Adapter<ListIngredientsAdapter.ViewHolder>  {

//    public interface OnBindCallback {
//        void onViewBound(ListRecipeAdapter.ViewHolder viewHolder, int position);
//    }

    private final List<Ingredients> data;
    Context mContext ;
    String recipeId;
    public ListIngredientsAdapter(List<Ingredients> ingredients, String id ) {
        this.data = ingredients;
        recipeId = id;
    }


    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public ListIngredientsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredients_item_list, parent, false);
        mContext = parent.getContext();
        return new ListIngredientsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListIngredientsAdapter.ViewHolder holder, int position) {
        final Ingredients ingredients = data.get(position);
//        FirebaseStorage storage = FirebaseStorage.getInstance();
//        StorageReference storageReference = storage.getReference();
//        StorageReference reference = storageReference.child("ImageRecipe/" + recipeId + "/Ingredients/"+ingredients.getId() + ingredients.getImageName() + "." + ingredients.getImageType());
//        try {
//            final File localFile = File.createTempFile(ingredients.getImageName(), ingredients.getImageType());
//            reference.getFile(localFile).addOnSuccessListener(downloadResult -> {
//                Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
//                ingredients.setImageBitmap(bitmap);
//                listIngredientsAdapter.notifyDataSetChanged();
//            }).addOnFailureListener(e -> {
//                Log.e("ListRecipeAdapter", " get image  fail");
//            });
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }



        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        StorageReference reference = storageReference.child("ImageRecipe/" + recipeId + "/Ingredients/"+ ingredients.getImageName() + "." + ingredients.getImageType());
        try {
            final File localFile = File.createTempFile(ingredients.getImageName(), ingredients.getImageType());
            reference.getFile(localFile).addOnSuccessListener(downloadResult -> {
                Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                Log.e("ListRecipeAdapter", " get image " + position + "Success");
                holder.imageIngredients.setImageBitmap(bitmap);
            }).addOnFailureListener(e -> {
                Log.e("ListRecipeAdapter", " get image " + position + "fail");
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

        holder.title.setText(ingredients.getName());
        holder.weight.setText(ingredients.getWeight()+ingredients.getUnit());


//        int index = position;
//        holder.layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void  onClick(View view) {
//                Drawable  viewColor =  holder.layout.getBackground();
//                if (type == Contain.LIST_FAVORITE){
//                    if(((ColorDrawable) viewColor).getColor()== Color.parseColor("#ffffff") && totalPick>0 ){
//                        holder.layout.setBackgroundColor(Color.parseColor("#efe8ff"));
//                        totalPick ++;
//                        picked.set(index, recipe.id);
//                    }else {
//                        holder.layout.setBackgroundColor(Color.parseColor("#ffffff"));
//                        picked.set(index, "");
//                        totalPick--;
//                    }
//                }else{
//                    //go to detail in here
//                    Intent detail = new Intent(view.getContext(), RecipeDetailActivity.class);
//                    detail.putExtra("RecipeId", recipe.id);
//                    ((Activity)mContext).startActivityForResult(detail,type);
//                }
//                notifyDataSetChanged();
//            }
//        });

//        holder.layout.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                Drawable  viewColor =  holder.layout.getBackground();
//                if (type == Contain.LIST_FAVORITE) {
//                    int color = ((ColorDrawable) viewColor).getColor();
//                    if( color== Color.parseColor("#ffffff") && totalPick==0){
//                        holder.layout.setBackgroundColor(Color.parseColor("#efe8ff"));
//                        picked.set(index,recipe.id);
//                        totalPick++;
//                    }
//                }
//                return true;
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageIngredients;
        public TextView title, weight;
        public ConstraintLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageIngredients = itemView.findViewById(R.id.image_list_ingredients);
            layout = itemView.findViewById(R.id.item_ingre);
            title = itemView.findViewById(R.id.title_ingredients);
            weight = itemView.findViewById(R.id.weight_ingre);

        }
    }
}
