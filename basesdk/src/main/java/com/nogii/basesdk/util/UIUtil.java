package com.nogii.basesdk.util;

import android.content.Context;
import android.content.res.Resources;

import com.nogii.basesdk.BaseApplication;


public class UIUtil {

	/** 获取上下文 */
	public static Context getContext() {
		return BaseApplication.getAppContext();
	}

	/** 获取Resources对象 */
	public static Resources getResources() {
		return getContext().getResources();
	}

	/** 获取xml文件中的字符串数据 */
	public static String getString(int resId) {
		return getResources().getString(resId);
	}

	/** 获取xml文件中的颜色值 */
	public static int getColor(int resId) {
		return getResources().getColor(resId);
	}

	/**获取xml文件中的字符串数组*/
	public static String[] getStringArr(int resId) {
		return getResources().getStringArray(resId);
	}

	/**获取包名*/
	public static String getPackageName(){
		return getContext().getPackageName();
	}

	/**安全的执行一个task
	 * @return */
	public static void postTaskSafely(Runnable task) {
		int curThreadId = android.os.Process.myTid();
		long mainThreadId = BaseApplication.getMainThreadId();
		if (curThreadId == mainThreadId) {
			task.run();
		} else {
			BaseApplication.getHandler().post(task);
		}
	}

	/**px2Dip*/
	public static int px2Dip(int px) {
		float density = getResources().getDisplayMetrics().density;
		int dip = (int) (px / density + .5f);
		return dip;
	}

	/**dip2Px*/
	public static int dip2Px(int dip) {
		float density = getResources().getDisplayMetrics().density;
		int px= (int) (dip * density + .5f);
		return px;
	}


}
