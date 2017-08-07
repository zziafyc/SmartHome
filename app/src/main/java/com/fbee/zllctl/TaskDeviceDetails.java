package com.fbee.zllctl;

/**
 * 设备联动信息，在本项目中该类只用在报警功能中，
 * 
 * @author Administrator
 * 
 */
public class TaskDeviceDetails
{
	// 联动任务信息
	private TaskInfo taskInfo;
	// 触发联动任务的详细信息
	private TaskDeviceAction taskDeviceAction;

	public TaskInfo getTaskInfo()
	{
		return taskInfo;
	}

	public void setTaskInfo(TaskInfo taskInfo)
	{
		this.taskInfo = taskInfo;
	}

	public TaskDeviceAction getTaskDeviceAction()
	{
		return taskDeviceAction;
	}

	public void setTaskDeviceAction(TaskDeviceAction taskDeviceAction)
	{
		this.taskDeviceAction = taskDeviceAction;
	}

}
