package com.edu.nbl.housekeeper.junit;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by 世贤 on 2017/8/10.
 */
public class RegexTestTest {
    @Test
    public void checkEmail() throws Exception {
        boolean flag = RegexTest.checkEmail();
        assertTrue(flag);
        System.out.println("flag="+flag);

        boolean flagPhone = RegexTest.checkPhone();
        System.out.println("flagPhone="+flagPhone);
    }

}