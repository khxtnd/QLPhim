package com.quanlyphim.model;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Film implements Serializable {
    private Integer id;
    private String name;
    private String category;
    private String image;
    private Integer rate;
    private Integer categoryId;


    public Film(String name, String category, String image, Integer rate, Integer categoryId) {
        this.name = name;
        this.category = category;
        this.image = image;
        this.rate = rate;
        this.categoryId = categoryId;
    }


    public Film(Integer id, String name, String category, String image, Integer rate, Integer categoryId) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.image = image;
        this.rate = rate;
        this.categoryId = categoryId;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getImage() {
        return image;
    }

    public Integer getRate() {
        return rate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}

