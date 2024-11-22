package com.quanlyphim.model;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Category implements Serializable {
    private Integer id;
    private String name;
    private String desc;

    public Category(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public Category(Integer id, String name, String desc) {
        this.id = id;
        this.name = name;
        this.desc = desc;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
