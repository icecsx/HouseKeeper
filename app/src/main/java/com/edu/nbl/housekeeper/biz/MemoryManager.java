package com.edu.nbl.housekeeper.biz;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import android.widget.Toast;

/**
 * �ڴ���Ϣ��ȡ
 */
public class MemoryManager {

	/** ��ȡ�ֻ�����sdcard·��, Ϊnull��ʾ�� */
	public static String getPhoneInSDCardPath() {
		String sdcardState = Environment.getExternalStorageState();
		if (!sdcardState.equals(Environment.MEDIA_MOUNTED)) {
			return null;
		}
		return Environment.getExternalStorageDirectory().getAbsolutePath();
	}

	/** 获取外置sdcard */
	public static String getPhoneOutSDCardPath() {
		Map<String, String> map = System.getenv();
		if (map.containsKey("SECONDARY_STORAGE")) {
			String paths = map.get("SECONDARY_STORAGE");
			// /storage/sdcard1:STOTAGE
			String path[] = paths.split(":");///storage/sdcard1:STOTAGE
			if (path == null || path.length <= 0) {
				return null;
			}
			return path[0];//["storage/sdcard1","STOTAGE"]
		}
		return "/storage/sdcard1";
	}

	/** �豸���ô洢SDCardȫ����С ��λB , ��û�����ÿ�ʱ,��СΪ0 */
	public static long getPhoneOutSDCardSize(Context context) {
		try {
			File path = new File(getPhoneOutSDCardPath());//storage/sdcard1
			if (path == null) {
				return 0;
			}
			StatFs stat = new StatFs(path.getPath());
			long blockSize = stat.getBlockSize();
			long blockCount = stat.getBlockCount();
			return blockCount * blockSize;
		} catch (Exception e) {
			Toast.makeText(context, "不存在外置SD卡，请插入", Toast.LENGTH_SHORT).show();
			return 0;
		}
	}

	/** h获取外置sdcard容量  B */
	public static long getPhoneOutSDCardFreeSize(Context context) {
		try {
			File path = new File(getPhoneOutSDCardPath());
			if (path == null) {
				return 0;
			}
			StatFs stat = new StatFs(path.getPath());
			long blockSize = stat.getBlockSize();
			long availaBlock = stat.getAvailableBlocks();
			return availaBlock * blockSize;
		} catch (Exception e) {
			return 0;
		}
	}

	/** 设备自身存储全部大小 单位B */
	public static long getPhoneSelfSize() {
		File path = Environment.getRootDirectory();
		//Log.d("TAG","Environment.getRootDirectory()"+path);//  /system
		//StatFs主要用于操作文件存储状态
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long blockCount = stat.getBlockCount();
		long rootFileSize = blockCount * blockSize;
		Log.d("SIZE","Environment.getRootDirectory()="+path+",blockSize="+blockSize+",blockCount="+blockCount+",rootFileSize="+rootFileSize);
		path = Environment.getDownloadCacheDirectory();// /cache
		
		stat = new StatFs(path.getPath());
		blockSize = stat.getBlockSize();
		blockCount = stat.getBlockCount();
		long cacheFileSize = blockCount * blockSize;
		Log.d("SIZE","Environment.getDownloadCacheDirectory()="+path+",blockSize="+blockSize+",blockCount="+blockCount+",cacheFileSize="+cacheFileSize);
		return rootFileSize + cacheFileSize;
	}

	/** 获取手机自身空闲容量的大小 单位B */
	public static long getPhoneSelfFreeSize() {
		File path = Environment.getRootDirectory();//  /system
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long blockCount = stat.getAvailableBlocks();
		long rootFileSize = blockCount * blockSize;

		path = Environment.getDownloadCacheDirectory();
		stat = new StatFs(path.getPath());
		blockSize = stat.getBlockSize();
		blockCount = stat.getAvailableBlocks();
		long cacheFileSize = blockCount * blockSize;//cache目录可用的总容量

		return rootFileSize + cacheFileSize;//system+cache目录可用容量的总和
	}

	/** 获取手机内置的sdcard容量    B */
	public static long getPhoneSelfSDCardSize() {
		String sdcardState = Environment.getExternalStorageState();
		if (!sdcardState.equals(Environment.MEDIA_MOUNTED)) {//判断内置sdcard是否挂载上
			return 0; //没有挂载上返回0
		}
		File path = Environment.getExternalStorageDirectory();// mnt/shell/emulated/0
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long blockCount = stat.getBlockCount();
		return blockCount * blockSize;
	}

	/** //获取手机内置的sdcard未使用的容量  B */
	public static long getPhoneSelfSDCardFreeSize() {
		String sdcardState = Environment.getExternalStorageState();
		if (!sdcardState.equals(Environment.MEDIA_MOUNTED)) {
			return 0;
		}
		File path = Environment.getExternalStorageDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availaBlock = stat.getAvailableBlocks();
		return availaBlock * blockSize;
	}

	/**��ȡ�ֻ��ܴ洢��С*/
	public static long getPhoneAllSize() {
		File path = Environment.getRootDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long blockCount = stat.getBlockCount();
		long rootFileSize = blockCount * blockSize;

		path = Environment.getDataDirectory();
		stat = new StatFs(path.getPath());
		blockSize = stat.getBlockSize();
		blockCount = stat.getBlockCount();
		long dataFileSize = blockCount * blockSize;

		path = Environment.getDownloadCacheDirectory();
		stat = new StatFs(path.getPath());
		blockSize = stat.getBlockSize();
		blockCount = stat.getBlockCount();
		long cacheFileSize = blockCount * blockSize;

		return rootFileSize + dataFileSize + cacheFileSize;
	}
	/**��ȡ�ֻ������ô洢��С*/
	public static long getPhoneAllFreeSize() {
		File path = Environment.getRootDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long blockCount = stat.getAvailableBlocks();
		long rootFileSize = blockCount * blockSize;

		path = Environment.getDataDirectory();
		stat = new StatFs(path.getPath());
		blockSize = stat.getBlockSize();
		blockCount = stat.getAvailableBlocks();
		long dataFileSize = blockCount * blockSize;

		path = Environment.getDownloadCacheDirectory();
		stat = new StatFs(path.getPath());
		blockSize = stat.getBlockSize();
		blockCount = stat.getAvailableBlocks();
		long cacheFileSize = blockCount * blockSize;

		return rootFileSize + dataFileSize + cacheFileSize;
	}

	/** �豸���������ڴ� ��λB */
	public static long getPhoneFreeRamMemory(Context context) {
		MemoryInfo info = new MemoryInfo();
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		am.getMemoryInfo(info);
		return info.availMem;//availMem���Դ洢���ֻ����е������ڴ�
	}

	/** �豸���������ڴ� ��λB */
	public static long getPhoneTotalRamMemory() {
		try {
			FileReader fr = new FileReader("/proc/meminfo");
			BufferedReader br = new BufferedReader(fr);
			String text = br.readLine();//读一行 MemTotal:  1030396 kBMemFree:  490424 kBBuffers:
			String[] array = text.split("\\s+");//space  [MemTotal:,1030396,kBMemFree....]
			return Long.valueOf(array[1]) * 1024; // ԭΪk, תΪb   array[1]=1030396ȫ�������ڴ�
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}
}
