package com.edu.nbl.housekeeper.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import com.edu.nbl.housekeeper.R;
import com.edu.nbl.housekeeper.main.MainActivity;

/**
 * Created by 世贤 on 2017/8/7.
 *  通知的工具类，帮助我们操作通知
 */

public class NotificationUtils {
    //1.构建通知管理对象
    private static NotificationManager manager;
    //2.构建通知对象
    private static Notification notification;
    //通知的id
    private static final int NOTIFI_APPICON_ID=1;
    //显示通知
    public static void showNotification(Context context){
        if (manager==null){//判断通知管理器是否为空
            //1.构建通知管理对象
            manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        if (notification==null){//如果通知为空
            //2.构建通知对象
            notification = new Notification();
        }
        //3.给通知设置属性
        notification.flags = Notification.FLAG_AUTO_CANCEL;//左右滑动可清除通知
        notification.icon = R.drawable.ic_launcher;//设置icon图标
        notification.tickerText = "新通知";
        notification.when= System.currentTimeMillis();//设置通知发送时间
        //自定义通知布局
        RemoteViews contentViews = new RemoteViews(context.getPackageName(),R.layout.layout_notify);
        notification.contentView = contentViews;//设置自定义的通知远程视图
        //点击通知跳转设置
        Intent intent = new Intent(context,MainActivity.class);//点击通知跳转到MainActivity中
        //PendingIntent.FLAG_UPDATE_CURRENT 及时更新通知
        PendingIntent pendingIntent = PendingIntent.getActivity(context,1,intent,PendingIntent.FLAG_UPDATE_CURRENT);//可延迟的意图
        notification.contentIntent = pendingIntent;
        //4.将通知放到通知管理器中
        manager.notify(NotificationUtils.NOTIFI_APPICON_ID,notification);
    }
    //取消通知
    public static void cancleNotification(Context context){
        if (manager==null){//如果通知管理器为空
            manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        manager.cancel(NOTIFI_APPICON_ID);//直接取消通知 根据通知id
    }
    //存储通知的状态
    public static void setOpenNotification(Context context,boolean open){
        //获取SharedPreferences实例
        SharedPreferences preferences = context.getSharedPreferences("notify",Context.MODE_PRIVATE);//notify.xml文件
        //获取Editor实例
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("open",open);
        //提交
        editor.commit();
    }
    //获取存储的通知按钮状态
    public static boolean getOpenNotification(Context context){
        //获取SharedPreferences实例  从存的notify.xml文件中取数据
        SharedPreferences preferences = context.getSharedPreferences("notify",Context.MODE_PRIVATE);
        //获取数据
        boolean open = preferences.getBoolean("open",false);
        return open;//本身需要判断是否是选中状态
    }
}
