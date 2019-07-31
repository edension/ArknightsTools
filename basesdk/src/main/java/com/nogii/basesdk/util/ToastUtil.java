package com.nogii.basesdk.util;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import java.util.List;

/**
 * Toast工具类，可显示和取消，应用在后台时可选择不显示
 */
public class ToastUtil {

	private static Toast mToast;

	private static Context appContext;

	public static void setAppContext(Context context) {
		appContext = context;
	}

	/**
	 * 显示Toast
	 * @param resId 显示的内容id
	 * @param onlyShowInForeground 是否仅当app在前台时显示
	 */
	public static void showToast(int resId, boolean onlyShowInForeground){
		String text = appContext.getResources().getString(resId);
		if(!TextUtils.isEmpty(text)){
			showToast(text, onlyShowInForeground);
		}
	}

	/**
	 * 显示Toast
	 * @param text 显示的内容
	 * @param onlyShowInForeground 是否仅当app在前台时显示
	 */
	public static void showToast(String text, boolean onlyShowInForeground){
		if(onlyShowInForeground && !isAppForeground(appContext) && null != appContext){
			return;
		}
		if(mToast == null) {
			mToast = Toast.makeText(appContext, text, Toast.LENGTH_SHORT);
		} else {
			mToast.setText(text);
			mToast.setDuration(Toast.LENGTH_SHORT);
		}
		mToast.show();
	}

	/**
	 * 显示Toast
	 * @param text 显示的内容
	 */
	public static void showToast(String text){
		showToast(text, false);
	}

	/**
	 * 显示Toast
	 * @param resId 显示的内容
	 */
	public static void showToast(int resId){
		showToast(resId, false);
	}

	/**
	 * 取消显示
	 */
	public static void cancelToast() {
		if (mToast != null) {
			mToast.cancel();
		}
	}

	/**
	 * 判断当前应用是否在前台
	 * @param context
	 * @return	true 前台，false 后台
	 */
	private static boolean isAppForeground(Context context) {
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
		for (RunningAppProcessInfo appProcess : appProcesses) {
			if (appProcess.processName.equals(context.getPackageName())) {
				if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
					return true;
				}else{
					return false;
				}
			}
		}
		return false;
	}
}
