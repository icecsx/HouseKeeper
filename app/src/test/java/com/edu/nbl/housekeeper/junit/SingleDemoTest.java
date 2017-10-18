package com.edu.nbl.housekeeper.junit;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by 世贤 on 2017/8/14.
 */
public class SingleDemoTest {
    @Test
    public void text() throws Exception {
        String str= SingleDemo.text();
        System.out.println(str);
    }

}