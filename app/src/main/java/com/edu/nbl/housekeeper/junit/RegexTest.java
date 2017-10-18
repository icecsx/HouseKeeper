package com.edu.nbl.housekeeper.junit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 世贤 on 2017/8/10.
 * 正则表达式
 *
 */

public class RegexTest {
    public static boolean checkEmail(){
        String email = "123123123@qq.com";
        String regex = "[a-zA-Z0-9]+@[a-zA-Z0-9]+.[a-z]{2,4}";
        boolean flag =email.matches(regex);

        return flag;
    }
    public static boolean checkPhone(){
        String number = "12345678910";
        String regex = "\\d{11}";//\\d表示0-9
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher =pattern.matcher(number);
        boolean flag=matcher.matches();
        return flag;
    }
}
