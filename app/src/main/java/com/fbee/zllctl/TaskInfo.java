package com.fbee.zllctl;

/**
 * 每个任务包含：两个动作、一个唯一的任务id（taskId）、任务名
 * 
 * @author Administrator
 * 
 */
public class TaskInfo
{
	// 任务id。it can only get from gateway ,don't modify it
	private short taskId;
	// 任务名
	private String taskName;
	// 第一个动作类型
	private byte taskType;
	// 判断该任务触发时，是否会报警
	private byte isAlarm;

	public short getTaskId()
	{
		return taskId;
	}

	public void setTaskId(short taskId)
	{
		this.taskId = taskId;
	}

	public String getTaskName()
	{
		return taskName;
	}

	public void setTaskName(String taskName)
	{
		this.taskName = taskName;
	}

	public byte getTaskType()
	{
		return taskType;
	}

	public void setTaskType(byte taskType)
	{
		this.taskType = taskType;
	}

	public byte getIsAlarm()
	{
		return isAlarm;
	}

	public void setIsAlarm(byte isAlarm)
	{
		this.isAlarm = isAlarm;
	}
}
