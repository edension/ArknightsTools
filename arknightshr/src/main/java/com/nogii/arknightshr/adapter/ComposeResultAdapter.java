package com.nogii.arknightshr.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nogii.arknightshr.R;
import com.nogii.arknightshr.bean.CharacterBean;
import com.nogii.arknightshr.bean.ComposeResultBean;
import com.nogii.basesdk.util.RecyclerViewUtil;
import com.zhy.view.flowlayout.FlowLayout;

import java.util.ArrayList;
import java.util.Iterator;

public class ComposeResultAdapter extends RecyclerView.Adapter<ComposeResultAdapter.ComposeResultViewHolder> {

    private ArrayList<ComposeResultBean> data;

    private Context mContext;

    private BaseCardAdapter fakeBaseCardAdapter;

    private ArrayList<View> detachedViewList = new ArrayList<>();

    public ComposeResultAdapter(Context context) {
        mContext = context;
        fakeBaseCardAdapter = new BaseCardAdapter(mContext);
    }

    @Override
    public ComposeResultViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = null;
        ComposeResultViewHolder itemViewHolder = null;
        itemView = LayoutInflater.from(mContext).inflate(R.layout.compose_result_item, viewGroup, false);
        itemViewHolder = new ComposeResultViewHolder(itemView);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder( ComposeResultViewHolder composeResultViewHolder, int i) {
        ComposeResultBean composeResultBean = getItem(i);
        composeResultViewHolder.tagAdapter.setData(composeResultBean.getTagBeans());
        composeResultViewHolder.tagAdapter.notifyDataSetChanged();

        ArrayList<CharacterBean> characterBeans = composeResultBean.getCharacterBeans();
        composeResultViewHolder.characterListAdapter.setData(characterBeans);
        composeResultViewHolder.characterListAdapter.notifyDataSetChanged();

        // TODO : 只有一个character的高亮效果
        //int itemViewBackground = composeResultBean.getCharacterBeans().size() == 1 && composeResultBean.getCharacterBeans().get(0).getStar() >= 5
        //        ? R.drawable.yellow_corner_rect : 0;
        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        //    composeResultViewHolder.itemView.setBackground(itemViewBackground == 0 ? null : mContext.getDrawable(itemViewBackground));
        //}
    }

    @Override
    public int getItemCount() {
        return null == data ? 0 : data.size();
    }

    public ComposeResultBean getItem(int position) {
        return null != data ? data.get(position) : null;
    }

    public ArrayList<ComposeResultBean> getData() {
        return data;
    }

    public void setData(ArrayList<ComposeResultBean> data) {
        this.data = data;
    }

    public ArrayList<View> getDetachedViewList() {
        return detachedViewList;
    }

    public void setDetachedViewList(ArrayList<View> detachedViewList) {
        this.detachedViewList = detachedViewList;
    }

    class ComposeResultViewHolder extends RecyclerView.ViewHolder {

        View itemView;
        RecyclerView tagList;
        // FlowLayout characterList;
        RecyclerView characterList;
        BaseCardAdapter tagAdapter;
        BaseCardAdapter characterListAdapter;

        public ComposeResultViewHolder( View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.itemView.setTag(this);
            Context context = itemView.getContext();

            tagList = itemView.findViewById(R.id.compose_result_item_tags);
            tagAdapter = new BaseCardAdapter(context);
            tagList.setAdapter(tagAdapter);
            tagList.setLayoutManager(RecyclerViewUtil.getFlowLayoutManager(context, false));
            tagList.setNestedScrollingEnabled(false);
            tagList.setItemViewCacheSize(200);

            characterList = itemView.findViewById(R.id.compose_result_item_characters);
            characterListAdapter = new BaseCardAdapter(context);
            characterList.setAdapter(characterListAdapter);
            characterList.setLayoutManager(RecyclerViewUtil.getFlowLayoutManager(context, false));
            characterList.setNestedScrollingEnabled(false);
            characterList.setItemViewCacheSize(600);
            characterList.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        }

        public View createCharacterListView(ViewGroup root) {
            View itemView = LayoutInflater.from(mContext).inflate(R.layout.base_card_item, null, false);
            initCharacterListView(itemView);
            root.addView(itemView);
            return itemView;
        }

        public void initCharacterListView(View itemView) {
            BaseCardAdapter.BaseCardViewHolder baseCardViewHolder = fakeBaseCardAdapter.new BaseCardViewHolder(itemView);
            itemView.setTag(baseCardViewHolder);
            itemView.setVisibility(View.GONE);
        }
    }
}
