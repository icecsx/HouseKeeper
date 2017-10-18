package com.edu.nbl.housekeeper.utils;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by 世贤 on 2017/8/10.
 */
public class CommonUtilsTest {
    @Test
    public void getFileSize() throws Exception {
        String str = CommonUtils.getFileSize(1024);
        System.out.println("str="+str);

    }

}