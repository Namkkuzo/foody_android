package com.example.foody.model;

import java.util.List;

public class RecipeDetail {

    private String id;
    private boolean cheap;
    private boolean dairyFree;
    private String description;
    private boolean glutentFree;
    private boolean healthy;
    private String imageName;
    private String imageType;
    private List<Process> processList;
    private List<Ingredients> ingredientsList;
    private String summary;
    private String title;
    private String totalTime;
    private boolean vegan;
    private boolean vegetarian;

    public RecipeDetail() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isCheap() {
        return cheap;
    }

    public void setCheap(boolean cheap) {
        this.cheap = cheap;
    }

    public boolean isDairyFree() {
        return dairyFree;
    }

    public void setDairyFree(boolean dairyFree) {
        this.dairyFree = dairyFree;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isGlutentFree() {
        return glutentFree;
    }

    public void setGlutentFree(boolean glutentFree) {
        this.glutentFree = glutentFree;
    }

    public boolean isHealthy() {
        return healthy;
    }

    public void setHealthy(boolean healthy) {
        this.healthy = healthy;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public List<Process> getProcessList() {
        return processList;
    }

    public void setProcessList(List<Process> processList) {
        this.processList = processList;
    }

    public List<Ingredients> getIngredientsList() {
        return ingredientsList;
    }

    public void setIngredientsList(List<Ingredients> ingredientsList) {
        this.ingredientsList = ingredientsList;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public boolean isVegan() {
        return vegan;
    }

    public void setVegan(boolean vegan) {
        this.vegan = vegan;
    }

    public boolean isVegetarian() {
        return vegetarian;
    }

    public void setVegetarian(boolean vegetarian) {
        this.vegetarian = vegetarian;
    }

    public RecipeDetail(String id, boolean cheap, boolean dairyFree, String description, boolean glutentFree, boolean healthy, String imageName, String imageType, List<Process> processList, List<Ingredients> ingredientsList, String summary, String title, String totalTime, boolean vegan, boolean vegetarian) {
        this.id = id;
        this.cheap = cheap;
        this.dairyFree = dairyFree;
        this.description = description;
        this.glutentFree = glutentFree;
        this.healthy = healthy;
        this.imageName = imageName;
        this.imageType = imageType;
        this.processList = processList;
        this.ingredientsList = ingredientsList;
        this.summary = summary;
        this.title = title;
        this.totalTime = totalTime;
        this.vegan = vegan;
        this.vegetarian = vegetarian;
    }
}
