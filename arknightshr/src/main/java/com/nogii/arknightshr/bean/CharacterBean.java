package com.nogii.arknightshr.bean;

import android.support.annotation.NonNull;

import java.util.List;

public class CharacterBean implements Comparable<CharacterBean> {

    /**
     * name : 食铁兽
     * id : 15
     * star : 5
     * tag : [{"id":505,"name":"减速","rare":2},{"id":202,"name":"近战位","rare":1},{"id":509,"name":"位移","rare":2},{"id":102,"name":"资深干员","rare":3},{"id":408,"name":"特种干员","rare":2},{"id":302,"name":"女性干员","rare":1}]
     */

    private String name;
    private long id;
    private int star;
    private List<TagBean> tag;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public List<TagBean> getTag() {
        return tag;
    }

    public void setTag(List<TagBean> tag) {
        this.tag = tag;
    }

    @Override
    public int compareTo(@NonNull CharacterBean o) {
        return o.star - star;
    }
}
