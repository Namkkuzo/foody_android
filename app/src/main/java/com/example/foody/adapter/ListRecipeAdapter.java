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
import com.example.foody.model.Recipe;
import com.example.foody.model.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListRecipeAdapter extends RecyclerView.Adapter<ListRecipeAdapter.ViewHolder> {
    List<Recipe> data = new ArrayList<>();
    Context mContext;
    User user;
    private final int type;
    List<String> picked = new ArrayList<>();
    int totalPick;


    public ListRecipeAdapter(int type, User user) {
        this.type = type;
        this.user = user;
        totalPick = 0;
    }

    public void newData(Recipe data) {
        this.data.add(data);
        picked.add("");
        notifyDataSetChanged();
    }
    public void newListData(List<Recipe> data) {
        this.data = data;
        for (int i= 0;i<data.size();i++){
            picked.add("");
        }
        notifyDataSetChanged();
    }

    public List<String> getListPicked() {
        return picked;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public ListRecipeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_item_list, parent, false);
        mContext = parent.getContext();
        return new ListRecipeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListRecipeAdapter.ViewHolder holder, int position) {
        final Recipe recipe = data.get(position);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        StorageReference reference = storageReference.child("ImageRecipe/" + recipe.id + "/" + recipe.imageName + "." + recipe.imageType);
        if (type == Contain.LIST_RECIPE) {
            try {
                final File localFile = File.createTempFile(recipe.imageName, recipe.imageType);
                reference.getFile(localFile).addOnSuccessListener(downloadResult -> {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    Log.e("ListRecipeAdapter", " get image " + position + "Success");
                    holder.imageRecipe.setImageBitmap(bitmap);
                }).addOnFailureListener(e -> {
                    Log.e("ListRecipeAdapter", " get image " + position + "fail");
                });

            } catch (IOException e) {
                Log.e("ListRecipeAdapter", "false");
                e.printStackTrace();
            }
        } else {
            holder.imageRecipe.setImageBitmap(recipe.imageBitmap);
        }

        if (picked.get(position).equals(recipe.id)) {
            holder.layout.setBackgroundColor(Color.parseColor("#efe8ff"));
        } else {
            holder.layout.setBackgroundColor(Color.parseColor("#ffffff"));
        }


        holder.title.setText(recipe.title);
        holder.summary.setText(recipe.summary);

        holder.favorite.setText(Integer.toString(recipe.totalLike));

        holder.time.setText(Integer.toString(recipe.totalTime));


        if (recipe.vegan) {
            holder.vegan.setTextColor(Color.rgb(0, 200, 83));
            holder.iconVegan.setImageResource(R.drawable.ic_vegan_active);
        } else {
            holder.vegan.setTextColor(Color.rgb(14, 29, 37));
            holder.iconVegan.setImageResource(R.drawable.ic_vegan);
        }
        int index = position;
        holder.layout.setOnClickListener(view -> {
            Drawable viewColor = holder.layout.getBackground();
            if (type == Contain.LIST_FAVORITE) {
                if (((ColorDrawable) viewColor).getColor() == Color.parseColor("#ffffff") && totalPick > 0) {
                    holder.layout.setBackgroundColor(Color.parseColor("#efe8ff"));
                    totalPick++;
                    picked.set(index, recipe.id);
                } else {
                    holder.layout.setBackgroundColor(Color.parseColor("#ffffff"));
                    picked.set(index, "");
                    if (totalPick>0)
                    totalPick--;
                    else {
                        Intent detail = new Intent(view.getContext(), RecipeDetailActivity.class);
                        detail.putExtra("RecipeId", recipe.id);
                        detail.putExtra("ImageType", user.imageType);
                        detail.putExtra("ImageName", user.imageName);
                        detail.putExtra("UserName", user.userName);
                        detail.putExtra("UserID", user.id);
                        ((Activity) mContext).startActivityForResult(detail, type);
                    }
                }
            } else {
                //go to detail in here
                Intent detail = new Intent(view.getContext(), RecipeDetailActivity.class);
                detail.putExtra("RecipeId", recipe.id);
                detail.putExtra("ImageType", user.imageType);
                detail.putExtra("ImageName", user.imageName);
                detail.putExtra("UserName", user.userName);
                detail.putExtra("UserID", user.id);
                ((Activity) mContext).startActivityForResult(detail, type);
            }
            notifyItemChanged(position);
        });

        holder.layout.setOnLongClickListener(v -> {
            Drawable viewColor = holder.layout.getBackground();
            if (type == Contain.LIST_FAVORITE) {
                int color = ((ColorDrawable) viewColor).getColor();
                if (color == Color.parseColor("#ffffff") && totalPick == 0) {
                    holder.layout.setBackgroundColor(Color.parseColor("#efe8ff"));
                    picked.set(index, recipe.id);
                    totalPick++;
                }
            }
            return true;
        });

    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageRecipe, iconVegan;
        public TextView title, summary, favorite, vegan, time;
        public ConstraintLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageRecipe = itemView.findViewById(R.id.image_list_recipe);
            iconVegan = itemView.findViewById(R.id.ic_vegan);
            layout = itemView.findViewById(R.id.item_container);
            title = itemView.findViewById(R.id.title_recipe_list);
            summary = itemView.findViewById(R.id.summary_recipe_list);
            time = itemView.findViewById(R.id.total_time);
            favorite = itemView.findViewById(R.id.total_like);
            vegan = itemView.findViewById(R.id.vegan);
        }
    }
}
