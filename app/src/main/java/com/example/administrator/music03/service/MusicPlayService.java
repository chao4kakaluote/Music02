package com.example.administrator.music03.service;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import com.example.administrator.music03.Activity.MusicPlay;
import com.example.administrator.music03.Utils.Utility;
import com.example.administrator.music03.entries.Music;
import java.util.Timer;
import java.util.TimerTask;
public class MusicPlayService extends Service
{
    public Music preMusic;
    public Music myMusic;
    private MediaPlayer myMediaPlayer;
    private MusicControl musicControl=null;
    private static Timer timer;
    public boolean isPlaying=false;
    public MusicPlayService()
    {
    }
    public void setMusic(Music music)
    {
        if(this.myMusic!=null)
            preMusic=myMusic;
        this.myMusic=music;
    }
    public void Play()
    {
        try
        {
            myMediaPlayer=new MediaPlayer();
            myMediaPlayer.reset();
            Log.d("url",Utility.localMusicPath + "/mp3/" + myMusic.getCompleteMusicName());
            myMediaPlayer.setDataSource(Utility.localMusicPath + "/mp3/" + myMusic.getCompleteMusicName());
            myMediaPlayer.prepare();
            myMediaPlayer.start();
            isPlaying=true;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    public void playMusic()
    {
         if(myMediaPlayer==null)
         {
             Play();
         }
         else
         {
             myMediaPlayer.release();
             Play();
         }
         addTimer();
    }
    public void pausePlay()
    {
        myMediaPlayer.pause();
        isPlaying=false;
    }
    public void continuePlay()
    {
        myMediaPlayer.start();
        isPlaying=true;
    }
    public void seekTo(int progress)
    {
        myMediaPlayer.seekTo(progress);
    }
    public void addTimer()
    {
        if(timer==null)
        {
            timer=new Timer();
            timer.schedule(new TimerTask()
            {
                @Override
                public void run()
                {
                    int duration=myMediaPlayer.getDuration();
                    int currentPosition=myMediaPlayer.getCurrentPosition();
                    Log.d("schedule1",String.valueOf(Thread.currentThread().getId()));
                    Message msg=new Message();
                    Bundle bundle=new Bundle();
                    bundle.putInt("duration",duration);
                    bundle.putInt("currentPosition",currentPosition);
                    msg.setData(bundle);
                    msg.what=0;
                    if(MusicPlay.handler!=null)
                    MusicPlay.handler.sendMessage(msg);
                }
            },5,500);
        }
    }

    public void createNotification()
    {

    }

    @Override
    public IBinder onBind(Intent intent)
    {
        musicControl=new MusicControl();
        return musicControl;
    }
    public class MusicControl extends Binder
    {
        public void setMusic(Music music)
        {
            myMusic=music;
        }
        public Music getMusic()
        {
            return myMusic;
        }
        public void play()
        {
            MusicPlayService.this.playMusic();
            Log.d("binderPlay","true");
        }
        public void pausePlay()
        {
            MusicPlayService.this.pausePlay();
        }
        public void continuePlay()
        {
            MusicPlayService.this.continuePlay();
        }
        public void seekTo(int progress)
        {
            MusicPlayService.this.seekTo(progress);
        }

        public boolean isPlay()
        {
            return isPlaying;
        }
    }

}
