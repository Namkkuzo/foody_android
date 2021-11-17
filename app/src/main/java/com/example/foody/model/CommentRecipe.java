package com.example.foody.model;

public class CommentRecipe {
    public User author;
    public String content;
    public String imageType;
    public String imageName;
    public  CommentRecipe(){
        author = new User();
    }
}
