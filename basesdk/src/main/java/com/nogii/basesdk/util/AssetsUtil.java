package com.nogii.basesdk.util;

import android.content.Context;
import android.content.res.AssetManager;

import com.nogii.basesdk.common.ArkConstant;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class AssetsUtil {

    public static AssetManager getAssetsManager(Context context) {
        return context.getAssets();
    }

    public static InputStream openAssetsFile(Context context, String fileName) {
        return openAssetsFile(context, fileName, ArkConstant.ARK_SPECIAL_LABEL_VALUE);
    }

    public static InputStream openAssetsFile(Context context, String fileName, int accessMode) {
        InputStream inputStream = null;
        AssetManager assetManager = getAssetsManager(context);
        try {
            if (accessMode == ArkConstant.ARK_SPECIAL_LABEL_VALUE) {
                inputStream = assetManager.open(fileName);
            } else {
                inputStream = assetManager.open(fileName, accessMode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputStream;
    }

    /**
     * 从assets文件读取String字符串
     * @param context
     * @param fileName
     * @return
     */
    public static String readStringFromAssest(Context context, String fileName) {
        return FileUtil.readFileString(openAssetsFile(context, fileName));
    }

    /**
     * 从assets文件读取String字符串
     * @param context
     * @param fileName
     * @param accessMode
     * @return
     */
    public static String readStringFromAssest(Context context, String fileName, int accessMode) {
        return  FileUtil.readFileString(openAssetsFile(context, fileName, accessMode));
    }


    /**
     * 从assets目录下拷贝整个文件夹，不管是文件夹还是文件都能拷贝
     *
     * @param context           上下文
     * @param rootDirFullPath   文件目录，要拷贝的目录如assets目录下有一个tessdata文件夹：
     * @param targetDirFullPath 目标文件夹位置如：/Download/tessdata
     */
    public static void copyFolderFromAssets(Context context, String rootDirFullPath, String targetDirFullPath) {
        try {
            String[] listFiles = context.getAssets().list(rootDirFullPath);// 遍历该目录下的文件和文件夹
            for (String string : listFiles) {// 判断目录是文件还是文件夹，这里只好用.做区分了
                if (FileUtil.isFileByName(string)) {// 文件
                    copyFileFromAssets(context, rootDirFullPath + "/" + string, targetDirFullPath + "/" + string);
                } else {// 文件夹
                    String childRootDirFullPath = rootDirFullPath + "/" + string;
                    String childTargetDirFullPath = targetDirFullPath + "/" + string;
                    new File(childTargetDirFullPath).mkdirs();
                    copyFolderFromAssets(context, childRootDirFullPath, childTargetDirFullPath);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 从assets目录下拷贝文件
     *
     * @param context            上下文
     * @param assetsFilePath     文件的路径名如：SBClock/0001cuteowl/cuteowl_dot.png
     * @param targetFileFullPath 目标文件路径如：/sdcard/SBClock/0001cuteowl/cuteowl_dot.png
     */
    public static void copyFileFromAssets(Context context, String assetsFilePath, String targetFileFullPath) {
        InputStream assestsFileImputStream;
        try {
            assestsFileImputStream = context.getAssets().open(assetsFilePath);
            FileUtil.copyFile(assestsFileImputStream, targetFileFullPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
