package com.edu.nbl.housekeeper.junit;

import android.os.Environment;

import java.io.File;

/**
 * Created by 世贤 on 2017/8/18.
 */

public class FileDemo {
    public static void getAllFiles(){
        File dir = Environment.getExternalStorageDirectory();
        File[] files = dir.listFiles();
        for (File file:files){
            System.out.println("fileName="+file.getName());
        }
    }
}
