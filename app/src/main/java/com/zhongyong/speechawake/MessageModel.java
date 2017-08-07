package com.zhongyong.speechawake;

import java.io.Serializable;

/**
 * Created by fyc on 2017/7/12
 * 邮箱：847891359@qq.com
 * 博客：http://blog.csdn.net/u013769274
 */

public class MessageModel implements Serializable {
    private String image;
    private String content;
    private int type;  //有两种类型 0：表示机器 1：表示人

    public MessageModel(String content, int type) {
        this.content = content;
        this.type = type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
