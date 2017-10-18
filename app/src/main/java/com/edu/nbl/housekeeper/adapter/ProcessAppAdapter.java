package com.edu.nbl.housekeeper.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.edu.nbl.housekeeper.R;
import com.edu.nbl.housekeeper.entity.RunningAppInfo;
import com.edu.nbl.housekeeper.utils.CommonUtils;

import java.util.List;

/**
 * Created by 世贤 on 2017/8/15.
 * 1.继承
 * 2.重写
 * 3.在getView方法中填充item布局并给对应控件放置数据
 */

public class ProcessAppAdapter extends BaseAdapter{
    private Context context;
    private List<RunningAppInfo> datas;//数据
    //构造方法对成员变量初始化
    public ProcessAppAdapter(Context context, List<RunningAppInfo> datas) {
        this.context = context;
        this.datas = datas;
    }
    //数据的对象
    @Override
    public int getCount() {
        return datas.size();
    }
    //根据位置获取对应的数据
    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }
    //获取item的id
    @Override
    public long getItemId(int position) {
        return position;
    }
    //获取otem布局放置数据
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView==null){
            //获取item布局
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_process_item,null);
            //构建ViewHolder实例
            holder = new ViewHolder();
            //实例化item的控件
            holder.cb_process = (CheckBox) convertView.findViewById(R.id.cb_process);
            holder.iv_process_icon = (ImageView) convertView.findViewById(R.id.iv_process_icon);
            holder.tv_process_name = (TextView) convertView.findViewById(R.id.tv_process_name);
            holder.tv_process_ram = (TextView) convertView.findViewById(R.id.tv_process_ram);
            holder.tv_process_show = (TextView) convertView.findViewById(R.id.tv_process_show);
            //存储holder
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        //放置数据
        RunningAppInfo runningAppInfo = datas.get(position);//获取对应位置的数据
        holder.cb_process.setChecked(runningAppInfo.isClear());//设置是否清除
        holder.iv_process_icon.setImageDrawable(runningAppInfo.getIcon());//设置图标
        holder.tv_process_name.setText(runningAppInfo.getLableName());//设置应用程序名称
        holder.tv_process_ram.setText("内存："+ CommonUtils.getFileSize(runningAppInfo.getSize()));//设置运行内存
        holder.tv_process_show.setText(runningAppInfo.isSystem()?"系统进程":"");//判断是否是系统进程
        //设置是否清理监听
        holder.cb_process.setOnCheckedChangeListener(checkChangeListener);
        holder.cb_process.setTag(position);//给每个CheckBox设置位置
        return convertView;
    }

    private CompoundButton.OnCheckedChangeListener checkChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            CheckBox checkBox = (CheckBox) buttonView;//点谁指谁
            int position = (int) checkBox.getTag();//拿到点击的CheckBox的位置
            datas.get(position).setClear(isChecked);//更新当前的CheckBox是否勾选
        }
    };

    public void addDatas(List<RunningAppInfo> datas) {
        this.datas.clear();//清空旧数据
        this.datas.addAll(datas);//添加新数据
    }
    //返回适配器中所有数据
    public List<RunningAppInfo> getDataFromAdapter() {
        return datas;
    }

    //ViewHolder优化item的控件
    class ViewHolder{
        CheckBox cb_process;//勾选checkBox
        ImageView iv_process_icon;//进程应用程序的图标
        TextView tv_process_name;//进程应用名称
        TextView tv_process_ram;//进程的运行内存
        TextView tv_process_show;//是否是系统进程


    }
}
