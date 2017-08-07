package com.fbee.zllctl;

import java.io.Serializable;

/**
 * senceinfo
 * 
 * @author Administrator
 */
public class SenceInfo implements Serializable
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 3854276665568461276L;
	private short senceId;
	private String senceName;
	// this fragment_scene's fragment_device number
	private byte deviceNum;
	private String senceIconPath;
//	public List<SenceData> senceDatas = new ArrayList<SenceData>();

	public SenceInfo()
	{

	}

	public SenceInfo(String senceId, String senceName, String senceIconPath)
	{
		this.senceId = Short.parseShort(senceId);
		this.senceName = senceName;
		this.senceIconPath = senceIconPath;
	}

	public short getSenceId()
	{
		return senceId;
	}

	public void setSenceId(short senceId)
	{
		this.senceId = senceId;
	}

	public String getSenceName()
	{
		return senceName;
	}

	public void setSenceName(String senceName)
	{
		this.senceName = senceName;
	}

	public byte getDeviceNum()
	{
		return deviceNum;
	}

	public void setDeviceNum(byte deviceNum)
	{
		this.deviceNum = deviceNum;
	}

//	public List<SenceData> getSenceDatas()
//	{
//		return senceDatas;
//	}
//
//	public void setSenceDatas(List<SenceData> senceDatas)
//	{
//		this.senceDatas = senceDatas;
//	}

	public String getSenceIconPath()
	{
		return senceIconPath;
	}

	public void setSenceIconPath(String senceIconPath)
	{
		this.senceIconPath = senceIconPath;
	}
}
