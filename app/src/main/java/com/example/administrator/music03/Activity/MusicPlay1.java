package com.example.administrator.music03.Activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.example.administrator.music03.R;
import com.example.administrator.music03.customView.LycicView;
import com.example.administrator.music03.service.MusicPlayService;

import java.util.Timer;
import java.util.TimerTask;

public class MusicPlay1 extends AppCompatActivity
{
    private static SeekBar progressBar;
    private ImageView pre;
    private ImageView play;
    private ImageView next;
    private LycicView lrcview;

    private MusicPlayService.MusicControl musicBinder=null;
    private ServiceConnection connection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                      musicBinder=(MusicPlayService.MusicControl)iBinder;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };
    Handler handler=null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_play1);
    }

    public void initView()
    {
         progressBar=(SeekBar)findViewById(R.id.music_progress);
         pre=(ImageView)findViewById(R.id.playing_pre);
         play=(ImageView)findViewById(R.id.playing_play);
         next=(ImageView)findViewById(R.id.playing_next);
    }
    public void initHandler()
    {
        handler=new Handler()
        {
            @Override
            public void handleMessage(Message msg)
            {
                super.handleMessage(msg);
            }
        };
    }
    public void bindMusicService()
    {
         Intent intent=new Intent(this, MusicPlayService.class);
         bindService(intent,connection,BIND_AUTO_CREATE);
    }
    public void getProgressBar()
    {

    }
    public void getLRcView()
    {

    }
    public void setPlay()
    {

    }
    public void setNext()
    {

    }
    public void setPre()
    {

    }
}
