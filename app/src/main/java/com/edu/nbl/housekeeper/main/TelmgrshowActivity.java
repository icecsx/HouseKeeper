package com.edu.nbl.housekeeper.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.edu.nbl.housekeeper.R;
import com.edu.nbl.housekeeper.adapter.TelListAdapter;
import com.edu.nbl.housekeeper.biz.DBTelManager;
import com.edu.nbl.housekeeper.entity.TableInfo;

import java.io.IOException;
import java.util.ArrayList;

public class TelmgrshowActivity extends BaseActivity {
    private ListView lv_number;//ListView
    private ArrayList<TableInfo> tableInfos;//数据
    private TelListAdapter adapter;//适配器
    private int idx;//下标代表不同的表

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_telmgrshow);
        //1.获取intent实例
        Intent intent = getIntent();
        //2.获取intent中存储的数据
        String title = intent.getStringExtra("title");
        idx = intent.getIntExtra("idx", 0);
        initActionBar(title, R.drawable.btn_homeasup_default, -1, listener);
        //1.初始化ListView
        lv_number = (ListView) findViewById(R.id.lv_number);
        //2.数据
        tableInfos = new ArrayList<>();
        //3.构建适配器
        adapter = new TelListAdapter(this, tableInfos);
        //4.设置适配器
        lv_number.setAdapter(adapter);
        //设置item的监听，拨打电话
        lv_number.setOnItemClickListener(onItemClickListener);
        //5.异步加载数据
        asyncTask();

    }

    private void asyncTask() {
        //构建子线程执行查询数据的耗时操作
        new Thread(new Runnable() {
            @Override
            public void run() {
                //获取查询的数据
                tableInfos = DBTelManager.readTable("table" + idx);
                runOnUiThread(new Runnable() {//回到主线程展示UI
                    @Override
                    public void run() {
                        //1.将数据添加到适配器集合中
                        adapter.addDatas(tableInfos);
                        //更新适配器
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }

    //点击返回键销毁当前页
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

    //item的监听，点击拨打电话
    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {

        /**
         *
         * @param parent    ListView
         * @param view      ListView对应位置的item布局
         * @param position  ListView对应item的位置
         * @param id        ListView对应item的id
         */
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //获取对应位置的item
            TableInfo tableInfo = (TableInfo) adapter.getItem(position);
            //获取号码
            long number = tableInfo.getNumber();
            //点击对应位置的item，取出电话号码，拨打电话
            Intent intent = new Intent(Intent.ACTION_CALL); //启动打电话意图
            intent.setData(Uri.parse("tel:" + number)); //设置电话号码
            if (ActivityCompat.checkSelfPermission(TelmgrshowActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            startActivity(intent);
        }
    };
}
