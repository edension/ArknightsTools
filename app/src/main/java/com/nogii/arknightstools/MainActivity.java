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
import com.nogii.arknightshr.adapter.ComposeResultAdapter;
import com.nogii.arknightshr.bean.ComposeResultBean;
import com.nogii.arknightshr.bean.TagBean;
import com.nogii.arknightshr.bean.TagGroupBean;
import com.nogii.arknightshr.data.ArkHRDataCacheManager;
import com.nogii.arknightshr.util.ArkHRDataUtil;
import com.nogii.basesdk.thread.ThreadPoolExecutorProxyFactory;
import com.nogii.basesdk.util.RecyclerViewUtil;
import com.nogii.basesdk.util.UIUtil;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "ArknightsTools";

    private ViewGroup tagGroup;

    private RecyclerView resultList;
    private ComposeResultAdapter resultAdapter;

    private ArrayList<BaseCardAdapter> baseCardAdapters = new ArrayList<>();
    private BaseCardAdapter startTypeAdapter;

    private ArrayList<TagBean> selectTags = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        tagGroup = findViewById(R.id.hr_tag_layout_tags_root);

        resultList = findViewById(R.id.hr_result_list);
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

        ViewGroup clearGroup = findViewById(R.id.hr_main_clear_layout);
        TextView clearTV = clearGroup.findViewById(R.id.card_item_name);
        CardView clearCard = clearGroup.findViewById(R.id.card_item_root);
        clearTV.setText("清空");
        clearCard.setCardBackgroundColor(Color.GRAY);
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
                resultAdapter.getData().clear();
                resultAdapter.notifyDataSetChanged();
            }
        });

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
                    resultAdapter.setData(composeResultBeans);
                    resultAdapter.notifyDataSetChanged();
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
}
