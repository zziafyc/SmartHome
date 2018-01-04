package com.zhongyong.speechawake;

public class Constants {
    public static final String EXTRA_KEY = "key";
    public static final String EXTRA_SECRET = "secret";
    public static final String EXTRA_SAMPLE = "sample";
    public static final String EXTRA_SOUND_START = "sound_start";
    public static final String EXTRA_SOUND_END = "sound_end";
    public static final String EXTRA_SOUND_SUCCESS = "sound_success";
    public static final String EXTRA_SOUND_ERROR = "sound_error";
    public static final String EXTRA_SOUND_CANCEL = "sound_cancel";
    public static final String EXTRA_INFILE = "infile";
    public static final String EXTRA_OUTFILE = "outfile";
    public static final String EXTRA_GRAMMAR = "grammar";
    public static final String EXTRA_RES_FILE = "res-file";
    public static final String EXTRA_KWS_FILE = "kws-file";

    public static final String EXTRA_LANGUAGE = "language";
    public static final String EXTRA_NLU = "nlu";
    public static final String EXTRA_VAD = "vad";
    public static final String EXTRA_PROP = "prop";

    public static final String EXTRA_OFFLINE_ASR_BASE_FILE_PATH = "asr-base-file-path";
    public static final String EXTRA_LICENSE_FILE_PATH = "license-file-path";
    public static final String EXTRA_OFFLINE_LM_RES_FILE_PATH = "lm-res-file-path";
    public static final String EXTRA_OFFLINE_SLOT_DATA = "slot-data";
    public static final String EXTRA_OFFLINE_SLOT_NAME = "name";
    public static final String EXTRA_OFFLINE_SLOT_SONG = "song";
    public static final String EXTRA_OFFLINE_SLOT_ARTIST = "artist";
    public static final String EXTRA_OFFLINE_SLOT_APP = "app";
    public static final String EXTRA_OFFLINE_SLOT_USERCOMMAND = "usercommand";

    public static final int SAMPLE_8K = 8000;
    public static final int SAMPLE_16K = 16000;

    public static final String VAD_SEARCH = "search";
    public static final String VAD_INPUT = "input";
    public static final String ZIGBEE_NUMBER_01 = "111169";
    public static final String ZIGBEE_NUMBER_02 = "111659";
    public static final String ZIGBEE_NUMBER_03 = "111924";
    public static final String ZIGBEE_NUMBER_04 = "111807";

    //zigbee项目
    public static final String ZIGBEE_SN_LIBRARY = "112e9112";
    public static final String ZIGBEE_SN_CLASSROOM = "";
    public static final String ZIGBEE_SN_KITCHEN = "";
    public static final String ZIGBEE_SN_LABORATORY = "";

    //图书馆
    public static final int LIBRARY_TWOSWITCH_LEFT_UID = 0;
    public static final int LIBRARY_TWOSWITCH_RIGHT_UID = 0;
    public static final int LIBRARY_THREESWITCH_LEFT_UID = 1201600;
    public static final int LIBRARY_THREESWITCH_MIDDLE_UID = 1136064;
    public static final int LIBRARY_THREESWITCH_RIGHT_UID = 1070528;
    //教室
    public static final int CLASSROOM_ONESWITCH_UID = 0;
    public static final int CLASSROOM_TWOSWITCH_LEFT_UID = 0;
    public static final int CLASSROOM_TWOSWITCH_RIGHT_UID = 0;
    //厨房
    public static final int KITCHEN_FLAMEABLE_GAS_UID = 0;
    //实验室
    public static final int KITCHEN_CO_GAS_UID = 0;
    public static final int KITCHEN_SMOKE_GAS_UID = 0;
}
