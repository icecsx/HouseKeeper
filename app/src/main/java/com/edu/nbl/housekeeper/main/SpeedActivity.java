package com.edu.nbl.housekeeper.main;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.edu.nbl.housekeeper.R;
import com.edu.nbl.housekeeper.adapter.ProcessAppAdapter;
import com.edu.nbl.housekeeper.biz.AppInfoManager;
import com.edu.nbl.housekeeper.biz.MemoryManager;
import com.edu.nbl.housekeeper.biz.SystemManager;
import com.edu.nbl.housekeeper.entity.RunningAppInfo;
import com.edu.nbl.housekeeper.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SpeedActivity extends BaseActivity {
    private TextView phone_name, phone_model,phone_ram;
    private ProgressBar pb_ram,pb_process;
    private Button btn_clean,btn_process;
    private ListView lv_process;
    private CheckBox cb_all_process;//全选按钮
    private ArrayList<RunningAppInfo> runningAppInfos;
    private Map<Integer, List<RunningAppInfo>> processAppInfos;
    private int state;//存储要显示的进程状态
    private ProcessAppAdapter appAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speed);
        initActionBar("手机加速",R.drawable.btn_homeasup_default,-1,listener);
        //初始化视图
        initViews();
        //初始化ListView数据
        initData();
        //初始化手机型号和手机版本
        initPhoneData();
        //初始化运行内存数据
        initRamData();
    }

    private void initRamData() {
        //获取全部的运行内存
        float totalRam = MemoryManager.getPhoneTotalRamMemory();
        //获取空闲的运行内存
        float freeRam = MemoryManager.getPhoneFreeRamMemory(this);
        //用过的
        float usedRam = totalRam-freeRam;
        //设置进度条
        float usedP = usedRam/totalRam;//已用的比例
        pb_ram.setMax(100);
        pb_ram.setProgress((int) (usedP*100));//设置进度条的百分比
        //设置TextView的比例
        phone_ram.setText("已用内存："+ CommonUtils.getFileSize((long) usedRam)+"/"+CommonUtils.getFileSize((long) totalRam));

    }

    private void initPhoneData() {
        phone_name.setText(SystemManager.getPhoneName().toUpperCase());//手机名称
        phone_model.setText(SystemManager.getPhoneModelName());//手机型号
    }

    //初始化视图
    private void initViews() {
        phone_name = (TextView) findViewById(R.id.tv_phone_name);
        phone_model = (TextView) findViewById(R.id.tv_phone_model);
        phone_ram = (TextView) findViewById(R.id.tv_ram);//手机的运行内存
        pb_ram = (ProgressBar) findViewById(R.id.pb_ram);//手机运行内存进度条
        pb_process = (ProgressBar) findViewById(R.id.pb_process);//数据加载的进度条
        btn_clean = (Button) findViewById(R.id.btn_clean);//一键清理按钮
        btn_process = (Button) findViewById(R.id.btn_process);//切换按钮
        cb_all_process = (CheckBox) findViewById(R.id.cb_all_process);//全选CheckBox
        //获取LiseView
        lv_process = (ListView) findViewById(R.id.lv_process);//进程ListView
        //2.数据
        runningAppInfos = new ArrayList<RunningAppInfo>();
        //3.构建适配器
        appAdapter = new ProcessAppAdapter(this, runningAppInfos);
        //4.设置适配器
        lv_process.setAdapter(appAdapter);
        //设置监听器
        btn_process.setOnClickListener(listener);//切换进程
        btn_clean.setOnClickListener(listener);//一键清理
        cb_all_process.setOnCheckedChangeListener(checkChangeListener);//全选
    }
    //初始化数据
    private void initData() {
        lv_process.setVisibility(View.INVISIBLE);
        pb_process.setVisibility(View.VISIBLE);
        //构建子线程去获取数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                //获取数据
                processAppInfos = AppInfoManager.getAppInfoManager(SpeedActivity.this).getRuningAppInfos();
                //回到主线程更新UI
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pb_process.setVisibility(View.INVISIBLE);
                        lv_process.setVisibility(View.VISIBLE);
                        //将数据放到适配器集合中
                        if (state==AppInfoManager.RUNING_APP_TYPE_USER){//显示用户的进程
                            List<RunningAppInfo> uesrapp = processAppInfos.get(AppInfoManager.RUNING_APP_TYPE_USER);//所有用户的进程
                            appAdapter.addDatas(uesrapp);
                            appAdapter.notifyDataSetChanged();//更新适配器
                            Log.d("TAG","数据已更新progressAppInfos="+processAppInfos);
                        }else if (state==AppInfoManager.RUNING_APP_TYPE_SYS){//显示系统的进程
                            List<RunningAppInfo> sysapp = processAppInfos.get(AppInfoManager.RUNING_APP_TYPE_SYS);//所有系统的进程
                            appAdapter.addDatas(sysapp);
                            appAdapter.notifyDataSetChanged();//更新适配器
                        }
                    }
                });
            }
        }).start();
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.iv_left://返回键
                    finish();
                    break;
                case R.id.btn_process://切换进程按钮
                    if (processAppInfos!=null){//此map集合包含系统进程和用户进程
                        switch (state){
                            case AppInfoManager.RUNING_APP_TYPE_USER://如果是用户进程，点击显示系统数据
                                appAdapter.addDatas(processAppInfos.get(AppInfoManager.RUNING_APP_TYPE_SYS));//获取系统进程，放入适配器集合
                                btn_process.setText("只显示用户进程");
                                state = AppInfoManager.RUNING_APP_TYPE_SYS;//当前是用户状态，点击完变成系统状态
                                break;
                            case AppInfoManager.RUNING_APP_TYPE_SYS://如果是系统进程，点击显示用户数据
                                appAdapter.addDatas(processAppInfos.get(AppInfoManager.RUNING_APP_TYPE_USER));//获取用户进程，放入适配器集合
                                btn_process.setText("显示系统进程");
                                state = AppInfoManager.RUNING_APP_TYPE_USER;//当前是系统状态，点击完变成用户统状态
                                break;
                        }
                        appAdapter.notifyDataSetChanged();
                        cb_all_process.setChecked(false);
                    }
                    break;
                case R.id.btn_clean://一键清理
                    //获取适配器中所有的item数据
                    List<RunningAppInfo> appInfos = appAdapter.getDataFromAdapter();
                    List<RunningAppInfo> newAppInfos = new ArrayList<>();
                    for (RunningAppInfo appInfo:appInfos){//遍历循环所有元素
                        if (appInfo.isClear()){//勾选上了
                            String packageName = appInfo.getPackageName();//获取包名
                            //所有相应包名的进程
                            AppInfoManager.getAppInfoManager(SpeedActivity.this).killProcesses(packageName);
                        }else {
                            newAppInfos.add(appInfo);
                        }
                    }
                    initData();//重新加载数据
                    cb_all_process.setChecked(false);
                    break;
            }
        }
    };
    //全选按钮
    private CompoundButton.OnCheckedChangeListener checkChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            List<RunningAppInfo> appInfos = appAdapter.getDataFromAdapter();//获取所有的item元素
            for (RunningAppInfo appInfo:appInfos){
                appInfo.setClear(isChecked);//把所有的CheckBox的勾选状态完成isChecked
            }
            //更新适配器
            appAdapter.notifyDataSetChanged();
        }
    };
}
