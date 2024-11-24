package com.quanlyphim.model;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Film implements Serializable {
    private Integer id;
    private String name = "";
    private String category = "";
    private String desc = "";
    private String image = "";
    private Integer evaluate = 0;
    private Integer categoryId = 0;

    public Film() {
    }

    public Film(String name, String category, String desc, String image, Integer evaluate, Integer categoryId) {
        this.name = name;
        this.category = category;
        this.desc = desc;
        this.image = image;
        this.evaluate = evaluate;
        this.categoryId = categoryId;
    }


    @NonNull
    @Override
    public String toString() {
        return name;
    }

    public Film(Integer id, String name, String category, String desc, String image, Integer evaluate, Integer categoryId) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.category = category;
        this.image = image;
        this.evaluate = evaluate;
        this.categoryId = categoryId;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getEvaluate() {
        return evaluate;
    }

    public void setEvaluate(Integer evaluate) {
        this.evaluate = evaluate;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
}

