package com.example.justloginregistertest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;

public class playvideo extends AppCompatActivity implements View.OnClickListener{
    private VideoView videoView;
    private Button play;
    private Button pause;
    private Button replay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playvideo);
        videoView =  findViewById(R.id.video_view);
         play =  findViewById(R.id.playv);
         pause =  findViewById(R.id.pausev);
         replay =  findViewById(R.id.replayv);
        play.setOnClickListener(this);
        pause.setOnClickListener(this);
        replay.setOnClickListener(this);
        if (ContextCompat.checkSelfPermission(playvideo.this, Manifest.
                permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(playvideo.this, new String[]{
                    Manifest.permission. WRITE_EXTERNAL_STORAGE }, 1);
        } else {
            initVideoPath(); // 初始化VideoView
        }
    }
    private void initVideoPath() {
        File file = new File(Environment.getDataDirectory(), "Video/testvideo.mp4");
        videoView.setVideoPath(file.getPath()); // 指定视频文件的路径
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.
                        PERMISSION_GRANTED) {
                    initVideoPath();
                } else {
                    Toast.makeText(this, "拒绝权限将无法使用程序", Toast.LENGTH_SHORT).
                            show();
                    finish();
                }
                break;
            default:
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.playv:
                if (!videoView.isPlaying()) {
                    videoView.start(); // 开始播放
                     }
                    break;
                    case R.id.pausev:
                        if (videoView.isPlaying()) {
                            videoView.pause(); // 暂停播放
                        }
                        break;
                    case R.id.replayv:
                        if (videoView.isPlaying()) {
                            videoView.resume(); // 重新播放
                        }
                        break;
                }
        }
        @Override
        protected void onDestroy() {
            super.onDestroy();
            if (videoView != null) {
                videoView.suspend();
            }
        }
    }
