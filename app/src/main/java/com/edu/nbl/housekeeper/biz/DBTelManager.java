package com.edu.nbl.housekeeper.biz;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.edu.nbl.housekeeper.entity.ClassInfo;
import com.edu.nbl.housekeeper.entity.TableInfo;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 世贤 on 2017/8/8.
 * 此工具类帮助我们将assets目录下的文件拷贝到data/data/包名/commonnum.db
 */

public class DBTelManager {
    //文件名
    private static final String FILE_NAME="commonnum.db";
    //包名
    private static final String PACKAGE_NAME="com.edu.nbl.housekeeper";
    //存储路径
    private static final String FILE_PATH="/data"+ Environment.getDataDirectory()+"/"+PACKAGE_NAME;
    //存储数据
    //InputStream is指的是将我们要拷贝的文件变成流  is代表commonnum.db文件流
    public static void readUpdateDB(InputStream is) throws IOException {
        File toFile = new File(FILE_PATH+"/"+FILE_NAME);//在data/data/包名/  新建commonnum.db
        if(toFile.exists()){
            return;
        }
        //如果存在将assets下面的commonnum.db文件拷贝到手机中
        BufferedInputStream bis = new BufferedInputStream(is); //提高读写效率 缓冲输入字节流
        FileOutputStream fos = new FileOutputStream(toFile,false); //文件字节输出流
        BufferedOutputStream bos = new BufferedOutputStream(fos);//高级流可以放低级流
        int len =0;
        byte[] data = new byte[1024*5];
        while ((len=bis.read(data))!=-1){
            bos.write(data,0,len);
        }
        bos.flush();//在缓冲区不满的情况下也能刷出数据
        bis.close();
        bos.close();
    }
    //从手机中的data/data/包名/commonnum.db查询数据  例如：订餐电话、公共服务
    public static ArrayList<ClassInfo> readClassListTable(){
        //打开FILE_PATH+"/"+FILE_NAME路径下的db文件
        //1.构建SQLiteDatabase实例
        SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(FILE_PATH+"/"+FILE_NAME,null);
        //用一个集合去存储我们查询的数据
        ArrayList<ClassInfo> classInfos = new ArrayList<>();
        //查询数据
        //2.编写查询语句
        String sql = "select * from classlist"; //*表示查询所有 包括name和idx
        //3.执行查询语句，获取坐标
        Cursor cursor = database.rawQuery(sql,null);
        //4.写循环查询所有数据
        if (cursor.moveToFirst()) {//将光标移到第一行（订餐电话）
            do {
                String name = cursor.getString(cursor.getColumnIndex("name"));//获取name字段所占列的下标
                int idx = cursor.getInt(cursor.getColumnIndex("idx"));
                //取一个存一个
                ClassInfo classInfo = new ClassInfo(name,idx);
                classInfos.add(classInfo);
            }while (cursor.moveToNext());//移动到下一行，如果有为true，没有为false
        }
        //关闭资源，施放内存
        cursor.close();
        database.close();
        return classInfos;
    }
    public static ArrayList<TableInfo> readTable(String tableName){
        ArrayList<TableInfo> tableInfos = new ArrayList<>();//集合存储所有数据
        //1.打开data/data/包名/commonnum.db数据库
        SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(FILE_PATH+"/"+FILE_NAME,null);
        //2.编写查询语句
        String sql = "select * from "+tableName; //例如table1，table2
        //3.执行查询语句，获取游标
        Cursor cursor = database.rawQuery(sql,null);
        //4.通过游标，遍历循环数据库中所有的数据
        if (cursor.moveToFirst()) {
            do {
                //取对应字段的数据
                String name = cursor.getString(cursor.getColumnIndex("name"));//获取name字段所占列的下标
                long number = cursor.getLong(cursor.getColumnIndex("number"));
                //取一个存一个
                TableInfo tableInfo = new TableInfo(name,number);
                tableInfos.add(tableInfo);
            } while (cursor.moveToNext());//条件是移到下一行是否还有数据
        }
        cursor.close();
        database.close();
        return tableInfos;//返回获取的所有数据
    }
}
