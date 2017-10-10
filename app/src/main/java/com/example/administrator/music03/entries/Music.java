package com.example.administrator.music03.entries;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/9/26.
 */

public class Music extends DataSupport implements Serializable
{
    private int id;
    private int ImageId;
    private String musicName;
    private String words;
    public void setId(int id) {
        this.id = id;
    }
    public void setMusicName(String musicName) {
        this.musicName=musicName;
    }
    public void setWords(String words) {
        this.words = words;
    }

    public int getId() {
        return id;
    }
    public String getMusicName() {
        String name=musicName.substring(0,musicName.lastIndexOf('.'));
        return name;
    }
    public String getCompleteMusicName()
    {
        return this.musicName;
    }
    public String getWords() {
        return words;
    }

    public int getImageId() {
        return ImageId;
    }
    public void setImageId(int imageId) {
        ImageId = imageId;
    }


}
