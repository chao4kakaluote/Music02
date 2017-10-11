package com.example.administrator.music03.Activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.administrator.music03.Adapter.LocalMusicAdapter;
import com.example.administrator.music03.R;
import com.example.administrator.music03.Utils.Utility;
import com.example.administrator.music03.entries.Music;
import com.example.administrator.music03.service.MusicPlayService;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity1 extends AppCompatActivity
{
    ListView  localList;
    RadioGroup radioGroup;
    RadioButton btn1;
    RadioButton btn2;
    LinearLayout layout;
    ImageView MusicImage;


    private LocalMusicAdapter Localadapter;
    private ArrayAdapter<String> Downloadadapter;
    private List<Music> musicLocalData=new ArrayList<>();
    private List<String> musicDownloadData=new ArrayList<>();

    private MusicPlayService.MusicControl musicBinder;
    private ServiceConnection connection=new ServiceConnection()
    {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder)
        {
            musicBinder=(MusicPlayService.MusicControl)iBinder;
            showSetBottomPic();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName)
        {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        initView();
        bindMusicService();
        initRadioGroup();
    }
    public void initView()
    {
         localList=(ListView)findViewById(R.id.musicList);
         radioGroup=(RadioGroup)findViewById(R.id.group);
         btn1=(RadioButton)findViewById(R.id.groupBtn1);
         btn2=(RadioButton)findViewById(R.id.groupBtn2);
         layout=(LinearLayout)findViewById(R.id.musicPicLayout);
        MusicImage=(ImageView)findViewById(R.id.musicPic);
    }
    //初始化radioGroup的显示和设置
    public void initRadioGroup()
    {
         radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
             @Override
             public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkId)
             {
                 int radioButton=radioGroup.getCheckedRadioButtonId();
                 RadioButton btn=(RadioButton)findViewById(radioButton);
                 String text=btn.getText().toString();
                 if(text.equals("本地音乐"))
                 {
                     setLocalList();
                 }
                 else
                 {
                     setDownloadList();
                 }
             }
         });
        radioGroup.check(radioGroup.getChildAt(0).getId());
    }
    //设置本地音乐list的adapter
    public void setLocalList()
    {
           musicLocalData= Utility.getLocalMusicList();
           if(musicLocalData!=null)
           {
               Localadapter=new LocalMusicAdapter(this,R.layout.localmusicitem,musicLocalData,new LocalMusicAdapter.OnListClickListener()
               {
                   @Override
                   public void onClickListPlay(Music music)
                   {
                           //如果当前正在播放音乐，则判断点击的音乐是否是同一首，如果不是则重新播放
                            if(musicBinder.isPlay())
                            {
                                if(!musicBinder.getMusic().getMusicName().equals(music.getMusicName()))
                                {
                                    musicBinder.setMusic(music);
                                    musicBinder.play();
                                }
                            }
                            //如果当前处于暂停状态则判断是否是同一首音乐或者当前未播放，是则继续，不是则重新播放。
                            else if(!musicBinder.isPlay())
                            {
                                if(musicBinder.getMusic()==null || !musicBinder.getMusic().getMusicName().equals(music.getMusicName()))
                                {
                                    musicBinder.setMusic(music);
                                    musicBinder.play();
                                }
                                else
                                {
                                    musicBinder.continuePlay();
                                }
                            }
                            showSetBottomPic();
                   }

                   @Override
                   public void onClickListReplay(Music music)
                   {
                         //重新播放即可
                           musicBinder.setMusic(music);
                           musicBinder.play();
                           showSetBottomPic();
                   }

                   @Override
                   public void onClickListPause(Music music)
                   {
                       //如果是同一首音乐则暂停，不是则不管。
                       if(musicBinder.getMusic()!=null && musicBinder.getMusic().getMusicName().equals(music.getMusicName()))
                       {
                           musicBinder.pausePlay();
                       }
                   }
               });
           }
           localList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
               @Override
               public void onItemClick(AdapterView<?> adapterView, View view, int position, long l)
               {
                   Intent intent=new Intent(MainActivity1.this,MusicPlay1.class);
                   Music music= musicLocalData.get(position);
                   intent.putExtra("music",music);
                   startActivity(intent);
               }
           });
           localList.setAdapter(Localadapter);
    }

    public void setDownloadList()
    {

    }

    public void showSetBottomPic()
    {
        if(musicBinder.getMusic()!=null) {
            layout.setVisibility(View.VISIBLE);
            //看是否存在图片文件，如果存在则设置该图片
            String path=Utility.localMusicPath+"/album/"+musicBinder.getMusic().getMusicName()+"jpg";
            File file=new File(path);
            if(file.exists()) {
                try {
                    FileInputStream f = new FileInputStream(Utility.localMusicPath + "/album/" + musicBinder.getMusic().getMusicName() + "jpg");
                    Bitmap bm = null;
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 8;//图片的长宽都是原来的1/8
                    BufferedInputStream bis = new BufferedInputStream(f);
                    bm = BitmapFactory.decodeStream(bis, null, options);
                    MusicImage.setImageBitmap(bm);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }

            MusicImage.setImageResource(R.drawable.music_disc);
            MusicImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity1.this, MusicPlay1.class);
                    Music music = musicBinder.getMusic();
                    intent.putExtra("music", music);
                    startActivity(intent);
                }
            });
        }
    }
    public void bindMusicService()
    {
           Intent intent=new Intent(this,MusicPlayService.class);
           startService(intent);
           bindService(intent,connection,BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }
}
