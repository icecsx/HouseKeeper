package com.edu.nbl.housekeeper.entity;

import android.content.pm.PackageInfo;

/**
 * Created by 世贤 on 2017/8/11.
 */

public class AppInfo {
    private boolean isDel;//是否删除
    private PackageInfo packageInfo;//此类封装软件的信息（应用程序名称，包名，版本，图标）

    public AppInfo() {

    }
    public AppInfo(boolean isDel, PackageInfo packageInfo) {
        this.isDel = isDel;
        this.packageInfo = packageInfo;
    }

    public boolean isDel() {
        return isDel;
    }

    public void setDel(boolean del) {
        isDel = del;
    }

    public PackageInfo getPackageInfo() {
        return packageInfo;
    }

    public void setPackageInfo(PackageInfo packageInfo) {
        this.packageInfo = packageInfo;
    }

    @Override
    public String toString() {
        return "AppInfo{" +
                "isDel=" + isDel +
                ", packageInfo=" + packageInfo +
                '}';
    }
}
