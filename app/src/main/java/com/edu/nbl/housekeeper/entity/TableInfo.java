package com.edu.nbl.housekeeper.entity;

/**
 * Created by 世贤 on 2017/8/9.
 * 封装商家名称和号码
 *
 */

public class TableInfo {
    private String name;
    private long number;

    public TableInfo() {

    }

    public TableInfo(String name, long number) {
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "TableInfo{" +
                "name='" + name + '\'' +
                ", number=" + number +
                '}';
    }
}
