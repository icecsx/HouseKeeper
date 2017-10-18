package com.edu.nbl.housekeeper.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.edu.nbl.housekeeper.R;
import com.edu.nbl.housekeeper.adapter.LeadPagerAdapter;
import com.edu.nbl.housekeeper.service.MusicService;

public class LeadActivity extends BaseActivity {
    private ViewPager viewPager; //轮播视图
    private ImageView[] icons = new ImageView[3]; //放三个点
    private LeadPagerAdapter adapter; //轮播图适配器
    private ImageView[] iv_leads = new ImageView[3];//放三个ImageView的引导页
    private TextView tv_skip;//直接跳过的TextView
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //1.获取SharedPreferences实例
        SharedPreferences preferences = getSharedPreferences("lead_config",Context.MODE_PRIVATE);
        //2.取数据  取不到默认为true   也就是默认是第一次登录
        boolean isFirstRun = preferences.getBoolean("isFirstRun",true);
        if (!isFirstRun){//不是第一次登录  直接跳到LogoActivity
            startActivity(LogoActivity.class);
            finish();
        }else {//是第一次登录 显示引导页面 放歌
            setContentView(R.layout.activity_lead);
            //1.初始化控件
            initView();
            //2.构建数据
            initData();
            //3.设置适配器并监听
            setLeadAdapter();
            //启动服务放歌
            Intent intent = new Intent(this, MusicService.class);
            startService(intent);
        }

    }

    private void initView() {
        //1.初始化控件
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        icons[0] = (ImageView) findViewById(R.id.iv_icon0);
        icons[0].setImageResource(R.drawable.adware_style_selected);//一来就让第一个点变成有色的
        icons[1] = (ImageView) findViewById(R.id.iv_icon1);
        icons[2] = (ImageView) findViewById(R.id.iv_icon2);
        tv_skip = (TextView) findViewById(R.id.tv_skip);//直接跳过的TextView
        //给直接跳过添加监听器
        tv_skip.setOnClickListener(onClickListener);

    }
    //2.构建数据
    private void initData() {
        //三个ImageView
        ImageView iv_lead = null;
        //LayoutInflater.from(this)
        //从xml文件中实例化ImageView
        iv_lead = (ImageView) getLayoutInflater().inflate(R.layout.iv_item_lead,null);
        //设置ImageView的src属性
        iv_lead.setImageResource(R.drawable.adware_style_applist);
        //将带图片的ImageView放到数组中
        iv_leads[0] = iv_lead;
        //--------------------
        iv_lead = (ImageView) getLayoutInflater().inflate(R.layout.iv_item_lead,null);
        iv_lead.setImageResource(R.drawable.adware_style_banner);
        iv_leads[1] = iv_lead;
        //--------------------
        iv_lead = (ImageView) getLayoutInflater().inflate(R.layout.iv_item_lead,null);
        iv_lead.setImageResource(R.drawable.adware_style_creditswall);
        iv_leads[2] = iv_lead;
    }

    private void setLeadAdapter() {
        //3.构建适配器
        adapter = new LeadPagerAdapter(iv_leads); //将三张有图片的ImageView传过去
        //4.设置适配器
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(onPagerListener);
    }

    private ViewPager.OnPageChangeListener onPagerListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            Log.d("滑动","onPageScrolled");
        }
        //当页面改变时，要改变三个相应的圆点（圆点指示器）
        @Override
        public void onPageSelected(int position) {
            Log.d("滑动","onPageSelected");
            //先让所有的点都变成无色的
            for (int i=0;i<icons.length;i++){
                icons[i].setImageResource(R.drawable.adware_style_default);
                tv_skip.setVisibility(View.GONE);//每一个页面都不可见
            }
            //再让对应位置的点变成有色的
            icons[position].setImageResource(R.drawable.adware_style_selected);
            //当到了第三个页面的时候就可见
            if (position==2){
                tv_skip.setVisibility(View.VISIBLE);
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {
            Log.d("滑动","onPageScrollStateChanged");
        }
    };
    //跳转到LogoActivity页面（显示商标的页面）
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //点击直接跳过  存储登录状态  不是第一次登录了
            savePreferences();
            stopService();
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
        }
    };

    private void stopService() {
        Intent intentservice = new Intent(LeadActivity.this,MusicService.class);
        stopService(intentservice);
    }

    private void savePreferences() {
        //1.构建SharedPreferences,创建lead_config.xml文件
        SharedPreferences preferences = getSharedPreferences("lead_config", Context.MODE_PRIVATE);
        //2.构建Editor实例
        SharedPreferences.Editor editor = preferences.edit();
        //3.存储数据
        editor.putBoolean("isFirstRun",false);
        //4.提交
        editor.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService();
    }
}
