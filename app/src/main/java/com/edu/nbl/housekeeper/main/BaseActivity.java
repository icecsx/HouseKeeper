package com.edu.nbl.housekeeper.main;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.edu.nbl.housekeeper.R;
import com.edu.nbl.housekeeper.view.ActionBarView;

/**
 * Created by 世贤 on 2017/8/4.
 */

public class BaseActivity extends AppCompatActivity {
    private ActionBarView actionBarView;

    //不带动画的跳转
    public void startActivity(Class<?> name){
        Intent intent = new Intent(this,name);
        startActivity(intent);
    }
    //带动画的跳转
    public void startActivity(Class<?> name,int enterAnim,int exitAnim){
        Intent intent = new Intent(this,name);
        startActivity(intent);
        overridePendingTransition(enterAnim,exitAnim);
    }
    //初始化ActionBar
    public void initActionBar(String title, int leftResID, int rightResID, View.OnClickListener listener){
        actionBarView = (ActionBarView) findViewById(R.id.actionBar);
        actionBarView.initActionBar(title,leftResID,rightResID,listener);

    }

    protected Handler mainHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            myHandleMessage(msg);
        };
    };
    protected void myHandleMessage(Message msg) {
    };
}
