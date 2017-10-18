package com.edu.nbl.housekeeper.entity;

import android.graphics.drawable.Drawable;

public class RubbishFileInfo {
	private boolean isChecked;

	private int _id;
	private String softChinesename;
	private String softEnglishname;
	private String apkname;
	private String filepath;
	private Drawable icon;

	private long size;

	public RubbishFileInfo(int _id, String softChinesename, String softEnglishname, String apkname, String filepath) {
		super();
		this._id = _id;
		this.softChinesename = softChinesename;
		this.softEnglishname = softEnglishname;
		this.apkname = apkname;
		this.filepath = filepath;
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean checked) {
		isChecked = checked;
	}
	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String getSoftChinesename() {
		return softChinesename;
	}

	public void setSoftChinesename(String softChinesename) {
		this.softChinesename = softChinesename;
	}

	public String getSoftEnglishname() {
		return softEnglishname;
	}

	public void setSoftEnglishname(String softEnglishname) {
		this.softEnglishname = softEnglishname;
	}

	public String getApkname() {
		return apkname;
	}

	public void setApkname(String apkname) {
		this.apkname = apkname;
	}

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public Drawable getIcon() {
		return icon;
	}

	public void setIcon(Drawable icon) {
		this.icon = icon;
	}

	@Override
	public String toString() {
		return "SoftdetailInfo [_id=" + _id + ", softChinesename=" + softChinesename + ", apkname=" + apkname + "]";
	}

}
