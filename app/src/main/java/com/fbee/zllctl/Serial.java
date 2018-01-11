package com.fbee.zllctl;


import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.util.List;


public class Serial {
    private static final String TAG = "Serial";
    private static Context mContext;

    public void setmContext(Context mContext) {
        Serial.mContext = mContext;
    }

    static {
        System.loadLibrary("zllcmd");
    }

	/* about net system */

	/* about serial system */

    /**
     * 当退出该app时必须调用此函数来释放其在jni层所用到的资源
     */
    public native void releaseSource();

	/* about net system */

    public native int setserver(String serverIP, short serverPort);

    /**
     * 设置语言 >0 汉语
     *
     * @return
     */
    public native int setLanguageType(int isCN);


    /**
     * 连接到本地局域网的网关
     *
     * @return 大于0（找个n个网关）连接成功，-3 连接超时，否则其他原因连接失败
     */
    public native int connectLANZll();

    /**
     * 连接到远程的网关
     *
     * @param userName 用户名
     * @param passWd   密码
     * @return 大于0，连接成功，否则失败,-2为账号或密码错误
     */
    public native int connectRemoteZll(String userName, String passWd);

    /**
     * 通过输入ip的方式来连接到本地的网关
     *
     * @param ip
     * @param snid
     * @return -1 连接失败，-3连接超时，>0连接成功
     */
    public native int connectLANZllByIp(String ip, String snid);

    /**
     * 获取当前连接的网关ip (仅限本地连接)
     *
     * @return
     */
    public native String getGatewayIp();

    /**
     * 获取当前连接的网关snid (仅限本地连接)
     *
     * @return
     */
    public native String getBoxSnid();

    /**
     * 获取当前找到所有的网关ip
     *
     * @return
     */
    public native String[] getGatewayIps(int len);

    /**
     * 获取当前连接网关的方式
     *
     * @return 0 无连接
     * <p/>
     * 1串口连接
     * <p/>
     * 2本地局域网连接
     * <p/>
     * 3远程连接
     * <p/>
     */
    public native int getConnectType();

    /**
     * 获取当前所有找到的网关snid
     *
     * @return
     */
    public native String[] getBoxSnids(int len);

    /**
     * 查看网关信息, 结果在gatewayInfo_CallBack返回
     */
    public native void getGateWayInfo();

    /**
     * 获取当前so版本信息
     *
     * @return
     */
    public native String getSoVer();

    /**
     * 复位网关
     *
     * @return
     */
    public native int resetGateway();

    /**
     * 获取当前连接的所有设备
     *
     * @return 当使用串口连接时，会立即返回当前所有设备。
     * <p/>
     * 当使用net和lan时，返回为NULL，你可以在newDevice_CallBack（）这个回调中，来获得所有设备
     */
    public native DeviceInfo[] getDevices();

    /**
     * 获得当前连接设备数目
     *
     * @return 获取当前连接设备数目
     */
    public native int getDeviceNum();

    /**
     * 更改设备或组名称,传入字符数组
     *
     * @param dInfo 要更改的设备（必须有device UId 和deviceName）
     * @param dName 新的设备名
     */
    public native int ChangeDeviceName(DeviceInfo dInfo, byte[] dName);

    /**
     * 删除当前设备
     *
     * @param info 要删除的设备（必须有device Uid）
     */
    public native int deleteDevice(DeviceInfo info);

    /**
     * 改变设备状态：1 开/ 0 关
     *
     * @param info  要改变状态的设备（必须要有Uid）
     * @param state 要改变的状态
     */
        public native int setDeviceState(DeviceInfo info, int state);

    /**
     * 获得设备状态，结果在getDeviceState_CallBack这个回调中会有返回
     *
     * @param info 要获取的设备（必须要有Uid）
     */
    public native int getDeviceState(DeviceInfo info);

    /**
     * 改变设备值，（亮度）
     *
     * @param info  要改变设备值的设备（必须要有Uid）
     * @param value 要改变的值
     */
    public native int setDeviceLevel(DeviceInfo info, byte value);

    /**
     * 改变设备值，（亮度）
     *
     * @param info           要改变设备值的设备（必须要有Uid）
     * @param value          要改变的值
     * @param transitionTime 延时时间（单位100ms）
     * @return
     */
    public native int setDeviceLevel(DeviceInfo info, byte value, short transitionTime);

    /**
     * 获得设备值（输出级别， 如亮度） ， 结果在getDeviceLevel_CallBack这个回调中有返回
     *
     * @param info 要获取设备值的设备（必须要有Uid）
     */
    public native int getDeviceLevel(DeviceInfo info);

    /**
     * 改变设备色调、饱和度
     *
     * @param info 要改变色调、饱和度的设备
     * @param hue  色调
     * @param sat  饱和度
     */
    public native int setDeviceHueSat(DeviceInfo info, byte hue, byte sat);

    /**
     * 改变设备色调、饱和度
     *
     * @param info           要改变色调、饱和度的设备
     * @param hue            色调
     * @param sat            饱和度
     * @param transitionTime 延时时间（单位100ms）
     * @return
     */
    public native int setDeviceHueSat(DeviceInfo info, byte hue, byte sat, short transitionTime);

    /**
     * 改变设备色温
     *
     * @param info  要改变色温的设备
     * @param value 色温值
     */
    public native int SetColorTemperature(DeviceInfo info, int value);

    /**
     * 改变设备色温
     *
     * @param info           要改变色温的设备
     * @param value          色温值
     * @param transitionTime 延时时间（单位100ms）
     * @return
     */
    public native int SetColorTemperature(DeviceInfo info, int value, short transitionTime);

    /**
     * 获取设备色调，结果在 getDeviceHue_CallBack这个回调中
     *
     * @param info 要获取设备色调的设备 （必须要有Uid）
     */
    public native int getDeviceHue(DeviceInfo info);

    /**
     * 获取设备饱和度
     *
     * @param info 要获取设备饱和度的设备（必须要有Uid）
     */
    public native int getDeviceSat(DeviceInfo info);

    /**
     * 获取设备色温值
     *
     * @param info 要获取设备色温的设备（必须要有Uid）
     */
    public native int getColorTemperature(DeviceInfo info);

    /**
     * 设置灯端自动变色
     *
     * @param info      要改变自动变色的灯具（必须要有Uid）
     * @param cmd       0x44
     * @param colorloop 1 启动
     * @param direction 方向  1
     * @param times     一个周期的时间 32
     * @param starthue  hue起始值 默认为0
     * @return
     */
    public native int SendautocolorZCL(DeviceInfo info, byte cmd,
                                       byte colorloop, byte direction, short times, short starthue);


    /***********************************************/
    /* about group */

    /**
     * 获取当前所有组
     *
     * @return 当使用串口连接时，会立即返回所有组； 当使用net或lan时要调用此函数来发送获取所有组，结果会在
     * newGroup_CallBack 这个回调中返回。
     */
    public native GroupInfo[] getGroups();

    /**
     * 获取指定组成员
     *
     * @param groupId 传入组ID
     * @param Dinfo   传入null即可
     * @return 当前组中所有成员, 当为net或lan连接时，结果在groupMember_CallBack（）中返回
     */
    public native DeviceInfo[] getGroupMember(short groupId, DeviceInfo Dinfo);

    /**
     * 添加一个新组
     *
     * @param groupName 要添加新组的组名
     * @return 串口连接时，成功返回该组的id，失败返回-1；net或lan连接时，返回0，添加结果在newGroup_CallBack这个回调中
     */
    public native int addGroup(String groupName);

    /**
     * 设置组中所有设备的状态
     *
     * @param groupId 要设置的状态的组的id
     * @param state   要设置的状态，0、1
     */
    public native int setGroupState(short groupId, byte state);

    /**
     * 设置组中所有lamp的亮度
     *
     * @param groupId 要设置的状态的组的id
     * @param level   亮度值
     */
    public native int setGroupLevel(short groupId, byte level);

    /**
     * 设置组中所有lamp的色调
     *
     * @param groupId 要设置的状态的组的id
     * @param hue     色调值
     */
    public native int setGroupHue(short groupId, byte hue);

    /**
     * 设置组中所有lamp的饱和度
     *
     * @param groupId 要设置的状态的组的id
     * @param sat     饱和度
     */
    public native int setGroupSat(short groupId, byte sat);

    /**
     * 设置组中所有设备的饱和度及色调
     *
     * @param groupId 要设置的状态的组的id
     * @param hue     色调值
     * @param sat     饱和度
     */
    public native int setGroupHueSat(short groupId, byte hue, byte sat);

    /**
     * 设置组中所有设备的色温
     *
     * @param groupId 要设置的状态的组的id
     * @param hue     色温值
     */
    public native int setGroupColorTemperature(short groupId, int value);

    /**
     * 将指定的设备加入到指定的组中
     *
     * @param info      设备信息 （必须有device uId）
     * @param groupInfo (必须包括groupName) 组信息
     */
    public native int addDeviceToGroup(DeviceInfo info, GroupInfo groupInfo);

    /**
     * 将指定的设备从组中删除
     *
     * @param info      （必须有device uId）
     * @param groupInfo (必须包括groupName)
     */
    public native int deleteDeviceFromGroup(DeviceInfo info, GroupInfo groupInfo);

    /**
     * 删除整个组
     *
     * @param groupName
     */
    public native int deleteGroup(String groupName);

    /***********************************************/
	/* about sence */

    /**
     * 获取当前所有场景
     *
     * @return 当是串口连接时，会立即返回当前所有场景；当是lan、net连接时，返回NULL，其结果在newSence_CallBack（）中返回
     */
    public native SenceInfo[] getSences();

    /**
     * (串口连接)
     * <p>
     * 仅支持serial连接方式，不要再 net连接时调用
     * <p>
     * （net连接请使用addDeviceToSence（）这个方法）；
     *
     * @param sceneName
     */
    public native void addSence(String sceneName);

    /**
     * 将指定的设备动作添加到指定的场景中，若场景不存在，则创建新场景
     *
     * @param senceName 场景名
     * @param uid       要添加的设备uid
     * @param deviceId  要添加的设备的deviceId
     * @param data1
     * @param data2
     * @param data3
     * @param data4
     * @param IRID      deviceId 若为162或者161可设置
     * @param delaytime 延迟时间为0-60s
     */
    public native int addDeviceToSence(String senceName, int uid,
                                       short deviceId, byte data1, byte data2, byte data3, byte data4, int IRID, byte delaytime);


    /**
     * 打开指定的场景
     *
     * @param senceId 要打开的场景的ID
     */
    public native int setSence(short sceneId);

    /**
     * 获取指定场景的详细信息， 结果在getSenceDetails_CallBack()这个回调中返回(只在net方式连接时可调用)
     *
     * @param senceId   场景Id
     * @param senceName 场景名
     */
    public native int getSenceDetails(short sceneId, String sceneName);

    /**
     * 删除场景中指定设备成员
     *
     * @param senceName 场景名
     * @param uId       设备uId
     */
    public native int deleteSenceMember(String sceneName, int uId);


    /**
     * 删除场景中指定设备成员
     *
     * @param senceName 场景名
     * @param uId       设备uId
     * @param irId      红外id (0-255)
     */
    public native int deleteSenceMember(String senceName, int uId, short irId);

    /**
     * 删除指定场景
     *
     * @param senceName
     */
    public native int deleteSence(String sceneName);

    /**
     * 修改场景名称
     *
     * @param senceId 场景Id
     * @param sName   新的场景名
     */
    public native int ChangeSceneName(short sceneId, String newSceneName);


    /***********************************************/
	/* about task */
    /***********************************************/
    /**
     * 获取当前所有任务
     *
     * @return 当使用wap或lan连接时返回为空，获取的场景在newTask_CallBack这个回调函数中
     */
    public native TaskInfo[] getTasks();

    /**
     * 添加定时任务
     *
     * @param taskInfo
     * @return
     */
    public native int addTimerTask(String taskName, TaskTimerAction timerAction, short sceneId);

    /**
     * 添加设备触发任务
     *
     * @param taskInfo
     * @param isAlarm  是否需要报警
     * @return
     */
    public native int addDeviceTask(String taskName, TaskDeviceAction deviceAction, short sceneId, byte isAlarm);

    /**
     * 添加设备报警任务
     *
     * @param taskName
     * @param deviceAction
     * @param SenceName
     * @param isAlarm
     * @return
     */
    public native int addDeviceAlarmTask(String taskName, TaskDeviceAction deviceAction);

    /**
     * 添加场景任务
     *
     * @param taskInfo
     * @return
     */
    public native int addSenceTask(String taskName, short sceneId1, short sceneId2);

    /**
     * 删除指定任务
     *
     * @param taskName 指定的任务名
     * @return
     */
    public native int deleteTask(String taskName);

    /**
     * 查看指定任务的详细信息,结果在getTimerTaskDetails_CallBack/getDeviceTaskDetails_CallBack
     * /getSenceTaskDetails_CallBack中返回
     *
     * @param taskName 指定的任务名
     * @return
     */
    public native TaskInfo getTaskInfo(String taskName);

    //
    // don't use timer
    //

    /**
     * (串口连接)
     * <p/>
     * 初始化定时相关，当使用lan或wap连接时，不要调用
     *
     * @return 返回当前所有的定时任务
     */
    public native TimerInfo[] initTimer();

    /**
     * 设备定时
     */
    public native int addDeviceTimer(int uid, byte workMode, byte h, byte m,
                                     byte s, byte taskId, byte data1, byte data2);

    /**
     * 组定时
     */
    public native int addGroupTimer(short groupId, byte workMode, byte h,
                                    byte m, byte s, byte taskId, byte data1, byte data2);

    /**
     * (串口连接)
     * <p/>
     * 场景定时
     */
    public native int addSenceTimer(byte sceneId, byte workMode, byte h,
                                    byte m, byte s, byte taskId, byte data1, byte data2);

    /**
     * 获取所有定时任务
     *
     * @return 当为serial连接时立即返回所有定时任务，当为net连接时，结果在getTimers_CallBack这个回调中返回
     */
    public native TimerInfo[] getTimers();

    /**
     * 删除定时任务
     *
     * @param info 要删除的定时任务
     * @return 删除失败返回小于0，否则删除成功
     */
    public native int deleteTimer(TimerInfo info);

    /**
     * 允许入网指令
     */
    public native int permitJoin();

    /**
     * 修改场景开发
     *
     * @param info    要设置的设备
     * @param cmd     0x04 (Store Scene)
     * @param sceneID 场景ID
     */
    public native int ModifySceneSwitchZCL(DeviceInfo info, byte cmd, byte sceneID);


    /**
     * 将指定的两个设备绑定
     *
     * @param sourceInfo 源设备
     * @param targetInfo 目标设备
     */
    public native int bindDevices(DeviceInfo sourceInfo, DeviceInfo targetInfo);

    /**
     * 将指定的两个设备解除绑定
     *
     * @param sourceInfo 源设备
     * @param targetInfo 目标设备
     * @return
     */
    public native int unBindDevice(DeviceInfo sourceInfo, DeviceInfo targetInfo);

    /**
     * 更新网关，发送数据部分
     *
     * @param data     发送的网关bin的内容
     * @param readcnt  发送的字节长度，除最后一次外每次应为256
     * @param fileaddr 已发送的字节总长度
     * @return
     */

    public native int upd(byte[] data, int readcnt, long fileaddr);

    /**
     * 待数据传完后，检查校验，开始更新
     *
     * @param fcrc 校验和值
     * @return
     */
    public native int updStart(long fcrc);

    /**
     * 设置用户自定义字符串
     *
     * @param info 用户字符串
     * @return 无
     */
    public native int setUserString(String userstring);

    /**
     * 获取用户自定义字符串，结果在getUserString_CallBack返回
     */
    public native void GetUserString();

    /***********************************************/
	/* about IR */
    /***********************************************/

    /**
     * 发送红外学习, 结果在IRstudyData_CallBack返回
     *
     * @return
     */
    public native int IRStudy(DeviceInfo dInfo);

    /**
     * 红外数据的转码
     *
     * @param learn_data_out IRstudyData_CallBack中返回的IRdata
     * @return
     */
    public native byte[] IRlearndatainout(byte learn_data_out[]);

    /**
     * 透传
     *
     * @return
     */
    public native int Transmit(DeviceInfo dInfo, byte[] dTransmitData);

    /**
     * 保存红外数据到网关中
     *
     * @return
     */
    public native int SaveIRDataToGW(IRDataInfo irInfo, byte[] dIRData);

    /**
     * 获取所有红外的数据名称
     *
     * @return
     */
    public native int getAllIRdataName();

    /**
     * 删除红外数据
     *
     * @return
     */
    public native int RemoveIRDataByIRDataId(IRDataInfo irInfo);

    /***********************************************/
	/* about DoorLock */
    /***********************************************/

    /**
     * 设置门锁时间
     *
     * @param info        （包含uid）
     * @param timeSeconds 从2000年1月1日开始的秒数
     * @return
     */
    public native int SetDoorLockTime(DeviceInfo info, int timeSeconds);

    /**
     * 获取门锁时间
     *
     * @param info （包含uid）
     * @return
     */
    public native int GetDoorLockTime(DeviceInfo info);

    /**
     * 设置门锁允许被控制状态
     *
     * @param info（包含uid）
     * @param controlable true 可控/ false 不可控
     * @return
     */
    public native int SetControlableState(DeviceInfo info, boolean controlable);

    /**
     * 获取门锁允许被控制状态
     *
     * @param info （包含uid）
     * @return
     */
    public native int GetControlableState(DeviceInfo info);

    /**
     * 获取门锁状态
     *
     * @param info（包含uid）
     * @param witchState  要读取的状态，测试写1
     * @return
     */
    public native int GetDoorLockState(DeviceInfo info, byte witchState);

    /**
     * 改变门锁状态
     *
     * @param info             要改变状态的设备（必须要有Uid）
     * @param state            1 开 / 0 关
     * @param gatedoorpassword 密码
     * @return
     */
    public native int setGatedoorState(DeviceInfo info, int state, byte[] gatedoorpassword);

    /**
     * 获取协调器信息，结果在getZigbeeModuleInfo_CallBack()返回
     *
     * @return
     */
    public native int getZigbeeModuleInfo();


    /**
     * 设置信道
     *
     * @param channel 信道
     * @return
     */
    public native int setChannel(byte channel);

    /**
     * 获取指定设备的RSSI，结果在getDeviceRSSI_CallBack返回
     *
     * @param info 指定设备
     * @return
     */
    public native int getDeviceRSSI(DeviceInfo info);

    /*
     * & 以下为java的回调方法 在这你可以得到对应的从设备返回的各种数据，不要主动去调用它 & 函数中参数即为返回的设备信息，及设备标识uId
     */
    // 获取设备状态,底层会一直返回设备状态，据此可以通过uId来判断某个设备是否在线,及通过state来判断设备状态（>0 开、<= 0关）
    public void getDeviceState_CallBack(int state, int uid) {
    }

    // 获取设备亮度
    public void getDeviceLevel_CallBack(int level, int uId) {

    }

    // 获取设备色调
    public void getDeviceHue_CallBack(int hue, int uId) {
    }


    // 获取设备饱和度
    public void getDeviceSat_CallBack(int sat, int uId) {
    }

    public void getColorTemperature_CallBack(int value, int uId) {
    }

    // 获取设备SNID号
    public void getDeviceSNID_CallBack(int snid, int uId) {

    }

    // 新设备加入
    public void newDevice_CallBack(DeviceInfo dinfo) {
        Intent i = new Intent();
        i.setAction(Contants.ACTION_CALLBACK);
        i.putExtra("Type", Contants.ACTION_NEW_DEVICE);
        i.putExtra("dinfo", dinfo);
        mContext.sendBroadcast(i);
    }

    // 返回新的组
    public void newGroup_CallBack(GroupInfo gInfo) {
    }

    // 返回新的场景
    public void newSence_CallBack(short senceId, String senceName) {
    }

    /**
     * 返回获取到的指定组的详细信息
     *
     * @param groupId
     * @param deviceUid
     */
    public void groupMember_CallBack(short groupId, int[] deviceUid) {
    }

    /**
     * 返回获取到的指定场景的详细信息
     *
     * @param senceId
     * @param deviceNumber
     * @param uId
     * @param deviceId
     * @param data1
     * @param data2
     * @param data3
     * @param data4
     * @param IRID
     * @param delaytime
     */
    public void getSenceDetails_CallBack(short senceId, int deviceNumber,
                                         int[] uId, short[] deviceId, byte[] data1, byte[] data2,
                                         byte[] data3, byte[] data4, byte[] IRID, byte[] delaytime) {
    }

    /**
     * 返回网关信息
     *
     * @param ver      网关版本
     * @param snid     snid号
     * @param uname    用户名
     * @param passwd   用户密码
     * @param DevSum   设备个数
     * @param GroupSum 组个数
     * @param TimerSum 定时个数
     * @param SceneSum 场景个数
     * @param TaskSum  任务个数
     */
    public void gatewayInfo_CallBack(byte[] ver, byte[] snid, byte[] uname,
                                     byte[] passwd, byte DevSum, byte GroupSum, byte TimerSum,
                                     byte SceneSum, byte TaskSum) throws UnsupportedEncodingException {

        Intent i = new Intent();
        i.setAction(Contants.ACTION_CALLBACK);
        i.putExtra("Type", Contants.ACTION_GET_GATEWAYINFO);
        GatewayInfo gatewayInfo = new GatewayInfo();
        gatewayInfo.ver = new String(ver, "utf-8");
        gatewayInfo.snid = new String(snid, "utf-8");
        gatewayInfo.uname = new String(uname, "utf-8");
        gatewayInfo.passwd = new String(passwd, "utf-8");

        gatewayInfo.DevSum = (int) DevSum;
        gatewayInfo.GroupSum = (int) GroupSum;
        gatewayInfo.TimerSum = (int) TimerSum;
        gatewayInfo.SceneSum = (int) SceneSum;
        gatewayInfo.TaskSum = (int) TaskSum;

        System.out.println(gatewayInfo.DevSum + "bbbbb");
        i.putExtra("gatewayInfo", gatewayInfo);
        mContext.sendBroadcast(i);
    }

    /**
     * 返回传感器数据
     * n
     *
     * @param uId      传感器设备uId
     * @param data     传感器设备返回的数据值
     * @param attribID 传感器数据类型：00 ： 温度 17：湿度
     */
    public void arriveReport_CallBack(int uId, int data, char clusterId, char attribID) {
        DeviceInfo deviceInfo = new DeviceInfo(uId, data, (short) clusterId, (short) attribID);
        Intent intent = new Intent();
        intent.setAction(Contants.ACTION_ArriveReport);
        intent.putExtra("updateData", deviceInfo);
        mContext.sendBroadcast(intent);
    }

    /**
     * 返回门锁状态
     *
     * @param uId
     * @param data
     * @param clusterId
     * @param attribID
     */
    public void arriveReportgatedoor_CallBack(int uId, byte[] data, char clusterId, char attribID) {
        Intent i = new Intent();
        i.setAction(Contants.ACTION_CALLBACK);
        i.putExtra("Type", Contants.ACTION_GET_GATEWAYINFO);
        i.putExtra("data", data);
        mContext.sendBroadcast(i);
    }


    /**
     * 当调用getTimerTasks()或addTask（）时返回
     */
    public void newTask_CallBack(byte taskType1, String taskName, short taskId) {

    }

    /**
     * 当调用getTaskInfo()时返回
     *
     * @param taskId   task's id
     * @param taskName task's name
     * @param action1  first task's action
     * @param action2  second task's action
     */
    public void getTimerTaskDetails_CallBack(String taskName, TaskTimerAction timerAction, short sceneId) {

    }

    /**
     * 当调用getTaskInfo()时返回，返回获取设备联动任务
     *
     * @param taskName     任务名
     * @param deviceAction 设备联动时，设备动作信息
     * @param senceName    联动执行场景
     * @param isAlarm      是否报警标志
     */
    public void getDeviceTaskDetails_CallBack(String taskName, TaskDeviceAction deviceAction, short sceneId, byte isAlarm) {

    }

    /**
     * 当调用getTaskInfo()时返回，返回获取场景联动任务
     *
     * @param taskName   任务名
     * @param senceName1 执行场景一
     * @param senceName2 联动执行场景二
     */
    public void getSceneTaskDetails_CallBack(String taskName, short sceneId1, short sceneId2) {

    }

    /**
     * 当调用getTimers()，返回定时任务数据
     *
     * @param uid
     * @param workMode
     * @param h
     * @param m
     * @param s
     * @param taskTye
     * @param data1
     * @param data2
     * @param TaskId
     */
    public void getTimers_CallBack(int uid, byte addrMode, byte workMode, byte h, byte m,
                                   byte s, byte taskType, byte data1, byte data2, byte taskId) {
    }

    /**
     * 当调用GetUserString()时返回，返回用户自定义字符串
     *
     * @param userStr
     */
    public void getUserString_CallBack(String userStr) {

    }

    /**
     * 当调用 getAllIRdataName()时返回，返回新的红外数据
     *
     * @param irInfo 组信息（包括IRDataId和IRDataName）
     */
    public void newIRData_CallBack(IRDataInfo irInfo) {

    }

    /**
     * 当调用IRStudy()时返回，返回学习数据
     *
     * @param IRlen    两包数据总长
     * @param IRcurlen 包长
     * @param IRdata   学习数据
     */
    public void IRstudyData_CallBack(int IRlen, int IRcurlen, byte[] IRdata) {

    }

    /**
     * 当调用getZigbeeModuleInfo时返回协调器信息
     *
     * @param channel 信道
     * @param pandId
     * @param expan
     * @param version 协调器版本
     */
    public void getZigbeeModuleInfo_CallBack(byte channel, byte[] pandId, byte[] expan, byte[] version) {
    }

    /**
     * 当调用addDeviceTimer时返回
     *
     * @param taskId 1~30 添加成功，0 添加失败
     */
    public void addTimer_CallBack(byte taskId) {

    }

    /**
     * 当调用deleteTimer时返回
     *
     * @param taskId 1~30 删除成功，0 删除失败
     */
    public void delTimer_CallBack(byte taskId) {
        Log.d(TAG, "delTimer_CallBack  taskId = " + taskId);
    }

    /**
     * 当调用getDeviceRSSI时返回设备RSSI
     *
     * @param revRSSI  接收RSSI
     * @param sendRSSI 发送RSSI
     */
    public void getDeviceRSSI_CallBack(int uid, byte revRSSI, byte sendRSSI) {

    }

    /**
     * 上报透传数据
     *
     * @param uid  设备uid
     * @param data 数据
     */
    public void arriveReportTransmit_CallBack(int uid, byte[] data) {

    }


    /**
     * 允许网关被match description 30秒
     *
     * @return
     */
    public native int setGatewayToMatchDes();

    /**
     * 绑定指定的设备到网关
     *
     * @param info
     * @return
     */
    public native int bindDeviceToGateway(DeviceInfo info);

    /**
     * 修改目标设备的IAS_CIE_Address为网关(会触发match description 30秒)
     *
     * @return
     */
    public native int setDeviceCIEAddress();

    /**
     * 创建防区（结果在createZONE_CallBack返回）
     *
     * @param zoneName  防区名称(utf-8编码)
     * @param isdefense 1 布防/0 撤防
     * @param taskList  任务列表（TaskInfo必须包含taskId和isAble）
     * @return
     */
    public native int createZONE(byte[] zoneName, byte isdefense, List<TaskInfo> taskList);

    /**
     * 当调用createZONE时回调
     *
     * @param zoneName  防区名称(长度不超过36)
     * @param zone      防区号
     * @param isdefense 1 布防/0 撤防
     * @param ret       添加防区任务标记位  1 成功/0 失败
     */
    public void createZONE_CallBack(String zoneName, byte zone, byte isdefense, byte ret) {

    }

    /**
     * 撤防（结果在destoryZONE_CallBack返回）
     *
     * @param zone   防区号
     * @param taskId 任务ID合集 (taskIds为NULL则删除防区)
     * @return
     */
    public native int destoryZONE(byte zone, byte[] taskIds);

    /**
     * 当调用destoryZONE时回调
     *
     * @param zone   防区号
     * @param taskId 任务Id合集
     * @param ret    销毁防区任务标记位  1 成功/0 失败
     */
    public void destoryZONE_CallBack(byte zone, byte[] taskId, byte ret) {

    }

    /**
     * 获取防区信息，结果在getZONEInfo_CallBack返回
     *
     * @return
     */
    public native int getZONEInfos();

    /**
     * 当调用getZONEInfos时返回所有防区
     *
     * @param zoneName  防区名称
     * @param zone      防区号
     * @param isdefense 1 布防/0 撤防
     */
    public void getZONEInfos_CallBack(String zoneName, byte zone, byte isdefense) {
    }

    /**
     * 获取指定防区详情
     *
     * @param zone 防区号
     * @return
     */
    public native int getZONEDetails(byte zone);

    /**
     * 当调用getZONEDetails时返回防区详情
     *
     * @param zoneName   防区名称
     * @param zone       防区号
     * @param isdefense  1 布防/0 撤防
     * @param taskId     任务Id合集
     * @param taskIsAble 任务使能位合集
     */
    public void getZONEDetails_CallBack(String zoneName, byte zone, byte isdefense, byte[] taskId, byte[] taskIsAble) {
    }

    /**
     * 设置防区使能，结果在setZONEToAble_CallBack返回
     *
     * @param zone   防区号
     * @param isable 1 防区有效/0 防区无效
     * @return
     */
    public native int setZONEToAble(byte zone, byte isable);

    /**
     * 当调用setDefenseToAble时返回，防区使能位
     *
     * @param zone 防区号
     * @param ret  1 成功/0 失败
     */
    public void setZONEToAble_CallBack(byte zone, byte ret) {

    }

    /**
     * 修改防区名称，结果在changeZONEName_CallBack返回
     *
     * @param zone     指定防区号
     * @param zoneName 新的名称
     * @return
     */
    public native int changeZONEName(byte zone, byte[] zoneName);

    /**
     * 当调用changeZONEName时返回(长度不超过36)
     *
     * @param zone     防区号
     * @param zoneName 防区名称
     * @param ret      1 成功/0 失败
     */
    public void changeZONEName_CallBack(byte zone, String zoneName, byte ret) {

    }

    /**
     * 获取情景开关的绑定信息，结果在getBindScene_CallBack、getBindDevice_CallBack返回
     *
     * @return
     */
    public native int getBindInfo();

    /**
     * 当调用getBindInfo时返回
     *
     * @param uid     源设备UID
     * @param sceneId 目标情景ID
     */
    public void getBindScene_CallBack(int uid, int sceneId) {

    }

    /**
     * 当调用getBindInfo时返回
     *
     * @param uid    源设备UID
     * @param tarUid 目标设备UID
     */
    public void getBindDevice_CallBack(int uid, int[] tarUid) {

    }

    /**
     * 重命名组名称，
     *
     * @param groupId 指定的组ID
     * @param name    新的名称(utf-8编码)
     * @return
     */
    public native int changeGroupName(short groupId, byte[] name);

    /**
     * 当调用changeGroupName时返回
     *
     * @param groupId 指定的组ID
     * @param name    新的名称
     */
    public void changeGroupName_CallBack(short groupId, String name) {

    }

    /**
     * 获取指定设备当前的状态
     *
     * @param deviceInfo 指定设备(包含 uid)
     * @param cmd        0x00 获取设备当前的状态,结果在getDeviceInfo_CallBack返回
     * @return
     */
    public native int identifyDevice(DeviceInfo deviceInfo, byte cmd);

    /**
     * 当调用identifyDevice且cmd为0x00时返回
     *
     * @param uid       指定设备
     * @param loopColor 自动变色标记位
     * @param reserve1
     * @param reserve2
     * @param reserve3
     */
    public void getDeviceInfo_CallBack(int uid, int loopColor, int reserve1, int reserve2, int reserve3) {

    }

    /**
     * 当调用resetGateway返回
     *
     * @param ret 0xa1 成功，否则失败
     */
    public void resetGateway_Callback(int ret) {

    }

    public void PermissionState_CallBack(byte per) {
    }

}
