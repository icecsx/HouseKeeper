package com.edu.nbl.housekeeper.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.edu.nbl.housekeeper.R;
import com.edu.nbl.housekeeper.adapter.AppAdapter;
import com.edu.nbl.housekeeper.biz.AppInfoManager;
import com.edu.nbl.housekeeper.entity.AppInfo;

import java.util.ArrayList;
import java.util.List;

public class SoftmgrShowActivity extends BaseActivity {

    private int id;//所有软件、系统软件或用户软件相对布局的id
    private String title;//actionBar中间的标题
    private ListView lv_app;//列表视图
    private List<AppInfo> appInfos;//数据
    private AppAdapter appAdapter;//适配器
    private ProgressBar progressBar;//当数据加载时显示，加载完销毁
    private CheckBox cb_uninstall;//卸载按钮
    private Button btn_delall;//卸载所有按钮
    private MySoftReceiver deleteReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_softmgr_show);
        Intent intent = getIntent();
        id = intent.getIntExtra("id",R.id.rl_all_soft);
        //区分所有软件、系统软件和用户软件
        switch (id){
            case R.id.rl_all_soft:
                title = "所有软件";
                break;
            case R.id.rl_system_soft:
                title = "系统软件";
                break;
            case R.id.rl_user_soft:
                title = "用户软件";
                break;
        }
        //初始化actionBar
        initActionBar(title,R.drawable.btn_homeasup_default,-1,listener);
        //初始化视图
        initViews();
        //异步加载数据，子线程获取数据，主线程更新UI
        asyncLoadData();
        //设置广播监听器，监听广播的实时卸载
        initReceiver();
    }
    //注册广播
    private void initReceiver() {
        //1.意图过滤器
        IntentFilter filter = new IntentFilter();
        //2.设置广播标示
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        //3.注册广播
        deleteReceiver = new MySoftReceiver();
        //确定格式
        filter.addDataScheme("package");
        registerReceiver(deleteReceiver,filter);
    }

    //1.继承BroadcastReceiver
    class MySoftReceiver extends BroadcastReceiver{
    //2.重写onReceive
        @Override
        public void onReceive(Context context, Intent intent) {
            //只要卸载了软件就执行此方法
            //重新加载一下数据
            asyncLoadData();
        }
    }


    //初始化视图
    private void initViews() {
        //1.获取ListView
        lv_app = (ListView) findViewById(R.id.lv_app);
        //2.获取数据
        appInfos = new ArrayList<>();
        //假数据

        //3.构建适配器
        appAdapter = new AppAdapter(appInfos,this);
        //4.设置适配器
        lv_app.setAdapter(appAdapter);
        progressBar = (ProgressBar) findViewById(R.id.pb_app);//在加载数据时显示圆形的progressBar
        cb_uninstall = (CheckBox) findViewById(R.id.cb_app_all);//全选的checkBox
        btn_delall = (Button) findViewById(R.id.btn_app_uninstall);//点击卸载选中的app
        //设置监听器
        cb_uninstall.setOnCheckedChangeListener(checkedChangeListener);//全选
        btn_delall.setOnClickListener(listener);//卸载监听
    }

    private void asyncLoadData() {
        //开始加载数据，显示progressBar,不显示ListView
        progressBar.setVisibility(View.VISIBLE);
        lv_app.setVisibility(View.INVISIBLE);
        //构建子线程获取数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                switch (id){
                    case R.id.rl_all_soft:
                        appInfos = AppInfoManager.getAppInfoManager(SoftmgrShowActivity.this).getAllPackageInfo(true);//获取所有软件
                        Log.d("TAG","所有软件"+appInfos);
                        break;
                    case R.id.rl_system_soft:
                        appInfos = AppInfoManager.getAppInfoManager(SoftmgrShowActivity.this).getSystemPackageInfo(true);//获取系统软件
                        Log.d("TAG","系统软件"+appInfos);
                        break;
                    case R.id.rl_user_soft:
                        appInfos = AppInfoManager.getAppInfoManager(SoftmgrShowActivity.this).getUserPackageInfo(true);//获取用户软件
                        break;
                }
                //更新UI
                runOnUiThread(new Runnable() {//回到主线程
                    @Override
                    public void run() {
                        //数据获取完毕，隐藏progressBar
                        progressBar.setVisibility(View.INVISIBLE);
                        //显示ListView
                        lv_app.setVisibility(View.VISIBLE);
                        //将数据添加到适配器中
                        appAdapter.addDatas(appInfos);
                        //更新适配器
                        appAdapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }
    //全选监听
    private CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener(){
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            List<AppInfo> appInfos = appAdapter.getDatasFromAdapter();//获取适配器里所有数据
            for (AppInfo appInfo:appInfos){//遍历循环所有的AppInfo实例
                appInfo.setDel(isChecked);//所有checkBox与全选的checkBox的状态一致
            }
            appAdapter.notifyDataSetChanged();//更新适配器
        }
    };
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.iv_left://点击返回键
                    finish();//销毁当前页
                    break;
                case R.id.btn_app_uninstall://点击卸载对应选中的应用程序
                    List<AppInfo> appInfos = appAdapter.getDatasFromAdapter();//获取适配器里所有的数据
                    //遍历循环，筛选要卸载的程序
                    for (AppInfo appInfo:appInfos){
                        if (appInfo.isDel()){//需要卸载
                            String name = appInfo.getPackageInfo().packageName;//获取包名
                            Intent intent = new Intent(Intent.ACTION_DELETE);//卸载意图
                            intent.setData(Uri.parse("package:"+name));//设置要卸载的包
                            startActivity(intent);//启动组件
                        }
                    }
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(deleteReceiver);//撤销注册
    }
}
