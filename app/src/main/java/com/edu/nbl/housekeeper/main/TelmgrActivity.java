package com.edu.nbl.housekeeper.main;

import android.content.Context;
import android.content.Intent;
import android.icu.text.LocaleDisplayNames;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.GridView;

import com.edu.nbl.housekeeper.R;
import com.edu.nbl.housekeeper.adapter.ClassListAdapter;
import com.edu.nbl.housekeeper.biz.DBTelManager;
import com.edu.nbl.housekeeper.entity.ClassInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TelmgrActivity extends BaseActivity {
    private GridView gv_type;//网格视图
    private ArrayList<ClassInfo> datas;//数据
    private ClassListAdapter adapter;//适配器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_telmgr);
        //初始化actionBar
        initActionBar("通讯大全",R.drawable.btn_homeasup_default,-1,listener);
        //1.初始化
        gv_type = (GridView) findViewById(R.id.gv_type);
        //2.数据
        datas = new ArrayList<>();
        //3.构建适配器
        adapter = new ClassListAdapter(this,datas);
        //4.设置适配器
        gv_type.setAdapter(adapter);
        //5.GridView的item点击跳转
        gv_type.setOnItemClickListener(onItemClickListener);
        //异步加载数据  构建子线程获取数据
        asyncTask();
    }

    private void asyncTask() {
        //1.构建子线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //将assets/db目录下的commonnum.db文件拷贝到手机里data/data/包名/commonnum.db文件中
                    DBTelManager.readUpdateDB(getAssets().open("db/commonnum.db"));
                    //从手机里data/data/包名/commonnum.db文件中取出数据 {{订餐电话，1}，{公共服务，2}...}
                    datas = DBTelManager.readClassListTable();
                    runOnUiThread(new Runnable() {//回到主线程展示UI
                        @Override
                        public void run() {
                            //需要放到GridView上展示
                            //1.将数据添加到适配器集合中
                            adapter.addDatas(datas);
                            //更新适配器
                            adapter.notifyDataSetChanged();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    //左边返回键的监听
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_left:
                    finish();
                    break;
            }
        }
        };
    //GridView里item的监听
    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener(){

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //有数据  找列表对应位置的数据
            ClassInfo classInfo = (ClassInfo) adapter.getItem(position);
            //2.构建Bundle实例
            Bundle bundle = new Bundle();
            //3.放数据
            bundle.putString("title",classInfo.getName());
            bundle.putInt("idx",classInfo.getIdx());
            Intent intent = new Intent(TelmgrActivity.this,TelmgrshowActivity.class);
            //4.把bundle放到intent
            intent.putExtras(bundle);
            startActivity(intent);
        }
    };
}
