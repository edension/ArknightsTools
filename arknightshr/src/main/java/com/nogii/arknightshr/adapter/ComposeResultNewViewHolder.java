package com.nogii.arknightshr.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nogii.arknightshr.R;
import com.nogii.arknightshr.bean.ComposeResultBean;
import com.nogii.basesdk.util.RecyclerViewUtil;

import java.util.ArrayList;

/**
 * 不用于RecyclerView的ViewHolder
 */
public class ComposeResultNewViewHolder extends RecyclerView.ViewHolder {

    public View itemView;
    public ViewGroup tagList;
    public ViewGroup characterList;

    public ComposeResultBean composeResultBeans;

    private Context mContext;


    public ComposeResultNewViewHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
        this.itemView.setTag(this);
        mContext= itemView.getContext();

        tagList = itemView.findViewById(R.id.compose_result_item_tags);
        characterList = itemView.findViewById(R.id.compose_result_item_characters);
    }
}
