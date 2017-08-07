package com.fbee.zllctl;

import java.io.Serializable;

/**
 * 定时联动的定时信息
 * 
 * @author Administrator
 * 
 */
public class TaskTimerAction implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5029772237257296604L;
	private byte WorkMode; // week
	private byte h; // hour
	private byte m; // minute
	private byte s; // second

	public byte getWorkMode()
	{
		return WorkMode;
	}

	public void setWorkMode(byte workMode)
	{
		WorkMode = workMode;
	}

	public byte getH()
	{
		return h;
	}

	public void setH(byte h)
	{
		this.h = h;
	}

	public byte getM()
	{
		return m;
	}

	public void setM(byte m)
	{
		this.m = m;
	}

	public byte getS()
	{
		return s;
	}

	public void setS(byte s)
	{
		this.s = s;
	}

}
