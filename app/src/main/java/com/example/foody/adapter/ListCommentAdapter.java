package com.example.foody.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.example.foody.helper.Contain;
import com.example.foody.model.CommentRecipe;
import com.huawei.agconnect.cloud.storage.core.AGCStorageManagement;
import com.huawei.agconnect.cloud.storage.core.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ListCommentAdapter extends RecyclerView.Adapter<ListCommentAdapter.ViewHolder> {

    private final List<CommentRecipe> data;
    Context mContext ;

    public ListCommentAdapter(List<CommentRecipe> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_item_list, parent, false);
        return new ListCommentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final  CommentRecipe  comment = data.get(position);
        holder.author.setText(comment.author.userName);
        holder.content.setText(comment.content);
//        if (!comment.author.imageName.equals("")) {
//            try {
//                AGCStorageManagement storageManagement = AGCStorageManagement.getInstance();
//                StorageReference reference = storageManagement.getStorageReference("ImageRecipe/" + recipe.id + "/" + recipe.imageName + "." + recipe.imageType);
//                final File localFile = File.createTempFile(recipe.imageName, recipe.imageType);
//                reference.getFile(localFile).addOnSuccessListener(downloadResult -> {
//                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
//                    Log.e("ListRecipeAdapter", " get image " + position + "Success");
//                    holder.imageRecipe.setImageBitmap(bitmap);
//                }).addOnFailureListener(e -> {
//                    Log.e("ListRecipeAdapter", " get image " + position + "fail");
//                });
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        } else {
//            holder.imageRecipe.setImageBitmap(recipe.imageBitmap);
//        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView avatar, image;
        public TextView content, author;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.item_avatar_comment);
            image = itemView.findViewById(R.id.item_image_comment);
            content = itemView.findViewById(R.id.item_content_comment);
            author = itemView.findViewById(R.id.item_user_name_comment);
        }
    }
}
