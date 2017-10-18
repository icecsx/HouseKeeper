package com.edu.nbl.housekeeper.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.edu.nbl.housekeeper.R;
import com.edu.nbl.housekeeper.biz.MemoryManager;
import com.edu.nbl.housekeeper.utils.CommonUtils;
import com.edu.nbl.housekeeper.view.PiechartView;

import java.security.cert.CertificateNotYetValidException;
import java.text.DecimalFormat;

public class SoftmgrActivity extends BaseActivity {
    private RelativeLayout all_soft,system_soft,user_soft;
    private ProgressBar phoneSpace,sdcardSpace;//手机内置空间的进度 ， 外置
    private TextView phoneSpaceMsg,sdcardSpaceMsg;//手机内置空间信息， 外置
    private PiechartView piechartView;//饼形图
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_softmgr);
        //初始化ActionBar
        initActionBar("软件管理",R.drawable.btn_homeasup_default,-1,listener);
        //初始化视图
        initViews();
        //初始化数据 把对应的数据放到对应的控件上
        initData();
    }

    private void initData() {
        //手机自身空间
        long phoneSelfTotal = MemoryManager.getPhoneSelfSize();//获取system+cache目录总容量的和
        //手机自身未使用的空间
        long phoneSelfUnused = MemoryManager.getPhoneSelfFreeSize();//获取system+cache目录空闲容量的总和
        //手机已使用的空间
        long phoneSelfUsed = phoneSelfTotal-phoneSelfUnused;
        //获取手机内置的sdcard容量
        long phoneSelfSDCardTotal = MemoryManager.getPhoneSelfSDCardSize();//mnt/shell/emulated/0
        //获取手机内置的sdcard未使用的容量
        long phoneSelfSDCardUnused = MemoryManager.getPhoneSelfSDCardFreeSize();
        //获取手机内置的sdcard已使用的容量
        long phoneSelfSDCardUsed = phoneSelfSDCardTotal-phoneSelfSDCardUnused;
        //手机自带的全部内存
        long phoneTotalSpace = phoneSelfTotal+phoneSelfSDCardTotal;//system+cache+mnt
        //手机自带的全部未使用内存
        long phoneUnusedSpace = phoneSelfUnused+phoneSelfSDCardUnused;
        //手机自带的全部已使用内存
        long phoneUsedSpace = phoneSelfUsed+phoneSelfSDCardUsed;
        //获取外置sdcardz总容量
        long phoneOutSDCardTotal = MemoryManager.getPhoneOutSDCardSize(this);
        //获取外置sdcard未使用的容量
        long phoneOutSDCardUnused = MemoryManager.getPhoneOutSDCardFreeSize(this);
        //获取外置sdcard已使用的容量
        long phoneOutSDCardUsed = phoneOutSDCardTotal-phoneOutSDCardUnused;
        //设置手机内置空间的进度条
        phoneSpace.setMax((int) (phoneTotalSpace/1024));//进度条总容量
        phoneSpace.setProgress((int) (phoneUsedSpace/1024));//设置进度条已使用的进度
        //设置手机外置空间进度条
        sdcardSpace.setMax((int) (phoneOutSDCardTotal/1024));
        sdcardSpace.setProgress((int) (phoneOutSDCardUnused/1024));
        //设置空间使用情况
        phoneSpaceMsg.setText("可用："+ CommonUtils.getFileSize(phoneUnusedSpace)+" / "+ CommonUtils.getFileSize(phoneTotalSpace));
        sdcardSpaceMsg.setText("可用："+ CommonUtils.getFileSize(phoneOutSDCardUnused)+" / "+CommonUtils.getFileSize(phoneOutSDCardTotal));
        //画饼形图
        //手机总空间
        float phoneAllSpace = phoneTotalSpace+phoneOutSDCardTotal;
        //因为没有外置sdcard 所以手机外置的sdcard存储空间是0
        //计算百分比
        float phonePercent = phoneTotalSpace/phoneAllSpace;//手机内置空间百分比
        float sdcardPercent = phoneOutSDCardTotal/phoneAllSpace;//手机外置空间百分比
        //保留小数点后两位
        DecimalFormat df = new DecimalFormat("#.00");
        phonePercent = Float.parseFloat(df.format(phonePercent));
        sdcardPercent = Float.parseFloat(df.format(sdcardPercent));
        //设置饼形图比例
        piechartView.setPiechartProportionWithAnim(phonePercent,sdcardPercent);
    }

    private void initViews() {
        all_soft = (RelativeLayout) findViewById(R.id.rl_all_soft);
        system_soft = (RelativeLayout) findViewById(R.id.rl_system_soft);
        user_soft = (RelativeLayout) findViewById(R.id.rl_user_soft);
        phoneSpace = (ProgressBar) findViewById(R.id.pb_phone);
        sdcardSpace = (ProgressBar) findViewById(R.id.pb_sdcard);
        phoneSpaceMsg = (TextView) findViewById(R.id.tv_phone);
        sdcardSpaceMsg = (TextView) findViewById(R.id.tv_sdcard);
        piechartView = (PiechartView) findViewById(R.id.piechart);
        all_soft.setOnClickListener(listener);
        system_soft.setOnClickListener(listener);
        user_soft.setOnClickListener(listener);
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.iv_left://返回键
                    finish();
                    break;
                case R.id.rl_all_soft://所有软件
                case R.id.rl_system_soft://系统软件
                case R.id.rl_user_soft://用户软件
                    Bundle bundle = new Bundle();
                    bundle.putInt("id",v.getId());//把对应点击的item的id传过去，区分所有软件、系统软件还是用户软件
                    Intent intent = new Intent(SoftmgrActivity.this,SoftmgrShowActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
            }
        }
    };
}
