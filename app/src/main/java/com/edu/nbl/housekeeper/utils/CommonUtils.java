package com.edu.nbl.housekeeper.utils;

import java.sql.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

/**
 * Created by 世贤 on 2017/8/10.
 * 进行单位换算工具类
 */

public class CommonUtils {
    //把long值对应的文件变成对应单位的
    public static String getFileSize(long fileSize){//对应的是字节
        //保留小数点后两位
        DecimalFormat df = new DecimalFormat("#.00");
        StringBuffer msgbuilder = new StringBuffer();
        if (fileSize<1024){//<1kb 就用byte本身单位
            msgbuilder.append((double)fileSize);
            msgbuilder.append("B");
        }else if (fileSize<1024*1024){
            msgbuilder.append(df.format((double)fileSize/1024));//保留两位小数
            msgbuilder.append("K");
        }else if (fileSize<1024*1024*1024){
            msgbuilder.append(df.format((double)fileSize/1024/1024));//保留两位小数
            msgbuilder.append("M");
        }else {
            msgbuilder.append(df.format((double)fileSize/1024/1024/1024));
            msgbuilder.append("G");
        }
        return msgbuilder.toString();
    }
    //把long类型的毫秒数变成固定格式字符串时间：2017-8-22 11:11:43
    public static String getStrTime(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date data = new Date(time);//把毫秒数变成Data类型
        String strTime = sdf.format(data);//把Data类型变成字符串
        return strTime;
    }
}
