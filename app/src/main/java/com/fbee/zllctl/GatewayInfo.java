package com.fbee.zllctl;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/10/20.
 * ---个人专属
 */

public class GatewayInfo implements Serializable {
    public String ver;
    public String snid;
    public String uname;
    public String passwd;
    public int DevSum;
    public int GroupSum;
    public int TimerSum;
    public int SceneSum;
    public int TaskSum;


    @Override
    public String toString() {
        return "GatewayInfo{" +
                "ver='" + ver + '\'' +
                ", snid='" + snid + '\'' +
                ", uname='" + uname + '\'' +
                ", passwd='" + passwd + '\'' +
                ", DevSum=" + DevSum +
                ", GroupSum=" + GroupSum +
                ", TimerSum=" + TimerSum +
                ", SceneSum=" + SceneSum +
                ", TaskSum=" + TaskSum +
                '}';
    }
}
