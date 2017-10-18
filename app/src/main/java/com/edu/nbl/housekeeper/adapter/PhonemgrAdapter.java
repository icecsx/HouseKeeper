package com.edu.nbl.housekeeper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.edu.nbl.housekeeper.R;
import com.edu.nbl.housekeeper.entity.PhoneInfo;

import java.util.List;

/**
 * Created by 世贤 on 2017/8/17.
 * 手机检测里的适配器
 * 1.继承
 * 2.重写相关方法
 * 3.在getView方法中，去向item布局中填充数据
 */

public class PhonemgrAdapter extends BaseAdapter {
    private Context context;//上下文
    private List<PhoneInfo> datas;//数据

    //初始化
    public PhonemgrAdapter(Context context, List<PhoneInfo> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //把数据和item布局进行组装
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //获取item布局
        convertView = LayoutInflater.from(context).inflate(R.layout.phone_param_list_item, null);//item的布局
        //2.获取item布局上的控件
        ImageView iv_icon = (ImageView) convertView.findViewById(R.id.iv_phoneparam_icon);
        TextView tv_title  = (TextView) convertView.findViewById(R.id.tv_phoneparam_title);
        TextView tv_text = (TextView) convertView.findViewById(R.id.tv_phoneparam_text);
        //3.获取数据
        PhoneInfo phoneInfo = (PhoneInfo) getItem(position);
        //4.放置数据
        iv_icon.setImageDrawable(phoneInfo.getIcon());
        tv_title.setText(phoneInfo.getTitle());
        tv_text.setText(phoneInfo.getText());
        //给3取余设置不同的颜色
        switch (position%3){
            case 0:
                iv_icon.setBackgroundResource(R.drawable.shape_green_rect);
                break;
            case 1:
                iv_icon.setBackgroundResource(R.drawable.shape_red_rect);
                break;
            case 2:
                iv_icon.setBackgroundResource(R.drawable.shape_yellow_rect);
                break;
        }
        //返回组装好的item
        return convertView;
    }

    public void addData(PhoneInfo phoneInfo) {
        datas.add(phoneInfo);
    }
}
