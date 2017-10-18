package com.edu.nbl.housekeeper.junit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 世贤 on 2017/8/15.
 */

public class MapTest {
    public static void getData(){
        Map<String,Integer> map = new HashMap<>();
        map.put("数学",91);
        map.put("语文",80);
        map.put("英语",70);
        System.out.println("map="+map);
        System.out.println("语文成绩："+map.get("语文"));
    }
    public static void getProcessApp(){
        Map<Integer,List<String>> map = new HashMap<>();
        List<String> sysapp = new ArrayList<>();
        sysapp.add("电话簿");
        sysapp.add("照相机");
        List<String> userapp = new ArrayList<>();
        userapp.add("支付宝");
        userapp.add("微信");
        map.put(2,sysapp);
        map.put(0,userapp);

        System.out.println("map="+map);
        List<String> getuserapp = map.get(0);
        System.out.println("用户进程="+getuserapp);
    }
}
