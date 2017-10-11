package com.fbee.zllctl;


/**
 * Created by Administrator on 2016/11/3.
 * ---个人专属
 */


public class Record {

    private Long id;

    private int uId;

    private String name;

    private String state;

    private String time;

    public Record(Long id, int uId, String name, String state, String time) {
        this.id = id;
        this.uId = uId;
        this.name = name;
        this.state = state;
        this.time = time;
    }

    public Record() {
    }

    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getState() {
        return this.state;
    }
    public void setState(String state) {
        this.state = state;
    }
    public String getTime() {
        return this.time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public int getUId() {
        return this.uId;
    }
    public void setUId(int uId) {
        this.uId = uId;
    }

}
