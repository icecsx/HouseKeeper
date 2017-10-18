package com.edu.nbl.housekeeper.biz;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Environment;

import com.edu.nbl.housekeeper.R;
import com.edu.nbl.housekeeper.entity.RubbishFileInfo;

public class DbClearPathManager {
	private static final String FILE_NAME = "clearpath.db"; // db文件名称
	private static final String PACKAGE_NAME = "com.edu.nbl.housekeeper"; // 当前应用程序包名
	// 文件路径
	private static final String FILE_PATH = "/data" + Environment.getDataDirectory().getAbsolutePath() + "/" + PACKAGE_NAME;

	private static ArrayList<RubbishFileInfo> softdetailInfos;

	public static ArrayList<RubbishFileInfo> getPhoneRubbishfile(Context context) {
		// #读取DB内softdetail表上的所有数据(225个)
		if (softdetailInfos == null) {
			softdetailInfos = readSoftdetailTable(); // 从Softdetail表内取出DB内有的所有缓存目录
		}
		// #从所有数据内，挑出手机上存在的
		ArrayList<RubbishFileInfo> phontSoftdetailInfos = new ArrayList<RubbishFileInfo>();
		for (RubbishFileInfo info : softdetailInfos) {
			File file = new File(info.getFilepath());
			if (file.exists()) {
				Drawable icon = null;
				try {
					icon = context.getPackageManager().getApplicationIcon(info.getApkname());
				} catch (NameNotFoundException e) {
					icon = context.getResources().getDrawable(R.drawable.ic_launcher);
				}
				info.setIcon(icon);
				phontSoftdetailInfos.add(info);
			}
		}
		return phontSoftdetailInfos;
	}

	private static ArrayList<RubbishFileInfo> readSoftdetailTable() {
		ArrayList<RubbishFileInfo> softdetailInfos = new ArrayList<RubbishFileInfo>();
		String path = FILE_PATH + "/" + FILE_NAME;
		SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(path, null);
		String sql = "select * from softdetail"; // 查询softdetail表（缓存目录表）
		Cursor cursor = database.rawQuery(sql, null);
		if (cursor.moveToFirst()) {
			do {
				int _id = cursor.getInt(cursor.getColumnIndex("_id"));
				String softChinesename = cursor.getString(cursor.getColumnIndex("softChinesename"));
				String softEnglishname = cursor.getString(cursor.getColumnIndex("softEnglishname"));
				String apkname = cursor.getString(cursor.getColumnIndex("apkname"));
				String filepath = cursor.getString(cursor.getColumnIndex("filepath"));
				filepath = Environment.getExternalStorageDirectory().getPath() + filepath;
				RubbishFileInfo info = new RubbishFileInfo(_id, softChinesename, softEnglishname, apkname, filepath);
				softdetailInfos.add(info);
			} while (cursor.moveToNext());
		}
		cursor.close();
		database.close();
		return softdetailInfos;
	}

	/** 更新DB文件 */
	public static void readUpdateDB(InputStream path) {
		File toFile = new File(FILE_PATH + "/" + FILE_NAME);
		if (toFile.exists()) {
			return;
		}
		try {
			// 输入流
			BufferedInputStream buffInputStream = new BufferedInputStream(path);
			// 输出流
			FileOutputStream outputStream = new FileOutputStream(toFile, false);
			BufferedOutputStream buffOutputStream = new BufferedOutputStream(outputStream);

			int len = 0;
			byte[] buff = new byte[5 * 1024];
			while ((len = buffInputStream.read(buff)) != -1) {
				buffOutputStream.write(buff, 0, len);
			}
			buffOutputStream.flush();
			buffOutputStream.close();
			buffInputStream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
