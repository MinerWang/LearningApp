package com.example.justloginregistertest;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

public class MusicService {
    private static final File PATH = Environment.getDataDirectory();
    public List<String> musicList;
    public MediaPlayer player;
    public int songNum;
    public String songName;

    class MusicFilter implements FilenameFilter {
        public boolean accept(File dir, String name) {
            return (name.endsWith(".mp3"));
        }
    }

    public MusicService() {
        super();
        player = new MediaPlayer();
        musicList = new ArrayList<String>();
        try {
            File MUSIC_PATH = new File(PATH, "Music");
            if (MUSIC_PATH.listFiles(new MusicFilter()).length > 0) {
                for (File file : MUSIC_PATH.listFiles(new MusicFilter())) {
                    musicList.add(file.getAbsolutePath());
                }
            }
        } catch (Exception e) {
            Log.i("TAG", "读取文件异常");
        }

    }

    public void setPlayName(String dataSource) {
        File file = new File(dataSource);
        String name = file.getName();
        int index = name.lastIndexOf(".");
        songName = name.substring(0, index);
    }

    public void play() {
        try {
            player.reset();
            String dataSource = musicList.get(songNum);
            setPlayName(dataSource);//截取歌名
            // 指定参数为音频文件
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.setDataSource(dataSource);
            player.prepare();
            player.start();

            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer arg0) {
                    next();
                }
            });

        } catch (Exception e) {
            Log.v("MusicService", e.getMessage());
        }
    }


    public  void goPlay(){
        int position = getCurrentProgress();
        player.seekTo(position);
        try {
            player.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
        player.start();
    }

    public int getCurrentProgress() {
        if (player != null & player.isPlaying()) {
            return player.getCurrentPosition();
        } else if (player != null & (!player.isPlaying())) {
            return player.getCurrentPosition();
        }
        return 0;
    }

    public void next() {
        songNum = songNum == musicList.size() - 1 ? 0 : songNum + 1;
        play();
    }

    public void last() {
        songNum = songNum == 0 ? musicList.size() - 1 : songNum - 1;
        play();
    }
    // 暂停播放
    public void pause() {
        if (player != null && player.isPlaying()){
            player.pause();
        }
    }

    public void stop() {
        if (player != null && player.isPlaying()) {
            player.stop();
            player.reset();
        }
    }
}
