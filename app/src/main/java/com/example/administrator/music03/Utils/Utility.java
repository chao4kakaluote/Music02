package com.example.administrator.music03.Utils;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import com.example.administrator.music03.R;
import com.example.administrator.music03.entries.LrcMusic;
import com.example.administrator.music03.entries.Music;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
/**
 * Created by Administrator on 2017/10/9.
 */
public class Utility
{
    public static String localMusicPath= Environment.getExternalStorageDirectory().toString()+"/MIUI/music";
    public static List<Music> getLocalMusicList()
    {
        Log.d("path",localMusicPath);
        List<Music> list=new ArrayList<>();
        File mfile=new File(localMusicPath+"/mp3");
        //得到该路径下方的所有文件
        File[] files=mfile.listFiles();
        Log.d("filesSize",String.valueOf(files.length));
        boolean exist_music=false;
        for(int i=0;i<files.length;i++)
        {
            if(checkIsMusicFile(files[i].getPath()))
            {
                exist_music=true;
                Music music=new Music();
                music.setMusicName(files[i].getName());
                music.setImageId(R.drawable.shenglue);
                Log.d("fileName",files[i].getName());
                list.add(music);
            }
        }
        return list;
    }

    public static boolean checkIsMusicFile(String fName)
    {
        boolean isMusicFile = false;
        // 获取扩展名
        String FileEnd = fName.substring(fName.lastIndexOf(".") + 1, fName.length()).toLowerCase();
        if (FileEnd.equals("mp3") || FileEnd.equals("mp4") || FileEnd.equals("wmv") ) {
            isMusicFile = true;
        }
        else
        {
            isMusicFile = false;
        }
        return isMusicFile;
    }

    public static void sendRequest(String url, Callback callback)
    {
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder().url(url).build();
        client.newCall(request).enqueue(callback);
    }
    public static void sendPostRequest(String url,String argsName[],String args[],Callback callback)
    {
        OkHttpClient client=new OkHttpClient();
        FormBody.Builder form=new okhttp3.FormBody.Builder();
        for(int i=0;i<argsName.length;i++)
        {
            form.add(argsName[i],args[i]);
        }
        RequestBody formBody=form.build();
        Request request=new Request.Builder().url(url).post(formBody).build();
        client.newCall(request).enqueue(callback);
    }

    public static ArrayList<LrcMusic> redLrc(InputStream in)
    {
        ArrayList<LrcMusic> alist = new ArrayList<LrcMusic>();
        //File f = new File(path.replace(".mp3", ".lrc"));
        try {
            //FileInputStream fs = new FileInputStream(f);
            InputStreamReader input = new InputStreamReader(in, "utf-8");
            BufferedReader br = new BufferedReader(input);
            String s = "";
            while ((s = br.readLine()) != null)
            {
                if (!TextUtils.isEmpty(s))
                {
                    String lyLrc = s.replace("[", "");
                    String[] data_ly = lyLrc.split("]");
                    if (data_ly.length > 1) {
                        String time = data_ly[0];
                        String lrc = data_ly[1];
                        LrcMusic lrcMusic = new LrcMusic(lrcData(time), lrc);
                        alist.add(lrcMusic);
                    }
                }
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return alist;
    }
    public static int lrcData(String time)
    {
        time = time.replace(":", "#");
        time = time.replace(".", "#");

        String[] mTime = time.split("#");

        //[03:31.42]
        int mtime = Integer.parseInt(mTime[0]);
        int stime = Integer.parseInt(mTime[1]);
        int mitime = Integer.parseInt(mTime[2]);

        int ctime = (mtime*60+stime)*1000+mitime*10;

        return ctime;
    }
}
