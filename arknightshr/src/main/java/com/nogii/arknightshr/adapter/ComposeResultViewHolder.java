package com.nogii.arknightshr.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nogii.arknightshr.R;
import com.nogii.basesdk.util.RecyclerViewUtil;

@Deprecated
public class ComposeResultViewHolder  extends RecyclerView.ViewHolder {

    public View itemView;
    public RecyclerView tagList;
    // FlowLayout characterList;
    public RecyclerView characterList;
    public BaseCardAdapter tagAdapter;
    public BaseCardAdapter characterListAdapter;
    private Context mContext;

    public ComposeResultViewHolder( View itemView) {
        super(itemView);
        this.itemView = itemView;
        this.itemView.setTag(this);
        mContext= itemView.getContext();

        tagList = itemView.findViewById(R.id.compose_result_item_tags);
        tagAdapter = new BaseCardAdapter(mContext);
        tagList.setAdapter(tagAdapter);
        tagList.setLayoutManager(RecyclerViewUtil.getFlowLayoutManager(mContext, false));
        tagList.setNestedScrollingEnabled(false);
        tagList.setItemViewCacheSize(200);

        characterList = itemView.findViewById(R.id.compose_result_item_characters);
        characterListAdapter = new BaseCardAdapter(mContext);
        characterList.setAdapter(characterListAdapter);
        characterList.setLayoutManager(RecyclerViewUtil.getFlowLayoutManager(mContext, false));
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
        BaseCardViewHolder baseCardViewHolder = new BaseCardViewHolder(itemView);
        itemView.setTag(baseCardViewHolder);
        itemView.setVisibility(View.GONE);
    }
}
