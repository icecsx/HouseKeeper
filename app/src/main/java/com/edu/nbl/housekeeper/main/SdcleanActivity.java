package com.edu.nbl.housekeeper.main;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.edu.nbl.housekeeper.R;
import com.edu.nbl.housekeeper.adapter.RubbishFileAdapter;
import com.edu.nbl.housekeeper.biz.DbClearPathManager;
import com.edu.nbl.housekeeper.biz.FileManager;
import com.edu.nbl.housekeeper.entity.RubbishFileInfo;
import com.edu.nbl.housekeeper.utils.CommonUtils;

public class SdcleanActivity extends BaseActivity {

    private ProgressBar pb_loading;
    private ListView lv_rubbishListview;
    private RubbishFileAdapter rubbishFileAdapter;
    private TextView tv_totalsize;
    private Button btn_clean;
    private CheckBox cb_clear_all;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sdclean);

        // 初始化ActionBar @see super class ActionBarActivity
        String title = getResources().getString(R.string.sdclean);
        initActionBar(title, R.drawable.btn_homeasup_default, -1, clickListener);
        //
        btn_clean = (Button) findViewById(R.id.btn_clearrubbish);
        btn_clean.setOnClickListener(clickListener);
        cb_clear_all = (CheckBox) findViewById(R.id.cb_all);
        cb_clear_all.setOnCheckedChangeListener(checkedChangeListener);
        tv_totalsize = (TextView) findViewById(R.id.tv_filesize);
        pb_loading = (ProgressBar) findViewById(R.id.progressBar);
        lv_rubbishListview = (ListView) findViewById(R.id.listviewRubbish);
        rubbishFileAdapter = new RubbishFileAdapter(this);
        lv_rubbishListview.setAdapter(rubbishFileAdapter);
        //
        try {
            asyncLoaddata();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private long totalSize = 0; // 用来保存总大小的变量
    @Override
    protected void myHandleMessage(Message msg) {
        // TODO Auto-generated method stub
        if (msg.what == 1) {
            long size = (Long) msg.obj;
            totalSize += size;
            tv_totalsize.setText(CommonUtils.getFileSize(totalSize));
        }
        if (msg.what == 2) {
            @SuppressWarnings("unchecked")
            ArrayList<RubbishFileInfo> rubbishFileInfos = (ArrayList<RubbishFileInfo>) msg.obj;
            pb_loading.setVisibility(View.INVISIBLE);
            lv_rubbishListview.setVisibility(View.VISIBLE);
            rubbishFileAdapter = new RubbishFileAdapter(SdcleanActivity.this);
            lv_rubbishListview.setAdapter(rubbishFileAdapter);
            rubbishFileAdapter.setDataToAdapter(rubbishFileInfos);
            rubbishFileAdapter.notifyDataSetChanged();
        }
    }

    private void asyncLoaddata() throws IOException {
        InputStream path = getResources().getAssets().open("db/clearpath.db");
        DbClearPathManager.readUpdateDB(path);//拷贝到手机中
        final ArrayList<RubbishFileInfo> rubbishFileInfos = DbClearPathManager.getPhoneRubbishfile(getApplicationContext());
        Log.d("TAG","rubbishFileInfos="+rubbishFileInfos);
        totalSize = 0;
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (RubbishFileInfo rubbishFileInfo : rubbishFileInfos) {
                    File file = new File(rubbishFileInfo.getFilepath());
                    long size = FileManager.getFileSize(file);
                    rubbishFileInfo.setSize(size);
                    // 更新全部大小
                    Message msg = mainHandler.obtainMessage();
                    msg.what = 1;
                    msg.obj = size;
                    mainHandler.sendMessage(msg);
                }
                // 全部加载完毕 更新UI
                Message msg = mainHandler.obtainMessage();
                msg.what = 2;
                msg.obj = rubbishFileInfos;
                mainHandler.sendMessage(msg);
            }
        }).start();
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int viewID = v.getId();
            switch (viewID) {
                case R.id.iv_left:
                    finish();
                    break;
                case R.id.btn_clearrubbish:
                    clearRubbish();
                    break;
            }
        }
    };
    //全选
    private CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            List<RubbishFileInfo> rubbishFileInfos = rubbishFileAdapter.getDatasFromAdapter();//获取适配器里所有数据
            for (int i=0;i<rubbishFileInfos.size();i++){
                RubbishFileInfo rubbishFileInfo = rubbishFileInfos.get(i);
                rubbishFileInfo.setChecked(isChecked);
            }
            rubbishFileAdapter.notifyDataSetChanged();
        }
    };
    //一键清理
    private void clearRubbish() {
        //清除列表里的东西
        List<RubbishFileInfo> datas = rubbishFileAdapter.getDatasFromAdapter();
        for (int i=0;i<datas.size();i++){
            RubbishFileInfo rubbishFileInfo = datas.get(i);
            if (rubbishFileInfo.isChecked()){
                rubbishFileAdapter.removeData(rubbishFileInfo);
            }
        }
        rubbishFileAdapter.notifyDataSetChanged();
    }
}
