package com.nogii.arknightshr.adapter;

import android.content.Context;
import android.graphics.Color;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nogii.arknightshr.R;
import com.nogii.arknightshr.bean.CharacterBean;
import com.nogii.arknightshr.bean.TagBean;
import com.nogii.basesdk.util.ToastUtil;

import java.util.ArrayList;

public class BaseCardAdapter extends RecyclerView.Adapter<BaseCardViewHolder> {

    private Context mContext;

    private ArrayList data;

    private ArrayList selectData = new ArrayList();

    private CardAdapterInterface cardAdapterInterface;

    private boolean isIgnoreSelectLimit;

    public BaseCardAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public BaseCardViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new BaseCardViewHolder(LayoutInflater.from(mContext).inflate(R.layout.base_card_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(BaseCardViewHolder holder, final int position) {
        final Object currentItem = getItem(position);

        final boolean isItemSelected = selectData.contains(currentItem);

        holder.bindCardViewHolder(currentItem, isItemSelected);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != cardAdapterInterface) {
                    if (!isIgnoreSelectLimit && !isItemSelected && cardAdapterInterface.getSelectTagCount() >= 5) {
                        ToastUtil.showToast("只能选择五个标签...");
                        return;
                    }
                    if (isItemSelected) {
                        selectData.remove(currentItem);
                    } else {
                        selectData.add(currentItem);
                    }
                    notifyDataSetChanged();
                    cardAdapterInterface.onCardItemClick(v, getItem(position), !isItemSelected, isIgnoreSelectLimit);
                }
            }
        });
    }

    public Object getItem(int position) {
        return null == data ? null : data.get(position);
    }

    @Override
    public int getItemCount() {
        return null == data ? 0 : data.size();
    }

    public ArrayList getData() {
        return data;
    }

    public void setData(ArrayList data) {
        selectData.clear();
        this.data = data;
    }

    public CardAdapterInterface getCardAdapterInterface() {
        return cardAdapterInterface;
    }

    public void setCardAdapterInterface(CardAdapterInterface cardAdapterInterface) {
        this.cardAdapterInterface = cardAdapterInterface;
    }

    public ArrayList getSelectData() {
        return selectData;
    }

    public boolean isIgnoreSelectLimit() {
        return isIgnoreSelectLimit;
    }

    public void setIgnoreSelectLimit(boolean ignoreSelectLimit) {
        isIgnoreSelectLimit = ignoreSelectLimit;
    }

    public interface CardAdapterInterface {
        void onCardItemClick(View view, Object obj, boolean isAdd, boolean isIgnoreClickLimit);
        int getSelectTagCount();
    }
}
