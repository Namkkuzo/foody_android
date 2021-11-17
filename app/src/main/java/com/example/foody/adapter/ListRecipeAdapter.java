package com.example.foody.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foody.R;
import com.example.foody.model.Recipe;
import com.huawei.agconnect.cloud.storage.core.AGCStorageManagement;
import com.huawei.agconnect.cloud.storage.core.DownloadTask;
import com.huawei.agconnect.cloud.storage.core.StorageReference;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ListRecipeAdapter extends RecyclerView.Adapter<ListRecipeAdapter.ViewHolder>  implements  View.OnLongClickListener{

    private List<Recipe> data;

    public ListRecipeAdapter( List<Recipe> recipeList) {
        this.data = recipeList;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public ListRecipeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_item_list, parent, false);
        return new ListRecipeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListRecipeAdapter.ViewHolder holder, int position) {
        final Recipe recipe = data.get(position);
        AGCStorageManagement storageManagement = AGCStorageManagement.getInstance();
        StorageReference reference = storageManagement.getStorageReference("ImageRecipe/" + recipe.id+"/"+ recipe.imageName +"."+recipe.imageType);
        try {
            final File localFile = File.createTempFile(recipe.imageName,recipe.imageType);
            reference.getFile(localFile).addOnSuccessListener(downloadResult -> {
                Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                Log.e("ListRecipeAdapter","get image "+ position + "Success");
                holder.imageRecipe.setImageBitmap(bitmap);
            }).addOnFailureListener(e -> {
                Log.e("ListRecipeAdapter","get image "+ position + "fail");
            });

        } catch (IOException e) {
            e.printStackTrace();
        }


        holder.title.setText(recipe.title);
        holder.summary.setText(recipe.summary);

        holder.favorite.setText(new Integer (recipe.totalLike).toString());

        holder.time.setText(new Integer( recipe.totalTime).toString());



        if (recipe.vegan){
            holder.vegan.setTextColor(Color.rgb(0,200,83));
            holder.iconVegan.setImageResource(R.drawable.ic_vegan_active);
        }
        else {
            holder.vegan.setTextColor(Color.rgb(14,29,37));
            holder.iconVegan.setImageResource(R.drawable.ic_vegan);
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public boolean onLongClick(View v) {
        Log.e("ListRecipeAdapter","long click");
        return false;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageRecipe, iconVegan;
        public TextView title, summary, favorite, vegan, time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageRecipe = itemView.findViewById(R.id.image_list_recipe);
            iconVegan = itemView.findViewById(R.id.ic_vegan);
            title = itemView.findViewById(R.id.title_recipe_list);
            summary = itemView.findViewById(R.id.summary_recipe_list);
            time = itemView.findViewById(R.id.total_time);
            favorite = itemView.findViewById(R.id.total_like);
            vegan = itemView.findViewById(R.id.vegan);
        }
    }
}
