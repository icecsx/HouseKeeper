package com.edu.nbl.housekeeper.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuAdapter;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.edu.nbl.housekeeper.R;
import com.edu.nbl.housekeeper.biz.AppInfoManager;
import com.edu.nbl.housekeeper.biz.MemoryManager;
import com.edu.nbl.housekeeper.view.ActionBarView;
import com.edu.nbl.housekeeper.view.ClearArcView;

public class MainActivity extends BaseActivity {
    private TextView tv_rockt,tv_softmgr,tv_phonemgr,tv_telmgr,tv_filemgr,tv_sdclean;
    private ClearArcView cav_clear;//一键清理饼形图
    private ImageView iv_clear;//一键清理ImageView
    private TextView tv_clear;//一键清理文字
    private TextView tv_score;//一键清理得分

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initActionBar("安卓管家",R.drawable.ic_launcher,R.drawable.ic_child_configs,listener);
        initViews();
        //一键加速
        initScoreClear();
        //一来就要一键加速
        initScoreData();
    }

    private void initScoreClear() {
        cav_clear = (ClearArcView) findViewById(R.id.cav_score);
        iv_clear = (ImageView) findViewById(R.id.iv_score);
        tv_clear = (TextView) findViewById(R.id.tv_speedup);
        tv_score = (TextView) findViewById(R.id.tv_score);
        //监听器
        iv_clear.setOnClickListener(listener);
        tv_clear.setOnClickListener(listener);
    }

    private void initViews() {
        tv_rockt = (TextView) findViewById(R.id.tv_rocket);
        tv_softmgr = (TextView) findViewById(R.id.tv_softmgr);
        tv_phonemgr = (TextView) findViewById(R.id.tv_phonemgr);
        tv_telmgr = (TextView) findViewById(R.id.tv_telmgr);
        tv_filemgr = (TextView) findViewById(R.id.tv_filemgr);
        tv_sdclean = (TextView) findViewById(R.id.tv_sdclean);
        tv_rockt.setOnClickListener(listener);
        tv_softmgr.setOnClickListener(listener);
        tv_phonemgr.setOnClickListener(listener);
        tv_telmgr.setOnClickListener(listener);
        tv_filemgr.setOnClickListener(listener);
        tv_sdclean.setOnClickListener(listener);
    }

    //给左右设置监听
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.iv_left:
                    startActivity(AboutActivity.class,R.anim.left_in,R.anim.right_out);
                    break;
                case R.id.iv_right:
                    startActivity(SettingActivity.class,R.anim.right_in,R.anim.left_out);
                    break;
                case R.id.tv_rocket://手机加速
                    startActivity(SpeedActivity.class,R.anim.down_in,R.anim.up_out);
                    break;
                case R.id.tv_softmgr://软件管理
                    startActivity(SoftmgrActivity.class,R.anim.down_in,R.anim.up_out);
                    break;
                case R.id.tv_phonemgr://手机检测
                    startActivity(PhonemgrActivity.class,R.anim.down_in,R.anim.up_out);
                    break;
                case R.id.tv_telmgr://通讯大全
                    startActivity(TelmgrActivity.class,R.anim.down_in,R.anim.up_out);
                    break;
                case R.id.tv_filemgr://文件管理
                    startActivity(FilemgrActivity.class,R.anim.down_in,R.anim.up_out);
                    break;
                case R.id.tv_sdclean://垃圾清理
                    startActivity(SdcleanActivity.class,R.anim.down_in,R.anim.up_out);
                    break;
                case R.id.iv_score://一键清理图片
                case R.id.tv_speedup://一键清理加速
                    //按下按钮清除后台、服务、空进程
                    AppInfoManager.getAppInfoManager(MainActivity.this).killALLProcesses();//清除所有不需要的进程
                    initScoreData();
                    break;
            }
        }
    };
    //初始化得分数据
    private void initScoreData() {
        //获取全部的运行内存
        float totalRam = MemoryManager.getPhoneTotalRamMemory();
        //获取空闲的运行内存
        float freeRam = MemoryManager.getPhoneFreeRamMemory(this);
        //计算百分比
        float usedRam = totalRam-freeRam;
        float usedP = usedRam/totalRam; // 正在使用/全部
        int used100 = (int) (usedP*100);
        tv_score.setText(used100+"");//显示百分比
        //设置圆弧的进度
        //算角度
        int angle = (int) (usedP*360);
        cav_clear.setAngleWithAnim(angle);
    }
}
