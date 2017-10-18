package com.edu.nbl.housekeeper.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.edu.nbl.housekeeper.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 世贤 on 2017/8/10.
 * 自定义饼形图，需要画三个圆弧
 * 1.继承
 * 2.添加有效的构造方法
 * 3.重写onMeasure方法
 * 4.重写onDraw方法
 */

public class PiechartView extends View {
    private Paint paint;//画笔
    private RectF oval;//限制圆形的大小
    private float proportionPhone;//手机内置空间所占比例
    private float proportionSD;//手机外置空间所占比例
    //要把比例换算成角度
    private float piechartAnalePhone=0;//手机内置空间的角度
    private float piechartAnaleSD = 0;//手机外置空间的角度
    private int phoneColor=0;//手机内置空间的颜色
    private int sdColor=0;//手机外置空间的颜色
    private int baseColor=0;//饼形图底部颜色
    private float sdcardTargetAngle;
    private float phoneTargetAngle;

    public PiechartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint=new Paint();
        phoneColor=context.getResources().getColor(R.color.phone_color);
        sdColor=context.getResources().getColor(R.color.sd_color);
        baseColor=context.getResources().getColor(R.color.base_color);
    }
    public void setPiechartProportion(float proportionPhone, float proportionSD){
        //初始化比例
        this.proportionPhone = proportionPhone;
        this.proportionSD = proportionSD;
        //把比例换算成角度
        float phoneTargetAngle = 360*this.proportionPhone;
        float sdcardTargetAngle = 360*this.proportionSD;
        //初始化要绘制的角度
        piechartAnalePhone=phoneTargetAngle;
        piechartAnaleSD=sdcardTargetAngle;
        postInvalidate();//重绘
    }
    //带动画，画饼形图
    public void setPiechartProportionWithAnim(float proportionPhone,float proportionSD){
        this.proportionPhone = proportionPhone;
        this.proportionSD = proportionSD;
        //把比例换算成角度
        phoneTargetAngle = 360*this.proportionPhone;
        sdcardTargetAngle = 360*this.proportionSD;
        //写一个循环（定时器），每26ms执行一次，每次加4°
        final Timer timer = new Timer();
        //初始化一个定时任务
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                // 执行任务 每26ms加4°
                piechartAnalePhone+=4;
                piechartAnaleSD+=4;
                postInvalidate();
                if (piechartAnalePhone>=phoneTargetAngle){
                    piechartAnalePhone=phoneTargetAngle;//将角度固定下来
                }
                if (piechartAnaleSD>=sdcardTargetAngle){
                    piechartAnaleSD=sdcardTargetAngle;//将角度固定下来
                }
                if (piechartAnalePhone==phoneTargetAngle&&piechartAnaleSD==sdcardTargetAngle){
                    timer.cancel();
                }
            }
        };
        timer.schedule(timerTask,28,26);//tinmeTask是要调度的任务  28是第一次执行任务要的时间 26表示每26ms执行一次run方法

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int viewWidth = MeasureSpec.getSize(widthMeasureSpec);//获取要绘制的宽
        int viewHeight = MeasureSpec.getSize(heightMeasureSpec);//获取要绘制的高
        oval=new RectF(0,0,viewWidth,viewHeight);//限制圆形的大小
        setMeasuredDimension(viewWidth,viewHeight);//将测量的尺寸告知系统底层
    }

    @Override
    protected void onDraw(Canvas canvas) {
        paint.setAntiAlias(true);//设置抗锯齿
        //先画底部
        paint.setColor(baseColor);//设置画笔颜色
        canvas.drawArc(oval,-90,360,true,paint);//画底部圆环
        //画手机内置空间圆弧
        paint.setColor(phoneColor);
        canvas.drawArc(oval,-90,piechartAnalePhone,true,paint);
        //画手机外置空间圆弧
        paint.setColor(sdColor);
        canvas.drawArc(oval,-90+piechartAnalePhone,piechartAnaleSD,true,paint);
    }
}
