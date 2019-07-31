package com.nogii.arknightshr.bean;

import android.support.annotation.NonNull;

public class TagBean implements Comparable<TagBean> {

    private long id;
    private String name;
    private int rare;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRare() {
        return rare;
    }

    public void setRare(int rare) {
        this.rare = rare;
    }

    @Override
    public int compareTo(@NonNull TagBean o) {
        return this.id > o.getId() ? 1 : -1;
    }
}
