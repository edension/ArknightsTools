package com.nogii.arknightshr.bean;

import android.support.annotation.NonNull;

import java.util.ArrayList;

public class ComposeResultBean implements Comparable<ComposeResultBean> {

    /**
     * tag组合集
     */
    private ArrayList<TagBean> tagBeans;

    /**
     * tag组合集对应的人物集
     */
    private ArrayList<CharacterBean> characterBeans;


    public ArrayList<TagBean> getTagBeans() {
        return tagBeans;
    }

    public void setTagBeans(ArrayList<TagBean> tagBeans) {
        this.tagBeans = tagBeans;
    }

    public ArrayList<CharacterBean> getCharacterBeans() {
        return characterBeans;
    }

    public void setCharacterBeans(ArrayList<CharacterBean> characterBeans) {
        this.characterBeans = characterBeans;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("tags name : ");
        for (int i = 0; i < tagBeans.size(); i++) {
            stringBuilder.append(tagBeans.get(i).getName() + "--");
        }
        stringBuilder.append("  characters Name : ");
        for (int i = 0; i < characterBeans.size(); i++) {
            stringBuilder.append(characterBeans.get(i).getName() + "--");
        }
        return stringBuilder.toString();
    }

    @Override
    public int compareTo(@NonNull ComposeResultBean o) {
        return characterBeans.size() - o.characterBeans.size();
    }
}
