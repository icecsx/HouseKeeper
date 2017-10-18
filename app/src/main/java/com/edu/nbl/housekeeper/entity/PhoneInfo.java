package com.edu.nbl.housekeeper.entity;

import android.graphics.drawable.Drawable;

/**
 * Created by 世贤 on 2017/8/17.
 * 封装手机信息的实体
 */

public class PhoneInfo {
    private Drawable icon;//item的图标
    private String title,text;//item的标题和内容

    public PhoneInfo() {
    }

    public PhoneInfo(Drawable icon, String title, String text) {
        this.icon = icon;
        this.title = title;
        this.text = text;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "PhoneInfo{" +
                "icon=" + icon +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
