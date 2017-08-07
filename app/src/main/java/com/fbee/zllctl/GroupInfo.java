package com.fbee.zllctl;

import java.io.Serializable;

public class GroupInfo implements Serializable
{
	private static final long serialVersionUID = -5802046019766543842L;
	private short groupId;
	private String groupName;
	private String groupIconPath;

	public short getGroupId()
	{
		return groupId;
	}

	public void setGroupId(short groupId)
	{
		this.groupId = groupId;
	}

	public String getGroupName()
	{
		return groupName;
	}

	public void setGroupName(String groupName)
	{
		this.groupName = groupName;
	}

	public String getGroupIconPath()
	{
		return groupIconPath;
	}

	public void setGroupIconPath(String groupIconPath)
	{
		this.groupIconPath = groupIconPath;
	}
}
