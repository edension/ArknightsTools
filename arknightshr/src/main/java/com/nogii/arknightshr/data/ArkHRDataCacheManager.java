package com.nogii.arknightshr.data;

import com.nogii.arknightshr.bean.CharacterBean;
import com.nogii.arknightshr.bean.TagBean;
import com.nogii.arknightshr.bean.TagGroupBean;

import java.util.ArrayList;
import java.util.HashMap;

public class ArkHRDataCacheManager {

    private static ArkHRDataCacheManager arkHRDataCacheManager;

    public static  synchronized ArkHRDataCacheManager getInstance() {
        if (null == arkHRDataCacheManager) {
            arkHRDataCacheManager = new ArkHRDataCacheManager();
        }
        return arkHRDataCacheManager;
    }

    public boolean isInitData = false;


    // =========================== tag

    /**
     * TagName/Id->TagBean map
     */
    public HashMap<String, TagBean> tagNameMap = new HashMap<>();
    public HashMap<Long, TagBean> tagIdMap = new HashMap<>();
    public ArrayList<TagBean> tagList = new ArrayList<>();

    public HashMap<TagGroupBean, ArrayList<TagBean>> tagGroupMap = new HashMap<>();
    public ArrayList<TagGroupBean> tagGroupBeans = new ArrayList<>();

    // =========================== character

    /**
     * TagBean->Character map
     */
    public HashMap<TagBean, ArrayList<CharacterBean>> tagCharacterMap = new HashMap<>();

    /**
     * CharacterStar ->Character map
     */
    public HashMap<Integer, CharacterBean> characterStarMap = new HashMap<>();

    /**
     * CharacterName/Id->TagBean map
     */
    public HashMap<String, CharacterBean> characterNameMap = new HashMap<>();
    public HashMap<Long, CharacterBean> characterIdMap = new HashMap<>();

    public void clearArkHRDataCache() {
        isInitData = false;
        tagCharacterMap.clear();
        tagNameMap.clear();
        tagIdMap.clear();
        tagList.clear();
        characterNameMap.clear();
        characterIdMap.clear();
        characterStarMap.clear();
        tagGroupMap.clear();
        tagGroupBeans.clear();
    }
}
