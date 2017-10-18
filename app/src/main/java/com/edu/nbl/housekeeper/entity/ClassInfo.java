package com.edu.nbl.housekeeper.entity;

/**
 * Created by 世贤 on 2017/8/8.
 */

public class ClassInfo {
    private String name; //类型 例如：订餐电话
    private int idx; //下标     例如： 1

    public ClassInfo() {
    }

    public ClassInfo(String name, int idx) {
        this.name = name;
        this.idx = idx;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    @Override
    public String toString() {
        return "ClassInfo{" +
                "name='" + name + '\'' +
                ", idx=" + idx +
                '}';
    }
}
