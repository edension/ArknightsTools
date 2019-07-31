package com.nogii.basesdk.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.nogii.basesdk.common.ArkConstant;

public class SpUtil {

    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(ArkConstant.ARK_SP_NAME, Context.MODE_PRIVATE);
    }

    /**
     * @描述 			保存boolean值到SharedPreferences中
     * @param context	上下文
     * @param key		保存的关键字
     * @param value		保存的值-boolean
     *@param  apply     apply()
     */
    public static void putBoolean(Context context,String key,boolean value, boolean apply){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit().putBoolean(key, value);
        if (apply) {
            editor.apply();
        } else {
            editor.commit();
        }
    }

    /**
     * @描述				获取SharedPreferences中存储的boolean值
     * @param context	上下文
     * @param key		保存的关键字
     * @param defValue	默认值
     * @return	返回存储的boolean值
     */
    public static boolean getBoolean(Context context,String key,boolean defValue){
        return getSharedPreferences(context).getBoolean(key, defValue);
    }

    /**
     * @描述 			保存String值到SharedPreferences中
     * @param context	上下文
     * @param key		保存的关键字
     * @param value		保存的值-String
     * @param  apply     apply()
     */
    public static void putString(Context context,String key,String value, boolean apply){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit().putString(key, value);
        if (apply) {
            editor.apply();
        } else {
            editor.commit();
        }
    }

    /**
     * @描述				获取SharedPreferences中存储的String值
     * @param context	上下文
     * @param key		保存的关键字
     * @param defValue	默认值
     * @return	返回存储的String值
     */
    public static String getString(Context context,String key,String defValue){
        return getSharedPreferences(context).getString(key, defValue);
    }

    /**
     * @描述 			保存int值到SharedPreferences中
     * @param context	上下文
     * @param key		保存的关键字
     * @param value		保存的值-int
     * @param  apply     apply()
     */
    public static void putInt(Context context,String key,int value, boolean apply){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit().putInt(key, value);
        if (apply) {
            editor.apply();
        } else {
            editor.commit();
        }
    }

    /**
     * @描述				获取SharedPreferences中存储的int值
     * @param context	上下文
     * @param key		保存的关键字
     * @param defValue	默认值
     * @return	返回存储的int值
     */
    public static int getInt(Context context,String key,int defValue){
        return getSharedPreferences(context).getInt(key, defValue);
    }

    /**
     * @描述 			保存long值到SharedPreferences中
     * @param context	上下文
     * @param key		保存的关键字
     * @param value		保存的值-long
     * @param  apply     apply()
     */
    public static void putLong(Context context,String key,long value, boolean apply){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit().putLong(key, value);
        if (apply) {
            editor.apply();
        } else {
            editor.commit();
        }
    }

    /**
     * @描述				获取SharedPreferences中存储的long值
     * @param context	上下文
     * @param key		保存的关键字
     * @param defValue	默认值
     * @return	返回存储的long值
     */
    public static long getLong(Context context,String key,Long defValue){
        return getSharedPreferences(context).getLong(key, defValue);
    }

}
