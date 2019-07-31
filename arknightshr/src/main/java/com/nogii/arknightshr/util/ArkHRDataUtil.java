package com.nogii.arknightshr.util;

import android.content.Context;
import android.nfc.Tag;
import android.text.TextUtils;
import android.util.Log;

import com.nogii.arknightshr.bean.CharacterBean;
import com.nogii.arknightshr.bean.ComposeResultBean;
import com.nogii.arknightshr.bean.TagBean;
import com.nogii.arknightshr.bean.TagGroupBean;
import com.nogii.arknightshr.data.ArkHRDataCacheManager;
import com.nogii.basesdk.util.AssetsUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ArkHRDataUtil {

    /**
     * 从assets里预设的数据初始化arkData
     * @param context
     * @param callBack
     */
    public static void initArkDataFromAssets(Context context, OnDataLoadStatusCallBack callBack) {
        ArkHRDataCacheManager arkHRDataCacheManager = ArkHRDataCacheManager.getInstance();

        String tagData = AssetsUtil.readStringFromAssest(context, "TagData.json");
        if (!TextUtils.isEmpty(tagData)) {
            try {
                JSONObject tagDataObj = new JSONObject(tagData);
                for (int i = 0; i < 6; i++) {
                    String tagDataItemKey = i + "";
                    JSONObject tagDataItemValue = tagDataObj.optJSONObject(tagDataItemKey);
                    TagGroupBean tagGroupBean = parseTagGroupBeanJson(tagDataItemValue);
                    arkHRDataCacheManager.tagGroupMap.put(tagGroupBean, (ArrayList<TagBean>) tagGroupBean.getChildren());
                    arkHRDataCacheManager.tagGroupBeans.add(tagGroupBean);
                    if (null != tagGroupBean.getChildren()) {
                        for (TagBean tagBean : tagGroupBean.getChildren()) {
                            arkHRDataCacheManager.tagNameMap.put(tagBean.getName(), tagBean);
                            arkHRDataCacheManager.tagIdMap.put(tagBean.getId(), tagBean);
                            arkHRDataCacheManager.tagList.add(tagBean);
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        String characterData = AssetsUtil.readStringFromAssest(context, "CharacterData.json");
        if (!TextUtils.isEmpty(characterData)) {
            try {
                JSONObject characterDataJson = new JSONObject(characterData);
                int count = characterDataJson.optInt("count");
                String date = characterDataJson.optString("date");
                JSONArray data = characterDataJson.optJSONArray("data");
                if (null != data) {
                    for (int i = 0; i < data.length(); i++) {
                        CharacterBean characterBean = parseCharacterBeanJson(data.optJSONObject(i));
                        arkHRDataCacheManager.characterStarMap.put(characterBean.getStar(), characterBean);
                        arkHRDataCacheManager.characterNameMap.put(characterBean.getName(), characterBean);
                        arkHRDataCacheManager.characterIdMap.put(characterBean.getId(), characterBean);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        arkHRDataCacheManager.isInitData = true;

        if (null != callBack) {
            callBack.onDataLoadFinished();
        }
    }

    /**
     * 根据选择的标签获取用于显示的列表
     * @param selectTags
     * @return
     */
    public static ArrayList<ComposeResultBean> getComposeResultBeans(ArrayList<TagBean> selectTags, ArrayList<TagBean> startTags) {

        ArkHRDataCacheManager arkHRDataCacheManager = ArkHRDataCacheManager.getInstance();

        // 最终结果集合
        ArrayList<ComposeResultBean> composeResultBeans = new ArrayList<>();

        // 组合tag集合
        ArrayList<ArrayList<TagBean>> tagComposeDatas = createTagCompose(selectTags);

        // 过滤星级集合
        ArrayList<Integer> starList = createStarIntegerList(startTags);

        // 遍历每一个标签组合集合
        for (int i = 0; i < tagComposeDatas.size(); i++) {

            ArrayList<TagBean> tagComposeData = tagComposeDatas.get(i);

            // ComposeResultBean : tag组合
            ComposeResultBean composeResultBean = new ComposeResultBean();
            composeResultBean.setTagBeans(tagComposeData);
            boolean isHaveHighStarTag = false;  // 是否含有高资tag
            for (int j = 0; j < tagComposeData.size(); j++) {
                if (tagComposeData.get(j).getId() == 103) {
                    isHaveHighStarTag =  true;
                    break;
                }
            }

            // ComposeResultBean : tag组合对应的Characters
            ArrayList<CharacterBean>[] tagComposeCharacterData = new ArrayList[tagComposeData.size()];
            for (int j = 0; j < tagComposeData.size(); j++) {
                TagBean tagBean = tagComposeData.get(j);
                tagComposeCharacterData[j] = arkHRDataCacheManager.tagCharacterMap.get(tagBean);
            }
            ArrayList<CharacterBean> characterBeans = getTagComposeCharacters(tagComposeCharacterData);
            composeResultBean.setCharacterBeans(characterBeans);

            // 不过滤六星，但是当前tag组合不含有高级资深则过滤六星characters
            if (starList.contains(6) && !isHaveHighStarTag) {
                java.util.Iterator<CharacterBean> characterBeanIterator = characterBeans.iterator();
                while(characterBeanIterator.hasNext()) {
                    if (characterBeanIterator.next().getStar() == 6) {
                        characterBeanIterator.remove();
                    }
                }
            }

            // 根据星级标签过滤characters
            if (startTags.size() == 0) {
                characterBeans.clear();
            } else if (starList.size() < 6) {
                java.util.Iterator<CharacterBean> characterBeanIterator = characterBeans.iterator();
                while(characterBeanIterator.hasNext()) {
                    CharacterBean characterBean = characterBeanIterator.next();
                    if (!starList.contains(characterBean.getStar())) {
                        characterBeanIterator.remove();
                    }
                }
            }

            Collections.sort(characterBeans);

            // 去除没有任何character或tag的列表
            if (composeResultBean.getTagBeans().size() == 0 || composeResultBean.getCharacterBeans().size() == 0) {

            } else {
                composeResultBeans.add(composeResultBean);
            }
        }

        return composeResultBeans;
    }

    public static ArrayList<Integer> createStarIntegerList(ArrayList<TagBean> startTags) {
        ArrayList<Integer> integerArrayList = new ArrayList<>();
        for (int i = 0; i < startTags.size(); i++) {
            integerArrayList.add((int) (startTags.get(i).getId() - 600));
        }
        return integerArrayList;
    }

    /**
     * 获取人物列表的交集
     * @param datas
     * @return
     */
    public static ArrayList<CharacterBean> getTagComposeCharacters(ArrayList<CharacterBean>... datas) {
        ArrayList<CharacterBean> resultData = new ArrayList<>();
        if (null != datas && datas.length > 0) {
            resultData.addAll(datas[0]);
            if (datas.length > 1) {
                for (int i = 1; i < datas.length; i++) {
                    resultData.retainAll(datas[i]);
                }
            }
        }
        return resultData;
    }

    /**
     * 根据选择的标签创建组合TAG
     *
     * @param selectTags
     * @return
     */
    public static ArrayList<ArrayList<TagBean>> createTagCompose(ArrayList<TagBean> selectTags) {
        ArrayList<ArrayList<TagBean>> arrayLists = new ArrayList<>();
        int nCnt = selectTags.size();
        int nBit = (0xFFFFFFFF >>> (32 - nCnt));
        for (int i = 1; i <= nBit; i++) {
            ArrayList<TagBean> tmp = new ArrayList<>();
            for (int j = 0; j < nCnt; j++) {
                if ((i << (31 - j)) >> 31 == -1) {
                    tmp.add(selectTags.get(j));
                }
            }
            arrayLists.add(tmp);
        }
        return arrayLists;
    }

    public static CharacterBean parseCharacterBeanJson(JSONObject characterBeanJson) {
        ArkHRDataCacheManager arkHRDataCacheManager = ArkHRDataCacheManager.getInstance();
        CharacterBean characterBean = new CharacterBean();
        characterBean.setId(characterBeanJson.optLong("id"));
        characterBean.setName(characterBeanJson.optString("name"));
        characterBean.setStar(characterBeanJson.optInt("star"));
        JSONArray tagChildren = characterBeanJson.optJSONArray("tag");
        if (null != tagChildren) {
            ArrayList<TagBean> tagBeans = new ArrayList<>();
            for (int i = 0; i < tagChildren.length(); i++) {
                long tagId = tagChildren.optJSONObject(i).optLong("id");
                TagBean tagBean = arkHRDataCacheManager.tagIdMap.get(tagId);

                ArrayList<CharacterBean> characterBeans = arkHRDataCacheManager.tagCharacterMap.get(tagBean);
                if (null == characterBeans) {
                    characterBeans = new ArrayList<>();
                    arkHRDataCacheManager.tagCharacterMap.put(tagBean, characterBeans);
                }
                characterBeans.add(characterBean);

                if (null == tagBean) {
                    tagBean = parseTagBeanJson(tagChildren.optJSONObject(i));
                    arkHRDataCacheManager.tagIdMap.put(tagBean.getId(), tagBean);
                    arkHRDataCacheManager.tagNameMap.put(tagBean.getName(), tagBean);
                    arkHRDataCacheManager.tagList.add(tagBean);
                }
                tagBeans.add(tagBean);

            }
            characterBean.setTag(tagBeans);
        }
        return characterBean;
    }

    /**
     * 解析标签类别的Json
     * @param tagGroupBeanJson
     * @return
     */
    public static TagGroupBean parseTagGroupBeanJson(JSONObject tagGroupBeanJson) {
        TagGroupBean tagGroupBean = new TagGroupBean();
        tagGroupBean.setId(tagGroupBeanJson.optLong("id"));
        tagGroupBean.setName(tagGroupBeanJson.optString("name"));
        JSONArray tagTypeChildren = tagGroupBeanJson.optJSONArray("children");
        if (null != tagTypeChildren) {
            ArrayList<TagBean> tagBeans = new ArrayList<>();
            for (int i = 0; i < tagTypeChildren.length(); i++) {
                tagBeans.add(parseTagBeanJson(tagTypeChildren.optJSONObject(i)));
            }
            tagGroupBean.setChildren(tagBeans);
        }
        return tagGroupBean;
    }

    /**
     * 解析标签的Json
     * @param tagBeanJson
     * @return
     */
    public static TagBean parseTagBeanJson(JSONObject tagBeanJson) {
        TagBean tagBean = new TagBean();
        tagBean.setId(tagBeanJson.optLong("id"));
        tagBean.setName(tagBeanJson.optString("name"));
        tagBean.setRare(tagBeanJson.optInt("rare"));
        return tagBean;
    }

    public interface OnDataLoadStatusCallBack {
        void onDataLoadFinished();
    }
}
