package com.example.administrator.music03.Activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.music03.R;
import com.example.administrator.music03.Utils.Utility;
import com.example.administrator.music03.customView.LycicView;
import com.example.administrator.music03.entries.Music;
import com.example.administrator.music03.service.MusicPlayService;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class MusicPlay1 extends AppCompatActivity
{
    private static SeekBar progressBar;
    private TextView currentTime;
    private TextView totalTime;
    private ImageView pre;
    private ImageView play;
    private ImageView next;
    private LycicView lrcview;
    private ImageView disc;
    private ImageView discBig;

    private Music myMusic;
    private MusicPlayService.MusicControl musicBinder=null;
    private ServiceConnection connection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder)
        {
            musicBinder=(MusicPlayService.MusicControl)iBinder;
            setPlay();
            setNext();
            setPre();
            setDiscAndLrc();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };
    public static Handler handler=null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_play1);
        initView();
        bindMusicService();
        initHandler();
    }
    public void initView()
    {
         progressBar=(SeekBar)findViewById(R.id.music_progress);
         pre=(ImageView)findViewById(R.id.playing_pre);
         play=(ImageView)findViewById(R.id.playing_play);
         next=(ImageView)findViewById(R.id.playing_next);
         currentTime=(TextView)findViewById(R.id.currentTime);
         totalTime=(TextView)findViewById(R.id.totalTime);
         disc=(ImageView)findViewById(R.id.disc);
         discBig=(ImageView)findViewById(R.id.disc_bg);
         lrcview=(LycicView)findViewById(R.id.view);
    }
    public void initHandler()
    {
        handler=new Handler()
        {
            @Override
            public void handleMessage(Message msg)
            {
                super.handleMessage(msg);
                Bundle b=msg.getData();
                int duration=b.getInt("duration");
                int currentPosition=b.getInt("currentPosition");
                progressBar.setMax(duration);
                progressBar.setProgress(currentPosition);
                int currentTotalSeconds=currentPosition/1000;
                int minutes=currentTotalSeconds/60;
                int currentSeconds=currentTotalSeconds%60;

                int TotalSeconds=duration/1000;
                int TotalMinutes=TotalSeconds/60;
                int TotalLeftSeconds=TotalSeconds%60;

                currentTime.setText("0"+String.valueOf(minutes)+":"+currentSeconds);
                totalTime.setText("0"+String.valueOf(TotalMinutes)+":"+TotalLeftSeconds);
                lrcview.scrollToTime(currentSeconds,TotalSeconds);
            }
        };
    }
    public void bindMusicService()
    {
         Intent intent=new Intent(this, MusicPlayService.class);
         bindService(intent,connection,BIND_AUTO_CREATE);
    }
    public void setDiscAndLrc()
    {
             disc.setOnClickListener(new View.OnClickListener()
             {
                 @Override
                 public void onClick(View view) {
                     if(musicBinder.getMusic()!=null)
                     {
                         disc.setVisibility(View.INVISIBLE);
                         discBig.setVisibility(View.INVISIBLE);

                         showlrc();
                         lrcview.setVisibility(View.VISIBLE);
                     }
                 }
             });
             lrcview.setOnClickListener(new View.OnClickListener()
             {
                 @Override
                 public void onClick(View view) {
                     lrcview.setVisibility(View.INVISIBLE);
                     disc.setVisibility(View.VISIBLE);
                     discBig.setVisibility(View.VISIBLE);
                 }
             });
    }
    public void showlrc()
    {
        Music music = null;
        if (myMusic != null)
            music = myMusic;
        else
        {
            music = musicBinder.getMusic();
        }
        String path = Utility.localMusicPath + "/lyric/" + music.getCompleteMusicName();
        path = path.replace(".mp3", ".lrc");
        Log.d("lrcPath",path);
        File file = new File(path);
        if(!file.exists())
            Toast.makeText(this,"歌词不存在",Toast.LENGTH_SHORT).show();
        else {
            lrcview.getLrc(file);
            lrcview.initView();
        }
    }

    public void setPlay()
    {
        play.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent=getIntent();
                myMusic=(Music)intent.getSerializableExtra("music");
                if(musicBinder.getMusic()==null && myMusic!=null)
                {
                    musicBinder.setMusic(myMusic);
                    musicBinder.play();
                    play.setImageResource(R.drawable.pause);
                }
                else if(!musicBinder.isPlay() && musicBinder.getMusic()!=null)
                {
                    musicBinder.continuePlay();
                    play.setImageResource(R.drawable.pause);
                }
                else if(musicBinder.isPlay())
                {
                    musicBinder.pausePlay();
                    play.setImageResource(R.drawable.play);
                }
            }
        });
    }
    public void setNext()
    {
            if(musicBinder.getMusic()!=null)
            {
                musicBinder.playNextMusic();
            }
    }
    public void setPre()
    {
            if(musicBinder.getMusic()!=null)
            {
                musicBinder.playPreMusic();
            }
    }
}
