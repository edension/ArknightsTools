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

public class BaseCardAdapter extends RecyclerView.Adapter<BaseCardAdapter.BaseCardViewHolder> {

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

    protected class BaseCardViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView cardName;

        public BaseCardViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_item_root);
            cardName = itemView.findViewById(R.id.card_item_name);
        }

        public void bindCardViewHolder(Object obj, boolean isItemSelected) {
            String cardName = "";
            if (obj instanceof CharacterBean) {
                cardName = ((CharacterBean) obj).getName();
            } else if (obj instanceof TagBean) {
                cardName = ((TagBean) obj).getName();
            }
            this.cardName.setText(cardName);

            int cardColor = ContextCompat.getColor(mContext, R.color.tag_rare_one_color);
            if (obj instanceof TagBean) {
                int tagRare = ((TagBean) obj).getRare();
                if (tagRare == 2) {
                    cardColor = ContextCompat.getColor(mContext, R.color.tag_rare_two_color);
                } else if (tagRare == 3) {
                    cardColor = ContextCompat.getColor(mContext, R.color.tag_rare_three_color);
                } else if (tagRare == 4) {
                    cardColor = ContextCompat.getColor(mContext, R.color.tag_rare_four_color);
                }
            } else if (obj instanceof CharacterBean) {
                int star = ((CharacterBean) obj).getStar();
                if (star <= 1) {
                } else if (star < 4) {
                } else if (star < 5) {
                    cardColor = ContextCompat.getColor(mContext, R.color.tag_rare_two_color);
                } else if (star < 6) {
                    cardColor = ContextCompat.getColor(mContext, R.color.tag_rare_three_color);
                } else if (star < 7) {
                    cardColor = ContextCompat.getColor(mContext, R.color.tag_rare_four_color);
                }
            }
            if (isItemSelected) {
                cardColor = Color.BLACK;
            }

            cardView.setCardBackgroundColor(cardColor);
        }
    }

    public interface CardAdapterInterface {
        void onCardItemClick(View view, Object obj, boolean isAdd, boolean isIgnoreClickLimit);
        int getSelectTagCount();
    }
}
