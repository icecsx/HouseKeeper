package com.edu.nbl.housekeeper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.edu.nbl.housekeeper.R;
import com.edu.nbl.housekeeper.entity.TableInfo;

import java.util.ArrayList;

/**
 * Created by 世贤 on 2017/8/9.
 * 商家名称和商家号码的适配器
 *
 * 1.继承BaseAdapter
 * 2.重写相关方法
 */

public class TelListAdapter extends BaseAdapter{
    private Context context;//上下文负责填充item布局
    private ArrayList<TableInfo> datas;//适配器中的数据

    public TelListAdapter(Context context, ArrayList<TableInfo> tableInfos) {
        this.context=context;
        this.datas=tableInfos;
    }
    //数据量
    @Override
    public int getCount() {
        return datas.size();
    }
    //对应位置的数据
    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }
    //获取对应位置
    @Override
    public long getItemId(int position) {
        return position;
    }
    //组装数据
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //1.获取item布局
        convertView = LayoutInflater.from(context).inflate(R.layout.layout_tellist_item,null);
        //2.获取对应item的数据
        TableInfo tableInfo = datas.get(position);
        //3.获取convertView上的控件
        TextView tv_name = (TextView) convertView.findViewById(R.id.tv_name);
        TextView tv_number = (TextView) convertView.findViewById(R.id.tv_number);
        //4.把数据放到对应的控件上
        tv_name.setText(tableInfo.getName());
        tv_number.setText(tableInfo.getNumber()+"");
        return convertView;
    }
    //将变动的数据添加到适配器集合中
    public void addDatas(ArrayList<TableInfo> tableInfos) {
        datas.clear();
        datas.addAll(tableInfos);
    }
}
