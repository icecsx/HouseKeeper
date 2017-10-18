package com.edu.nbl.housekeeper.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.edu.nbl.housekeeper.R;
import com.edu.nbl.housekeeper.entity.AppInfo;

import java.util.List;

/**
 * Created by 世贤 on 2017/8/11.
 * 显示应用程序的适配器
 * 1.继承BaseAdapter
 * 2.重写相关方法
 */

public class AppAdapter extends BaseAdapter{
    private List<AppInfo> datas;
    private Context context;//加载布局资源

    public AppAdapter(List<AppInfo> datas, Context context) {
        this.datas = datas;
        this.context = context;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView==null){
            //获取item的布局
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_app_item,null);
            holder = new ViewHolder();
            holder.cb_app = (CheckBox) convertView.findViewById(R.id.cb_app);
            holder.iv_app_icon = (ImageView) convertView.findViewById(R.id.iv_app);
            holder.tv_app_name = (TextView) convertView.findViewById(R.id.tv_app_name);
            holder.tv_app_packagename = (TextView) convertView.findViewById(R.id.tv_app_packagename);
            holder.tv_app_version = (TextView) convertView.findViewById(R.id.tv_app_version);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        //获取数据
        AppInfo appInfo = datas.get(position); //获取对应位置item的数据
        boolean isDel = appInfo.isDel();
        //获取图标
        Drawable drawable = appInfo.getPackageInfo().applicationInfo.loadIcon(context.getPackageManager());
        //获取应用程序名称
        String appName = appInfo.getPackageInfo().applicationInfo.loadLabel(context.getPackageManager()).toString();
        //获取应用程序包名
        String appPackageName = appInfo.getPackageInfo().packageName;
        //获取应用程序版本号
        String appVersion = appInfo.getPackageInfo().versionName;
        //放数据
        holder.cb_app.setChecked(isDel);
        holder.iv_app_icon.setImageDrawable(drawable);
        holder.tv_app_name.setText(appName);
        holder.tv_app_packagename.setText(appPackageName);
        holder.tv_app_version.setText(appVersion);
        //给CheckBox设置选中监听
        holder.cb_app.setOnCheckedChangeListener(checkChangeListener);
        holder.cb_app.setTag(position);//存储选中的checkBox位置
        return convertView;
    }
    //将获取的数据添加到适配器集合中
    public void addDatas(List<AppInfo> appInfos) {
        this.datas = appInfos;
    }
    //返回适配器集合中所有的数据
    public List<AppInfo> getDatasFromAdapter() {
        return datas;
    }

    //对item控件的优化
    class ViewHolder{
        CheckBox cb_app;//选择是否卸载
        ImageView iv_app_icon;//应用程序图标
        TextView tv_app_name;//应用程序名称
        TextView tv_app_packagename;//应用程序包名
        TextView tv_app_version;//应用程序版本
    }
    //卸载的checkBox监听
    CompoundButton.OnCheckedChangeListener checkChangeListener = new CompoundButton.OnCheckedChangeListener() {
        /**
         *
         * @param buttonView 指的是checkBox 点谁指谁
         * @param isChecked 表示是否选中
         */
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            int position = (int) buttonView.getTag();//获取之前存储的位置
            datas.get(position).setDel(isChecked);//设置对应位置的AppInfo是否勾选

        }
    };
}
