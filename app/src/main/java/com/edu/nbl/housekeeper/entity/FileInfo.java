package com.edu.nbl.housekeeper.entity;

import java.io.File;

/**
 * Created by 世贤 on 2017/8/21.
 * 文件实体类
 */

public class FileInfo {
    private boolean isSelect;//是否选中
    private String iconName;//图片名称
    private String fileType;//文件类型（通过后缀名）
    private File file;//文件

    public FileInfo(boolean isSelect, String iconName, String fileType, File file) {
        this.isSelect = isSelect;
        this.iconName = iconName;
        this.fileType = fileType;
        this.file = file;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getIconName() {
        return iconName;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return "FileInfo{" +
                "isSelect=" + isSelect +
                ", iconName='" + iconName + '\'' +
                ", fileType='" + fileType + '\'' +
                ", file=" + file +
                '}';
    }
}
