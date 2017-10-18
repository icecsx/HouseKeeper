package com.edu.nbl.housekeeper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.edu.nbl.housekeeper.R;
import com.edu.nbl.housekeeper.entity.ClassInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 世贤 on 2017/8/8.
 * 1.继承BaseAdapter
 * 2.重写相关方法
 */

public class ClassListAdapter extends BaseAdapter{
    private Context context; //上下文
    private List<ClassInfo> datas; //存储适配器集合的数据

    public ClassListAdapter(Context context, List<ClassInfo> datas) {
        this.context = context;
        this.datas = datas;
    }
    //网格视图  元素的个数
    @Override
    public int getCount() {
        return datas.size();
    }
    //网格视图  对应item的数据
    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }
    //网格视图  对应item的id
    @Override
    public long getItemId(int position) {
        return position;
    }
    //构建对应的item的布局
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ClassInfo classInfo = datas.get(position);
        ViewHolder holder =null;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_classlist_item,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
       holder.tv_title.setText(classInfo.getName());
        switch (position%3){
            case 0:
                convertView.setBackgroundResource(R.drawable.selector_classlist_bg1);
                break;
            case 1:
                convertView.setBackgroundResource(R.drawable.selector_classlist_bg2);
                break;
            case 2:
                convertView.setBackgroundResource(R.drawable.selector_classlist_bg3);
                break;
        }
        return convertView;//返回组装好的item
    }

    public void addDatas(ArrayList<ClassInfo> datas) {
        this.datas.clear();//清空旧数据
        this.datas.addAll(datas);//添加新数据
    }

    class ViewHolder{
        private TextView tv_title;
        public ViewHolder(View convertView){
            this.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
        }
    }
}
