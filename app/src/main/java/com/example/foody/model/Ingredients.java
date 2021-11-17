package com.example.foody.model;

public class Ingredients {
    private String imageName;
    private String imageType;
    private String name;
    private String unit;
    private String weight;

    public Ingredients() {
    }

    public Ingredients(String imageName, String imageType, String name, String unit, String weight) {
        this.imageName = imageName;
        this.imageType = imageType;
        this.name = name;
        this.unit = unit;
        this.weight = weight;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
}
