package com.edu.nbl.housekeeper.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.edu.nbl.housekeeper.R;
import com.edu.nbl.housekeeper.view.ActionBarView;

public class AboutActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initActionBar("关于我们",R.drawable.btn_homeasup_default,-1,listener);
    }
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.iv_left:
                    //Intent可以实现Activity之间的跳转
                    String className = getIntent().getStringExtra("className");//获取类名 SettingActivity
                    if (className==null||className.equals("")){//从主页面过来的
                        startActivity(MainActivity.class);
                        finish();//销毁当前页
                        return;//结束onClick方法，不会执行return下面的语句
                    }
                    if (className.equals(SettingActivity.class.getSimpleName())){
                        startActivity(SettingActivity.class);
                    }else {//不是从设置页面过来的
                        startActivity(MainActivity.class);
                    }
                    //startActivity(LogoActivity.class);
                    finish(); //销毁当前页
                    break;
            }
        }
    };
}
