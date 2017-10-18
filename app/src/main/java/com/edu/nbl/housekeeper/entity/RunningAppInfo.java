package com.edu.nbl.housekeeper.entity;

import android.graphics.drawable.Drawable;

/**
 * Created by 世贤 on 2017/8/14.
 * 封装进程的实体类
 */

public class RunningAppInfo {
    private boolean isClear;//是否清空
    private Drawable icon;//图标
    private String lableName;//应用程序名称
    private String packageName;//应用程序包名
    private boolean isSystem;//是否是系统进程
    private long size;//运行内存的大小

    public RunningAppInfo() {
    }

    public RunningAppInfo(boolean isClear, Drawable icon, String lableName, String packageName, boolean isSystem, long size) {
        this.isClear = isClear;
        this.icon = icon;
        this.lableName = lableName;
        this.packageName = packageName;
        this.isSystem = isSystem;
        this.size = size;
    }

    public boolean isClear() {
        return isClear;
    }

    public void setClear(boolean clear) {
        isClear = clear;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getLableName() {
        return lableName;
    }

    public void setLableName(String lableName) {
        this.lableName = lableName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public boolean isSystem() {
        return isSystem;
    }

    public void setSystem(boolean system) {
        isSystem = system;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "RunningAppInfo{" +
                "isClear=" + isClear +
                ", icon=" + icon +
                ", lableName='" + lableName + '\'' +
                ", packageName='" + packageName + '\'' +
                ", isSystem=" + isSystem +
                ", size=" + size +
                '}';
    }
}
