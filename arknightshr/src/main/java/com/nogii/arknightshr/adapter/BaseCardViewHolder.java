package com.nogii.arknightshr.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.nogii.arknightshr.R;
import com.nogii.arknightshr.bean.CharacterBean;
import com.nogii.arknightshr.bean.TagBean;


public class BaseCardViewHolder extends RecyclerView.ViewHolder {

    CardView cardView;
    TextView cardName;
    Context mContext;

    public BaseCardViewHolder(@NonNull View itemView) {
        super(itemView);
        cardView = itemView.findViewById(R.id.card_item_root);
        cardName = itemView.findViewById(R.id.card_item_name);
        mContext = itemView.getContext();
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