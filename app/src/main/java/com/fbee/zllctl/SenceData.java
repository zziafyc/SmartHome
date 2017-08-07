package com.fbee.zllctl;

public class SenceData {
	private int uId;
	// deviceId : 0x0101 for huesat lamp, 0x 0201 for light lamp ...
	private short deviceId;
	// if huesat lamp ,data1 on/off,data2 level,data3 hue,data4 sat
	private byte data1;
	private byte data2;
	private byte data3;
	private byte data4;
	
	
	public SenceData(){
		
	}
	
	public SenceData(int uId, short deviceId, byte data1, byte data2,
			byte data3, byte data4) {
		super();
		this.uId = uId;
		this.deviceId = deviceId;
		this.data1 = data1;
		this.data2 = data2;
		this.data3 = data3;
		this.data4 = data4;
		
	}

	public int getuId() {
		return uId;
	}

	public void setuId(int uId) {
		this.uId = uId;
	}

	public short getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(short deviceId) {
		this.deviceId = deviceId;
	}

	public byte getData1() {
		return data1;
	}

	public void setData1(byte data1) {
		this.data1 = data1;
	}

	public byte getData2() {
		return data2;
	}

	public void setData2(byte data2) {
		this.data2 = data2;
	}

	public byte getData3() {
		return data3;
	}

	public void setData3(byte data3) {
		this.data3 = data3;
	}

	public byte getData4() {
		return data4;
	}

	public void setData4(byte data4) {
		this.data4 = data4;
	}
}
