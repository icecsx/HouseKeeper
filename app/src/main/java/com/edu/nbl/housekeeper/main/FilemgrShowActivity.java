package com.edu.nbl.housekeeper.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.edu.nbl.housekeeper.R;
import com.edu.nbl.housekeeper.adapter.FileAdapter;
import com.edu.nbl.housekeeper.biz.FileManager;
import com.edu.nbl.housekeeper.entity.FileInfo;
import com.edu.nbl.housekeeper.utils.CommonUtils;
import com.edu.nbl.housekeeper.utils.FileTypeUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FilemgrShowActivity extends BaseActivity {
    private int id;//用id区分当前显示的页面
    private TextView tv_file_number,tv_file_size;
    private Button btn_file_delete;
    private ListView lv_file;
    private List<FileInfo> fileInfos;
    private FileAdapter fileAdapter;
    private String title;
    private long fileSize;
    private int fileNumber;//文件个数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filemgr_show);
        //初始化控件

        initViews();
        //初始化数据
        initDatas();
        //初始化ActionBar
        initActionBar(title,R.drawable.btn_homeasup_default,-1,listener);
        //把数据放到对应的控件上
        setDatas();
    }

    //初始化相关成员变量
    private void initViews() {
        id = getIntent().getIntExtra("id",-1);//根据id获取对应点击的控件
        Log.d("ID","id="+Integer.toHexString(id));
        tv_file_number = (TextView) findViewById(R.id.tv_file_number);//文件数量
        tv_file_size = (TextView) findViewById(R.id.tv_file_size);//文件大小
        btn_file_delete = (Button) findViewById(R.id.btn_file_delete);//删除按钮
        btn_file_delete.setOnClickListener(listener);
        lv_file = (ListView) findViewById(R.id.lv_file);//列表视图
        lv_file.setOnItemClickListener(itemClickListener);
    }

    private void initDatas() {
        //通过id来区分要显示的标题
        switch (id){
            case R.id.rl_file_all:
                title = "全部";
                fileInfos = FileManager.getFileManager().getAllFileList();//获取全部文件
                fileSize = FileManager.getFileManager().getAllFileSize();
                Log.d("FILE","fileInfos="+fileInfos);
                break;
            case R.id.rl_file_text:
                title = "文档";
                fileInfos = FileManager.getFileManager().getTextFileList();//获取文档文件
                fileSize = FileManager.getFileManager().getTextFileSize();
                Log.d("FILE","fileInfos="+fileInfos);
                break;
            case R.id.rl_file_video:
                title = "视频";
                fileInfos = FileManager.getFileManager().getVideoFileList();//获取视频文件
                fileSize = FileManager.getFileManager().getVideoFileSize();
                Log.d("FILE","fileInfos="+fileInfos);
                break;
            case R.id.rl_file_audio:
                title = "音频";
                fileInfos = FileManager.getFileManager().getAudioFileList();//获取音频文件
                fileSize = FileManager.getFileManager().getAudioFileSize();
                Log.d("FILE","fileInfos="+fileInfos);
                break;
            case R.id.rl_file_image:
                title = "图像";
                fileInfos = FileManager.getFileManager().getImageFileList();//获取图像文件
                fileSize = FileManager.getFileManager().getImageFileSize();
                Log.d("FILE","fileInfos="+fileInfos);
                break;
            case R.id.rl_file_zip:
                title = "压缩包";
                fileInfos = FileManager.getFileManager().getZipFileList();//获取压缩包文件
                fileSize = FileManager.getFileManager().getZipFileSize();
                Log.d("FILE","fileInfos="+fileInfos);
                break;
            case R.id.rl_file_apk:
                title = "程序包";
                fileInfos = FileManager.getFileManager().getApkFileList();//获取程序包文件
                fileSize = FileManager.getFileManager().getApkFileSize();
                Log.d("FILE","fileInfos="+fileInfos);
                break;
        }
        fileNumber = fileInfos.size();//文件个数
    }
    //将数据放到对应的控件上
    private void setDatas() {
        tv_file_number.setText(fileNumber+"");//文件个数
        tv_file_size.setText(CommonUtils.getFileSize(fileSize));//占用空间
        //构建适配器放置ListView数据
        Log.d("DATA","fileInfos="+fileInfos);
        fileAdapter = new FileAdapter(this,fileInfos);
        //设置适配器
        lv_file.setAdapter(fileAdapter);
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.iv_left:
                    finish();
                    break;
                case R.id.btn_file_delete://点击删除文件
                    deleteFiles();
            }
        }
    };
    //删除文件
    private void deleteFiles() {
        //获取所有文件
        List<FileInfo> allFileInfos = fileAdapter.getDatasFromAdapter();
        //要删除的文件集合
        List<FileInfo> delFileInfos = new ArrayList<>();
        for (int i = 0;i<allFileInfos.size();i++){//遍历所有文件
            FileInfo fileInfo = allFileInfos.get(i);//拿到一个文件
            if (fileInfo.isSelect()){//是否选中
                //选中
                delFileInfos.add(fileInfo);
            }
        }
        //把要删除的集合里所有文件删除
        for (int i = 0;i<delFileInfos.size();i++){
            FileInfo fileInfo = delFileInfos.get(i);
            String fileType = fileInfo.getFileType();//获取文件类型
            File file = fileInfo.getFile();
            long size=file.length();//删除文件的大小
            if (file.delete()){
                //删除成功 更新数据
                fileAdapter.getDatasFromAdapter().remove(fileInfo);//删除适配器集合中的元素
                FileManager.getFileManager().setAllFileSize(FileManager.getFileManager().getAllFileSize()-size);//设置最新文件大小
                fileSize = FileManager.getFileManager().getAllFileSize();//获取最新文件大小
                FileManager.getFileManager().getAllFileList().remove(fileInfo);//真正移除
                switch (fileType){
                    case "txt"://文档
                        FileManager.getFileManager().getTextFileList().remove(fileInfo);//删除文档中的集合
                        break;
                    case "video":
                        FileManager.getFileManager().getVideoFileList().remove(fileInfo);//删除视频中的集合
                        break;
                    case "audio":
                        FileManager.getFileManager().getAudioFileList().remove(fileInfo);//删除音频中的集合
                        break;
                    case "image":
                        FileManager.getFileManager().getImageFileList().remove(fileInfo);//删除图像中的集合
                        break;
                    case "zip":
                        FileManager.getFileManager().getZipFileList().remove(fileInfo);//删除压缩包中的集合
                        break;
                    case "apk":
                        FileManager.getFileManager().getApkFileList().remove(fileInfo);//删除程序包中的集合
                        break;
                }
                switch (id){
                    case R.id.rl_file_text:
                        FileManager.getFileManager().setTextFileSize(FileManager.getFileManager().getTextFileSize()-size);//设置文档文件大小
                        fileSize = FileManager.getFileManager().getTextFileSize();//获取最新文档文件大小
                        break;
                    case R.id.rl_file_video:
                        FileManager.getFileManager().setVideoFileSize(FileManager.getFileManager().getVideoFileSize()-size);//设置视频文件大小
                        fileSize = FileManager.getFileManager().getVideoFileSize();//获取最新视频文件大小
                        break;
                    case R.id.rl_file_audio:
                        FileManager.getFileManager().setAudioFileSize(FileManager.getFileManager().getAudioFileSize()-size);//设置音频文件大小
                        fileSize = FileManager.getFileManager().getAudioFileSize();//获取最新音频文件大小
                        break;
                    case R.id.rl_file_image:
                        FileManager.getFileManager().setImageFileSize(FileManager.getFileManager().getImageFileSize()-size);//设置图像文件大小
                        fileSize = FileManager.getFileManager().getImageFileSize();//获取最新图像文件大小
                        break;
                    case R.id.rl_file_zip:
                        FileManager.getFileManager().setZipFileSize(FileManager.getFileManager().getZipFileSize()-size);//设置压缩包文件大小
                        fileSize = FileManager.getFileManager().getZipFileSize();//获取最新压缩包文件大小
                        break;
                    case R.id.rl_file_apk:
                        FileManager.getFileManager().setApkFileSize(FileManager.getFileManager().getApkFileSize()-size);//设置程序包文件大小
                        fileSize = FileManager.getFileManager().getApkFileSize();//获取最新程序包文件大小
                        break;
                }
                //更新UI
                fileAdapter.notifyDataSetChanged();
                //更新文件数量
                fileNumber = fileAdapter.getDatasFromAdapter().size();
                tv_file_number.setText(fileNumber+"个");
                //更新文件大小
                tv_file_size.setText(CommonUtils.getFileSize(fileSize));
                System.gc();//主动调用垃圾回收器回收垃圾
                Thread.yield();//放弃线程当前的执行权
            }
        }
    }
    //列表的监听器 调用系统组件打开相应的文件
    private AdapterView.OnItemClickListener itemClickListener =  new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            FileInfo fileInfo = (FileInfo) fileAdapter.getItem(position);//获取点击的item
            File file = fileInfo.getFile();//获取点击的文件
            String type = FileTypeUtil.getMIMEType(file);
            //通过mime指定组件打开文件
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);//启动组件打开文件
            intent.setDataAndType(Uri.fromFile(file),type);//设置数据和类型
            startActivity(intent);
        }
    };
}
