package com.edu.nbl.housekeeper.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.edu.nbl.housekeeper.R;

public class LogoActivity extends BaseActivity {
    private ImageView iv_logo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);
        iv_logo = (ImageView) findViewById(R.id.iv_logo);
        //加载动画文件
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.logo_alpha);
        animation.setAnimationListener(animationListener);
        iv_logo.startAnimation(animation);
    }

    private Animation.AnimationListener animationListener = new Animation.AnimationListener(){

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            //动画结束 跳转到主页面
            startActivity(MainActivity.class);
            finish();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };
}
