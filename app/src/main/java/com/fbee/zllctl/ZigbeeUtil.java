package com.fbee.zllctl;

/**
 * Created by Administrator on 2016/11/4.
 * ---个人专属
 */

public class ZigbeeUtil {


    /**
     * 获取报警位
     *
     * @param data
     * @return
     */
    public static int getAlarmValue(int data) {
        if((data & 1) == 1 || ((data >> 1) & 1) == 1) {
            return 1;
        }
        return 0;
    }

    public static boolean isAlarm(int data) {
        if((data & 1) == 1 || ((data >> 1) & 1) == 1) {
            return true;
        }
        return false;
    }

    /**
     * 获取防拆位
     * @param data
     * @return
     */
    public static int getTamperValue(int data) {
        return ((data >> 2) & 1);
    }

    public static boolean isTamperException(int data) {
        if(((data >> 2) & 1) == 1) {
            return true;
        }
        return false;
    }

    /**
     * 获取电量位
     * @param data
     * @return
     */
    public static int getBatteryValue(int data) {
        return ((data >> 3) & 1);
    }

    public static boolean isBatteryLow(int data) {
        if(((data >> 3) & 1) == 1) {
            return true;
        }
        return false;
    }

    /**
     * 获取温度值
     * @param data
     * @return
     */
    public static float getThValue(int data) {
        return (float) ((data & 0xffff) / 100.0);
    }

    /**
     * 获取湿度值
     * @param data
     * @return
     */
    public static float getTbValue(int data) {
        return (float) (((data >> 16) & 0xffff) / 100.0);
    }


}
