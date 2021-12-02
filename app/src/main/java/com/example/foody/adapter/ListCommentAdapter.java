package com.example.foody.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foody.R;
import com.example.foody.model.CommentRecipe;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ListCommentAdapter extends RecyclerView.Adapter<ListCommentAdapter.ViewHolder> {

    List<CommentRecipe> data;
    String recipeId ;

    public ListCommentAdapter(List<CommentRecipe> data, String recipeId) {
        this.data = data;
        this.recipeId = recipeId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item_list, parent, false);
        return new ListCommentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final  CommentRecipe  comment = data.get(position);
        holder.author.setText(comment.author.userName);
        holder.content.setText(comment.content);
        if (comment.content.equals("")){
            holder.content.setVisibility(View.GONE);
            Log.e("ListRecipeAdapter", "get content gone");
        }
        else {
            Log.e("ListRecipeAdapter", "get content VISIBLE");
            holder.content.setVisibility(View.VISIBLE);
        }
        if (comment.imageName != null) {
            try {
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageReference = storage.getReference();
                StorageReference reference = storageReference.child("ImageRecipe/" + recipeId + "/Comment/" + comment.imageName + "." + comment.imageType);
                final File localFileAvatar = File.createTempFile(comment.imageName, comment.imageType);
                reference.getFile(localFileAvatar).addOnSuccessListener(downloadResult -> {
                    Bitmap bitmapAvatar = BitmapFactory.decodeFile(localFileAvatar.getAbsolutePath());
                    Log.e("ListRecipeAdapter", " get image " + comment.imageName + " Success");

                    holder.image.setImageBitmap(bitmapAvatar);
                }).addOnFailureListener(e -> {
                    Log.e("ListRecipeAdapter", " get image " + comment.imageName + " fail");
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            holder.image.setVisibility(View.GONE);
        }

        if (comment.author.imageName != null) {
            try {
                final String name = comment.author.imageName ;
                final String type = comment.author.imageType;
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageReference = storage.getReference();
                StorageReference reference = storageReference.child("ImageProfile/" + comment.author.imageName + "." + comment.author.imageType);
                final File localFile = File.createTempFile(name, type);
                reference.getFile(localFile).addOnSuccessListener(downloadResult -> {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    Log.e("ListRecipeAdapter", " get image profile " + comment.imageName + " Success");
                    holder.avatar.setImageBitmap(bitmap);
                }).addOnFailureListener(e -> {
                    Log.e("ListRecipeAdapter", " get image profile " + comment.imageName + " fail");
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setItems(List<CommentRecipe> listComment) {
        this.data = listComment;
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
