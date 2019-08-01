package com.nogii.arknightstools;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.nogii.arknightshr.adapter.BaseCardAdapter;
import com.nogii.arknightshr.adapter.BaseCardViewHolder;
import com.nogii.arknightshr.adapter.ComposeResultAdapter;
import com.nogii.arknightshr.adapter.ComposeResultNewViewHolder;
import com.nogii.arknightshr.bean.CharacterBean;
import com.nogii.arknightshr.bean.ComposeResultBean;
import com.nogii.arknightshr.bean.TagBean;
import com.nogii.arknightshr.bean.TagGroupBean;
import com.nogii.arknightshr.data.ArkHRDataCacheManager;
import com.nogii.arknightshr.util.ArkHRDataUtil;
import com.nogii.basesdk.thread.ThreadPoolExecutorProxyFactory;
import com.nogii.basesdk.ui.activity.BaseActivity;
import com.nogii.basesdk.util.RecyclerViewUtil;
import com.nogii.basesdk.util.UIUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

public class MainActivity extends BaseActivity {

    private static final String TAG = "ArknightsTools";

    private ViewGroup tagGroup;

    private RecyclerView resultList;
    private ComposeResultAdapter resultAdapter;

    private ViewGroup resultGroup;

    private TextView clearTV;

    private ArrayList<BaseCardAdapter> baseCardAdapters;
    private BaseCardAdapter startTypeAdapter;

    private ArrayList<TagBean> selectTags;

    private ArrayList<ComposeResultBean> emptyComposeList;

    @Override
    protected void initView() {
        super.initView();
        setContentView(R.layout.activity_main);
        tagGroup = findViewById(R.id.hr_tag_layout_tags_root);
        resultGroup = findViewById(R.id.hr_result_area);
        resultList = findViewById(R.id.hr_result_list);
        initClearView();
    }

    private void initClearView() {
        ViewGroup clearGroup = findViewById(R.id.hr_main_clear_layout);
        clearTV = clearGroup.findViewById(R.id.card_item_name);
        CardView clearCard = clearGroup.findViewById(R.id.card_item_root);
        clearTV.setText("清空");
        clearCard.setCardBackgroundColor(Color.GRAY);
    }

    @Deprecated
    private void initResultRecyclerView() {
        resultGroup.setVisibility(View.GONE);
        resultList.setVisibility(View.VISIBLE);
        ChipsLayoutManager chipsLayoutManager = ChipsLayoutManager.newBuilder(this)
                .setChildGravity(Gravity.LEFT)
                .setMaxViewsInRow(Integer.MAX_VALUE)
                .setScrollingEnabled(true)
                .build();
        resultList.setLayoutManager(chipsLayoutManager);
        // 用在于当知道Adapter内Item的改变不会影响RecyclerView宽高的时候，可以设置为true让RecyclerView避免重新计算大小。
        resultList.setHasFixedSize(true);
        // 这个是在处理滑动卡顿时常用的，牵扯到时间分发和手势
        resultList.setNestedScrollingEnabled(true);
        // 设置子视图的缓存处理大小
        resultList.setItemViewCacheSize(600);
        // recycledViewPool重新给定义个新的存放视图的pool
        RecyclerView.RecycledViewPool recycledViewPool = new RecyclerView.RecycledViewPool();
        resultList.setRecycledViewPool(recycledViewPool);
        resultAdapter = new ComposeResultAdapter(this);
        resultList.setAdapter(resultAdapter);
    }

    @Override
    protected void initData() {
        super.initData();
        baseCardAdapters = new ArrayList<>();
        parentDetachedViewList = new Stack<>();
        tagChildDetachedViewList = new Stack<>();
        characterChildDetachedViewList = new Stack<>();
        selectTags = new ArrayList<>();
        emptyComposeList = new ArrayList<>();
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        clearTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectTags.clear();
                for (int i = 0; i < baseCardAdapters.size(); i++) {
                    if (baseCardAdapters.get(i) == startTypeAdapter) {
                        continue;
                    }
                    baseCardAdapters.get(i).getSelectData().clear();
                    baseCardAdapters.get(i).notifyDataSetChanged();
                }
                if (null != resultAdapter) {
                    resultAdapter.getData().clear();
                    resultAdapter.notifyDataSetChanged();
                } else {
                    recycleComposeLayout(emptyComposeList);
                }
            }
        });
    }

    @Override
    protected void initFinished() {
        super.initFinished();
        if (ArkHRDataCacheManager.getInstance().isInitData) {
            onArkDataLoadFinished();
        } else {
            ThreadPoolExecutorProxyFactory.getNormalPoolExecutorProxy().execute(new Runnable() {
                @Override
                public void run() {
                    ArkHRDataUtil.initArkDataFromAssets(MainActivity.this, new ArkHRDataUtil.OnDataLoadStatusCallBack() {
                        @Override
                        public void onDataLoadFinished() {
                            UIUtil.postTaskSafely(new Runnable() {
                                @Override
                                public void run() {
                                    onArkDataLoadFinished();
                                }
                            });
                        }
                    });
                }
            });
        }
    }

    /**
     * arkData加载完毕
     */
    private void onArkDataLoadFinished() {
        ArkHRDataCacheManager arkHRDataCacheManager = ArkHRDataCacheManager.getInstance();

        for (int i = 0; i < arkHRDataCacheManager.tagGroupBeans.size(); i++) {
            View tagItem = LayoutInflater.from(MainActivity.this).inflate(R.layout.hr_tag_item, null, false);
            TextView tagTypeNameTV = tagItem.findViewById(R.id.hr_tag_item_name);
            RecyclerView tagRecyclerView = tagItem.findViewById(R.id.hr_tag_item_list);
            tagRecyclerView.setNestedScrollingEnabled(false);

            TagGroupBean tagGroupBean = arkHRDataCacheManager.tagGroupBeans.get(i);

            tagTypeNameTV.setText(tagGroupBean.getName());
            final BaseCardAdapter baseCardAdapter = new BaseCardAdapter(MainActivity.this);
            ArrayList<TagBean> currentGroupList = arkHRDataCacheManager.tagGroupMap.get(tagGroupBean);
            final boolean isStarType = currentGroupList.size() > 0 && currentGroupList.get(0).getId() >= 601 &&
                    currentGroupList.get(0).getId() <= 606;
            baseCardAdapter.setIgnoreSelectLimit(isStarType);
            baseCardAdapter.setData(currentGroupList);
            if (isStarType) {
                startTypeAdapter = baseCardAdapter;
                baseCardAdapter.getSelectData().addAll(currentGroupList);
            }


            baseCardAdapter.setCardAdapterInterface(new BaseCardAdapter.CardAdapterInterface() {
                @Override
                public void onCardItemClick(View view, Object obj, boolean add, boolean isIgnoreClickLimit) {
                    if (!isIgnoreClickLimit) {
                        if (add) {
                            selectTags.add((TagBean) obj);
                        } else {
                            selectTags.remove(obj);
                        }
                    }
                    ArrayList<ComposeResultBean> composeResultBeans = ArkHRDataUtil.getComposeResultBeans(selectTags, startTypeAdapter.getSelectData());
                    Collections.sort(composeResultBeans);

                    createComposeLayout(composeResultBeans);
                    //resultAdapter.setData(composeResultBeans);
                    //resultAdapter.notifyDataSetChanged();
                }

                @Override
                public int getSelectTagCount() {
                    int totalCount = 0;
                    for (int i = 0; i < baseCardAdapters.size(); i++) {
                        if (baseCardAdapters.get(i).isIgnoreSelectLimit()) {
                            continue;
                        }
                        totalCount += baseCardAdapters.get(i).getSelectData().size();
                    }
                    return totalCount;
                }
            });
            tagRecyclerView.setAdapter(baseCardAdapter);
            baseCardAdapters.add(baseCardAdapter);
            tagRecyclerView.setLayoutManager(RecyclerViewUtil.getFlowLayoutManager(MainActivity.this, false));

            tagGroup.addView(tagItem);
        }
    }


    private Stack<View> parentDetachedViewList;
    private Stack<View> tagChildDetachedViewList;
    private Stack<View> characterChildDetachedViewList;

    /**
     * 创建组合视图
     * @param composeResultBeans
     */
    public void createComposeLayout(ArrayList<ComposeResultBean> composeResultBeans) {
        resultGroup.setVisibility(View.VISIBLE);
        resultList.setVisibility(View.GONE);

        recycleComposeLayout(composeResultBeans);

        for (int i = 0; i < composeResultBeans.size(); i++) {
            boolean isNeedAddView = false;

            ArrayList<CharacterBean> characterBeans = composeResultBeans.get(i).getCharacterBeans();
            ArrayList<TagBean> tagBeans = composeResultBeans.get(i).getTagBeans();

            View parentView = i < resultGroup.getChildCount() && resultGroup.getChildCount() > 0 ? resultGroup.getChildAt(i) : null;
            if (null == parentView) {
                isNeedAddView = true;
                parentView = getComposeView(parentDetachedViewList, R.layout.compose_result_new_item);
            }

            ComposeResultNewViewHolder composeResultNewViewHolder = null;
            if (null == parentView.getTag()) {
                composeResultNewViewHolder = new ComposeResultNewViewHolder(parentView);
                parentView.setTag(composeResultNewViewHolder);
            } else {
                composeResultNewViewHolder = (ComposeResultNewViewHolder) parentView.getTag();
            }
            composeResultNewViewHolder.composeResultBeans = composeResultBeans.get(i);
            ViewGroup tagsGroup = composeResultNewViewHolder.tagList;
            ViewGroup charactersGroup = composeResultNewViewHolder.characterList;

            initBaseCardViewGroup(tagBeans, tagChildDetachedViewList, tagsGroup);
            initBaseCardViewGroup(characterBeans, characterChildDetachedViewList, charactersGroup);

            if (isNeedAddView) {
                resultGroup.addView(parentView);
            }
        }
    }

    /**
     * 回收组合视图
     * @param composeResultBeans
     */
    private void recycleComposeLayout(ArrayList<ComposeResultBean> composeResultBeans) {

        int startIndex = 0, endIndex = composeResultBeans.size();

        if (composeResultBeans.size() > resultGroup.getChildCount()) {
            endIndex = 0;
        } else if (composeResultBeans.size() < resultGroup.getChildCount()) {
            endIndex = resultGroup.getChildCount();
            startIndex = composeResultBeans.size();
        }

        if (endIndex != 0) {
            for (int i = endIndex - 1; i >= startIndex; i--) {
                View groupView = resultGroup.getChildAt(i);
                ComposeResultNewViewHolder composeResultNewViewHolder = (ComposeResultNewViewHolder) groupView.getTag();
                ViewGroup tagsGroup = composeResultNewViewHolder.tagList;
                ViewGroup charactersGroup = composeResultNewViewHolder.characterList;

                if (i < composeResultBeans.size()) {
                    recycleComposeChildLayout(composeResultBeans.get(i).getTagBeans(), tagsGroup,
                            tagChildDetachedViewList);
                    recycleComposeChildLayout(composeResultBeans.get(i).getCharacterBeans(), charactersGroup,
                            characterChildDetachedViewList);
                }

                parentDetachedViewList.push(groupView);
                resultGroup.removeView(groupView);
            }
        }
    }

    private void recycleComposeChildLayout(ArrayList data, ViewGroup parentGroup, Stack childStack) {
        int startIndex = 0, endIndex = data.size();

        if (data.size() > parentGroup.getChildCount()) {
            endIndex = 0;
        } else if (data.size() < parentGroup.getChildCount()) {
            endIndex = parentGroup.getChildCount();
            startIndex = data.size();
        }

        if (endIndex != 0) {
            for (int j = endIndex - 1; j >= startIndex; j--) {
                View childView = parentGroup.getChildAt(j);
                childStack.push(childView);
                parentGroup.removeView(childView);
            }
        }
    }

    /**
     * 初始化组合视图中的baseCardViewGroup
     * @param baseCardBeans
     * @param detachedList
     * @param parentGroup
     */
    private void initBaseCardViewGroup(ArrayList baseCardBeans, Stack<View> detachedList, ViewGroup parentGroup) {
        int startIndex = 0, endIndex = baseCardBeans.size();
        if (parentGroup.getChildCount() < baseCardBeans.size()) {
            endIndex = parentGroup.getChildCount();
            for (int j = parentGroup.getChildCount(); j < baseCardBeans.size(); j++) {
                boolean isNeedAddView = false;

                View tagAddView = null;
                tagAddView = j < parentGroup.getChildCount() && parentGroup.getChildCount() > 0 ? parentGroup.getChildAt(j) : null;
                if (null == tagAddView) {
                    isNeedAddView = true;
                    tagAddView = getComposeView(detachedList, R.layout.base_card_item);
                }
                BaseCardViewHolder baseCardViewHolder = null;
                if (null == tagAddView.getTag()) {
                    baseCardViewHolder = new BaseCardViewHolder(tagAddView);
                    tagAddView.setTag(baseCardViewHolder);
                    Object obj = baseCardBeans.get(j);
                    if (obj instanceof TagBean) {
                        ((TagBean) obj).setSelected(false);
                    } else if (obj instanceof CharacterBean) {
                        ((CharacterBean) obj).setSelected(false);
                    }
                }
                baseCardViewHolder.bindCardViewHolder(baseCardBeans.get(j), false);

                if (isNeedAddView) {
                    parentGroup.addView(tagAddView);
                }
            }
        } else if (parentGroup.getChildCount() > baseCardBeans.size()) {
            for (int j = parentGroup.getChildCount() - 1; j >= baseCardBeans.size(); j--) {
                parentGroup.removeView(parentGroup.getChildAt(j));
            }
        }
        if (endIndex > 0) {
            for (int j = startIndex; j < endIndex; j++) {
                BaseCardViewHolder baseCardViewHolder = (BaseCardViewHolder) parentGroup.getChildAt(j).getTag();
                baseCardViewHolder.bindCardViewHolder(baseCardBeans.get(j), false);
            }
        }
    }

    /**
     * 获取组合view
     * @param popList
     * @param layoutResId
     * @return
     */
    private View getComposeView(Stack<View> popList, int layoutResId) {
        View composeView = popParentView(popList);
        if (null == composeView) {
            composeView = LayoutInflater.from(this).inflate(layoutResId, null, false);
        }
        return composeView;
    }

    /**
     * 从缓存栈中获取detached的view
     * @param popList
     * @return
     */
    private View popParentView(Stack<View> popList) {
        View popView = null;
        if (null != popList && !popList.empty()) {
            popView = popList.pop();
        }
        return popView;
    }
}
