package com.edu.nbl.housekeeper.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.edu.nbl.housekeeper.R;
import com.edu.nbl.housekeeper.entity.FileInfo;
import com.edu.nbl.housekeeper.utils.CommonUtils;

import java.util.List;

/**
 * Created by 世贤 on 2017/8/22.
 */

public class FileAdapter extends BaseAdapter{
    private Context context;
    private List<FileInfo> datas;
    //构造方法初始化数据
    public FileAdapter(Context context, List<FileInfo> datas) {
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.layout_file_item,null);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        //把数据放到对应的控件上
        FileInfo fileInfo = datas.get(position);//根据位置获取一个数据
        String filename = fileInfo.getFile().getName();//获取文件名
        String filetime = CommonUtils.getStrTime(fileInfo.getFile().lastModified());//把long值的毫秒变成String
        String filesize = CommonUtils.getFileSize(fileInfo.getFile().length());//进行单位换算
        boolean isSelect =fileInfo.isSelect();//是否删除文件
        int icon = context.getResources().getIdentifier(fileInfo.getIconName(),"drawable",context.getPackageName());
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),icon);//把drawable下的资源变成bitmap
        //放数据
        holder.tv_file_name.setText(filename);
        holder.tv_file_time.setText(filetime);
        holder.tv_file_size.setText(filesize);
        holder.cb_file_del.setChecked(isSelect);//是否删除
        holder.iv_file_icon.setImageBitmap(bitmap);
        //设置CheckBox的监听器
        holder.cb_file_del.setOnCheckedChangeListener(onCheckedChangeListener);
        holder.cb_file_del.setTag(position);//设置状态改变的位置
        return convertView;
    }
    //获取适配器中所有数据
    public List<FileInfo> getDatasFromAdapter() {
        return datas;
    }

    public void addDatas(List<FileInfo> fileInfos) {
        datas.clear();
        datas.addAll(fileInfos);
    }

    //对控件的优化
    class ViewHolder{
        CheckBox cb_file_del;
        ImageView iv_file_icon;
        TextView tv_file_name,tv_file_time,tv_file_size;
        public ViewHolder(View convertView) {
            cb_file_del = (CheckBox) convertView.findViewById(R.id.cb_file_del);
            iv_file_icon= (ImageView) convertView.findViewById(R.id.iv_file_icon);
            tv_file_name = (TextView) convertView.findViewById(R.id.tv_file_name);
            tv_file_time = (TextView) convertView.findViewById(R.id.tv_file_time);
            tv_file_size = (TextView) convertView.findViewById(R.id.tv_file_size);
        }
    }
    private CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        /**
         *
         * @param buttonView  表示点击的那个CheckBox
         * @param isChecked   表示点击的CheckBox的选中状态
         */
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            int position = (int) buttonView.getTag();//获取状态改变的checkBox对应的item位置
            datas.get(position).setSelect(isChecked);

        }
    };
}
