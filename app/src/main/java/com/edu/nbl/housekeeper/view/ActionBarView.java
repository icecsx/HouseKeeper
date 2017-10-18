package com.edu.nbl.housekeeper.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edu.nbl.housekeeper.R;

/**
 * Created by 世贤 on 2017/8/3.
 * 自定义视图
 * 1.继承
 * 2.添加构造方法
 */

public class ActionBarView extends LinearLayout {
    //声明控件
    private ImageView iv_left,iv_right;
    private TextView tv_title;

    //当初始化ActionBarView时调用
    public ActionBarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.layout_actionbar,this);
        iv_left = (ImageView) findViewById(R.id.iv_left);
        iv_right = (ImageView) findViewById(R.id.iv_right);
        tv_title = (TextView) findViewById(R.id.tv_title);

    }

    /**
     *
     * @param title actionBar中间标题
     * @param leftResID 左边的ImageView
     * @param rightResID 右边的ImageView
     * @param listener 给左右按钮设置监听器
     */
    public void initActionBar(String title,int leftResID,int rightResID,OnClickListener listener){
        //设置标题
        tv_title.setText(title);
        if (rightResID==-1){//如果传过来的id是-1，就不显示图片
            iv_right.setVisibility(View.INVISIBLE);
        }else {
            iv_right.setVisibility(View.VISIBLE);
            iv_right.setImageResource(rightResID);//在对应的页面显示对应的图片
        }
        iv_left.setImageResource(leftResID);//设置左边图片的资源
        //设置监听
        iv_left.setOnClickListener(listener);
        iv_right.setOnClickListener(listener);
    }
}
