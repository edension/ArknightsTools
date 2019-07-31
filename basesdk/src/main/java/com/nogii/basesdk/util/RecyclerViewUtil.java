package com.nogii.basesdk.util;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

import com.nogii.basesdk.view.FlowLayoutManager;

public class RecyclerViewUtil {

    public static LinearLayoutManager getBaseLayoutManager(Context context, boolean isVertical) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(isVertical ? LinearLayoutManager.VERTICAL : LinearLayoutManager.HORIZONTAL);
        return linearLayoutManager;
    }

    public static FlowLayoutManager getFlowLayoutManager(Context context, boolean isFullyLayout) {
        return new FlowLayoutManager(context, isFullyLayout);
    }
}
