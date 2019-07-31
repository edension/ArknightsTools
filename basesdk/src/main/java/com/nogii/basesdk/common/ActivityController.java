package com.nogii.basesdk.common;

import android.app.Activity;

import java.util.LinkedList;


public class ActivityController {
    
    public static LinkedList<Activity> activities = new LinkedList<>();
    
    public static void addActivity(Activity activity) {
        if (isActivityExist(activity)) {
            activities.add(activity);
        }
    }
    
    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }
    
    public static void finishAllActivity() {
        for (int i = 0; i < activities.size(); i++) {
            Activity activity = activities.get(i);
            if (isActivityExist(activity)) {
                activity.finish();
            }
        }
        activities.clear();
    }

    public static boolean isActivityExist(Activity activity) {
        return null != activity && !activity.isDestroyed() && !activity.isFinishing();
    }
}
