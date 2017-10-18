package com.edu.nbl.housekeeper.junit;

/**
 * Created by 世贤 on 2017/8/14.
 * 单例模式
 * 懒汉式
 * 饿汉式
 */

public class SingleDemo {
    public static String text(){
//        A a = A.a;
//        A a1 =A.a;
        B b = B.getInstance();
        B b1 = B.getInstance();
        return b.toString()+"/"+b1.toString();
    }

}
//饿汉式
class A{
    //1.私有化构造方法
    private A(){

    }
    static A a = new A();
}
//懒汉式
class B{
    private B(){

    }
    private static B b;
    public static B getInstance(){
        if (b==null){
            synchronized (B.class) {
                if (b==null) {
                    b = new B();
                }
            }
        }
        return b;
    }
}