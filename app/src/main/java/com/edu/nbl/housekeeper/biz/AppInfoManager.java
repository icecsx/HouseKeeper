package com.edu.nbl.housekeeper.biz;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Debug;
import android.util.Log;

import com.edu.nbl.housekeeper.entity.AppInfo;
import com.edu.nbl.housekeeper.entity.RunningAppInfo;

/** 应用程序管理类 */
public class AppInfoManager {
	private Context context;
	private PackageManager packageManager;//获取应用程序信息(系统的和用户的)
	private ActivityManager activityManager;
	/** 用来保存所有应用程序包(activity的)列表 */
	private List<AppInfo> allPackageInfos = new ArrayList<AppInfo>();//存所有软件
	private List<AppInfo> userPackageInfos = new ArrayList<AppInfo>();//存用户软件
	private List<AppInfo> systemPackageInfos = new ArrayList<AppInfo>();//存系统软件

	/** 实例化本类时(单态了)，将去获取所有应用程序列表,保存在 {@link #allPackageInfos} */
	private AppInfoManager(Context context) {
		this.context = context;
		packageManager = context.getPackageManager();
		activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
	}

	/** 清理所有正在运行的程序(级别为服务进程以上的非系统进程) */
	public void killALLProcesses() {
		//获取所有进程
		List<RunningAppProcessInfo> appProcessInfos = activityManager.getRunningAppProcesses();
		for (RunningAppProcessInfo appProcessInfo : appProcessInfos) {//遍历所有进程
			if (appProcessInfo.importance >= RunningAppProcessInfo.IMPORTANCE_SERVICE) {
				String packageName = appProcessInfo.processName;//服务进程  后台进程  空进程
				try {
					ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA | PackageManager.GET_SHARED_LIBRARY_FILES | PackageManager.GET_SHARED_LIBRARY_FILES | PackageManager.GET_UNINSTALLED_PACKAGES);
					if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {//是否是系统进程
					} else {
						activityManager.killBackgroundProcesses(packageName);//杀死（服务进程  后台进程  空进程）用户进程
					}
				} catch (NameNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/** 清理指定程序 */
	public void killProcesses(String packageName) {
		activityManager.killBackgroundProcesses(packageName);
	}

	public static final int RUNING_APP_TYPE_SYS = 2;//表示显示系统进程
	public static final int RUNING_APP_TYPE_USER = 0;//表示显示用户进程

	/** 获取正在运行应用 */
	public Map<Integer, List<RunningAppInfo>> getRuningAppInfos() {
		Map<Integer, List<RunningAppInfo>> runingAppInfos = new HashMap<Integer, List<RunningAppInfo>>();//构建一个map
		List<RunningAppInfo> sysapp = new ArrayList<RunningAppInfo>();//存储系统进程的集合
		List<RunningAppInfo> userapp = new ArrayList<RunningAppInfo>();//存储用户进程的集合
		// 获取所有正在运行应用
		List<RunningAppProcessInfo> appProcessInfos = activityManager.getRunningAppProcesses();//获取所有进程
		Log.d("APP","所有进程："+appProcessInfos);
		for (RunningAppProcessInfo appProcessInfo : appProcessInfos) {//遍历所有的进程
			String packageName = appProcessInfo.processName; // 正在运行程序进程名
			int pid = appProcessInfo.pid; // 正在运行程序进程ID
			//100      200      300    400    500
			//前台进程->可见进程->服务进程>后台进程>空进程
			int importance = appProcessInfo.importance; // 正在运行程序进程级别
			Log.d("APP","packageName="+packageName+",pid="+pid+",importance="+importance);
			// 服务进程（包括）级别以下进程
			if (importance >= RunningAppProcessInfo.IMPORTANCE_SERVICE) {//服务进程>后台进程>空进程
				Drawable icon; // 所取数据：运行中程序图标
				String lableName; // 所取数据：运行中程序名称
				long size; // 所取数据：运行中程序所占内存
				Debug.MemoryInfo[] memoryInfos = activityManager.getProcessMemoryInfo(new int[] { pid });
				size = (memoryInfos[0].getTotalPrivateDirty()) * 1024;//将运行内存k转换成b

				try {
					icon = packageManager.getApplicationIcon(packageName);//获取图标
					ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA | PackageManager.GET_SHARED_LIBRARY_FILES | PackageManager.GET_UNINSTALLED_PACKAGES);
					lableName = packageManager.getApplicationLabel(applicationInfo).toString();//应用程序的名称
					Log.d("APP","优先级大于服务进程:icon="+icon+",lableName"+lableName+",size="+size+",importance="+importance);
					// TODO: 2017/8/14 手机加速页面要改的代码
					RunningAppInfo runingAppInfo = new RunningAppInfo(false,icon,lableName,packageName,false,size);
					// 系统进程
					if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {//判断是否是系统进程
						runingAppInfo.setSystem(true);//设置为true表示显示系统进程
						runingAppInfo.setClear(false);//设置checkBox为不勾选的状态
						sysapp.add(runingAppInfo);//将获取到的系统进程放到sysapp集合中去
					}
					// 用户进程(默认选中)
					else {
						runingAppInfo.setSystem(false);//设置为false表示不显示系统进程
						runingAppInfo.setClear(true);//设置checkBox为勾选的状态
						userapp.add(runingAppInfo);//将获取到的用户进程放到userapp集合中去
					}
				} catch (NameNotFoundException ex) {
				}
			}
		}
		runingAppInfos.put(RUNING_APP_TYPE_SYS, sysapp);//将所有的系统进程集合放到对应key=2的runingAppInfos的Value中
		runingAppInfos.put(RUNING_APP_TYPE_USER, userapp);//将所有的用户进程集合放到对应key=0的runingAppInfos的Value中
		return runingAppInfos;//返回存储了系统进程集合和用户进程集合Map
	}

	/** 用来返回本类的唯一对象 (单态模块　且做了同步处理,还优化了一下同步处理) */
	private static AppInfoManager appManager = null;
	//单例模式
	public static AppInfoManager getAppInfoManager(Context context) {
		if (appManager == null) {
			synchronized (context) {
				if (appManager == null) {
					appManager = new AppInfoManager(context);
				}
			}
		}
		return appManager;
	}

	/** 用来返回所有应用程序列表 */
	public List<AppInfo> getAllPackageInfo(boolean isReset) {
		if (isReset) {
			loadAllActivityPackager();//向allPackageInfos存所有软件
		}
		return allPackageInfos;//所有软件
	}

	/** 用来返回所有系统应用程序列表 */
	public List<AppInfo> getSystemPackageInfo(boolean isReset) {
		if (isReset) {
			loadAllActivityPackager();//向allPackageInfos存所有软件
			systemPackageInfos.clear();//清空systemPackageInfos
			for (AppInfo appInfo : allPackageInfos) {//遍历所有的软件（系统的和用户的）
				if ((appInfo.getPackageInfo().applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {//判断是否是系统软件

					systemPackageInfos.add(appInfo);// 系统软件
				} else {//用户软件
				}
			}
		}
		return systemPackageInfos;
	}

	/** 用来返回所有用户应用程序列表 */
	public List<AppInfo> getUserPackageInfo(boolean isReset) {
		if (isReset) {
			loadAllActivityPackager();//获取所有软件
			userPackageInfos.clear();//清空用户软件
			for (AppInfo appInfo : allPackageInfos) {
				if ((appInfo.getPackageInfo().applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {//系统软件
				} else {
					userPackageInfos.add(appInfo);// 用户软件
				}
			}
		}
		return userPackageInfos;
	}

	// 加载所有Activity应用程序包
	private void loadAllActivityPackager() {
		List<PackageInfo> infos = packageManager.getInstalledPackages(PackageManager.GET_ACTIVITIES | PackageManager.GET_UNINSTALLED_PACKAGES);
		//3|9
		//00000011 3 1*2^1+1*2^0
		//00001001 9  1*2^3+1*2^0
		//00001011 11
		allPackageInfos.clear();
		for (PackageInfo packageInfo : infos) {//遍历所有PackageInfo类型，把它存储到所有软件的集合中
			allPackageInfos.add(new AppInfo(false,packageInfo));//new AppInfo(packageInfo)包PackageInfo类型变成AppInfo类型
		}
	}
}
