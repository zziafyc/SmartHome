package com.zhongyong.speechawake;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.text.TextUtils;
import android.util.AndroidRuntimeException;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.speech.EventListener;
import com.baidu.speech.EventManager;
import com.baidu.speech.EventManagerFactory;
import com.baidu.speech.VoiceRecognitionService;
import com.baidu.tts.auth.AuthInfo;
import com.baidu.tts.client.SpeechError;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.client.TtsMode;
import com.fbee.zllctl.Contants;
import com.fbee.zllctl.DeviceInfo;
import com.fbee.zllctl.GatewayInfo;
import com.fbee.zllctl.ZigbeeUtil;
import com.zhongyong.smarthome.MyApplication;
import com.zhongyong.smarthome.R;
import com.zhongyong.smarthome.base.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;


public class WakeUpActivity extends BaseActivity {
    //远程访问百度api接口,词法分析相关接口
    private static final String api = "https://aip.baidubce.com/rpc/2.0/solution/v1/unit_utterance";
    //app之间传输数据
    DatagramSocket client;
    public static final String appIP = "192.168.1.15";
    public static final int appPort = 6666;
    //语音识别
    private static final String TAG = "zziafyc";
    private EventManager mWpEventManager;
    private SpeechRecognizer mSpeechRecognizer;
    private boolean isWake;
    //语音合成
    private SpeechSynthesizer mSpeechSynthesizer;
    private String mSampleDirPath;
    private static final String SAMPLE_DIR_NAME = "baiduTTS";
    private static final String SPEECH_FEMALE_MODEL_NAME = "bd_etts_speech_female.dat";
    private static final String SPEECH_MALE_MODEL_NAME = "bd_etts_speech_male.dat";
    private static final String TEXT_MODEL_NAME = "bd_etts_text.dat";
    private static final String LICENSE_FILE_NAME = "temp_license";
    private static final String ENGLISH_SPEECH_FEMALE_MODEL_NAME = "bd_etts_speech_female_en.dat";
    private static final String ENGLISH_SPEECH_MALE_MODEL_NAME = "bd_etts_speech_male_en.dat";
    private static final String ENGLISH_TEXT_MODEL_NAME = "bd_etts_text_en.dat";
    private InetAddress mAddress;
    //listView界面交互
    @Bind(R.id.chatListView)
    ListView mListView;
    @Bind(R.id.title_tv_message)
    TextView title;
    ChatAdapter mAdapter;
    List<MessageModel> mList = new ArrayList<>();
    //智能家居设备
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String type = intent.getStringExtra("Type");
            switch (type) {
                case Contants.ACTION_NEW_DEVICE:
                    DeviceInfo deviceInfo = (DeviceInfo) intent.getSerializableExtra("dinfo");
                    int uid = deviceInfo.getUId();
                    int deviceType = deviceInfo.getDeviceId();
                    int profileId = deviceInfo.getProfileId();
                    int ZoneType = deviceInfo.getZoneType();
                    if (isExist(uid) == -1) {
                        MyApplication.deviceInfos.add(deviceInfo);
                        if (deviceType == 0x0000 || deviceType == 0x0002 || deviceType == 0x0009 || deviceType == 0x0202 || (deviceType == 0x0200 && profileId == 0x0104)) {
                            //开关设备
                            MyApplication.switchDeviceInfos.add(deviceInfo);
                        } else if (deviceType == 0x0302) {
                            //温湿度传感器
                            MyApplication.thtbDeviceInfos.add(deviceInfo);
                        } else if (deviceType == 0x0402 && ZoneType == 0x0015) {
                            //门磁传感器
                            MyApplication.doorDeviceInfos.add(deviceInfo);
                        } else if (deviceType == 0x0402 && ZoneType == 0x002B) {
                            //气体传感器
                            MyApplication.gasDeviceInfos.add(deviceInfo);
                        } else if (deviceType == 0x0402 && ZoneType == 0x8001) {
                            //一氧化碳传感器
                            MyApplication.cogasDeviceInfos.add(deviceInfo);
                        }
                    } else {
                        MyApplication.deviceInfos.set(isExist(uid), deviceInfo);
                    }

                    if (MyApplication.gatewayInfo != null) {
                        if (MyApplication.gatewayInfo.DevSum == 0) {
                            break;
                        }
                        if (MyApplication.gatewayInfo.DevSum == MyApplication.deviceInfos.size()) {
                        }
                    } else {

                    }
                    break;
                case Contants.ACTION_GET_GATEWAYINFO:
                    MyApplication.gatewayInfo = (GatewayInfo) intent.getSerializableExtra("gatewayInfo");
                    break;

            }
        }
    };

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_wakeup;
    }

    @Override
    protected void initViews() {
        setCustomTitle("唤醒小度后，便可与小度聊天");
        initUDP();
        initialSpeechRecognizer();
        initialEnv();
        initialTts();
        initialListener();
        //智能设备1
        registerReceiver(mReceiver, new IntentFilter(Contants.ACTION_CALLBACK));
    }

    @Override
    protected void initAdapter() {
        mAdapter = new ChatAdapter(this, mList);
        mListView.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {

    }

    private boolean connect() {
        int ret = MyApplication.mSerial.connectLANZll();  //本地连接
        if (ret > 0) {
            final String[] boxip = MyApplication.mSerial.getGatewayIps(ret);
            final String[] boxsnid = MyApplication.mSerial.getBoxSnids(ret);
            int conRet = MyApplication.mSerial.connectLANZllByIp(boxip[0], boxsnid[0]);
            if (conRet > 0) {
                MyApplication.mSerial.getDevices();
                Log.e(TAG, "已连接该网关");
                return true;
            }
        }
        return false;
    }

    private void initUDP() {
        try {
            client = new DatagramSocket();
            try {
                mAddress = InetAddress.getByName(appIP);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    private void initialSpeechRecognizer() {
        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this, new ComponentName(this, VoiceRecognitionService.class));
    }


    private void initialEnv() {
        if (mSampleDirPath == null) {
            String sdcardPath = Environment.getExternalStorageDirectory().toString();
            mSampleDirPath = sdcardPath + "/" + SAMPLE_DIR_NAME;
        }
        makeDir(mSampleDirPath);
        copyFromAssetsToSdcard(false, SPEECH_FEMALE_MODEL_NAME, mSampleDirPath + "/" + SPEECH_FEMALE_MODEL_NAME);
        copyFromAssetsToSdcard(false, SPEECH_MALE_MODEL_NAME, mSampleDirPath + "/" + SPEECH_MALE_MODEL_NAME);
        copyFromAssetsToSdcard(false, TEXT_MODEL_NAME, mSampleDirPath + "/" + TEXT_MODEL_NAME);
        copyFromAssetsToSdcard(false, LICENSE_FILE_NAME, mSampleDirPath + "/" + LICENSE_FILE_NAME);
        copyFromAssetsToSdcard(false, "english/" + ENGLISH_SPEECH_FEMALE_MODEL_NAME, mSampleDirPath + "/"
                + ENGLISH_SPEECH_FEMALE_MODEL_NAME);
        copyFromAssetsToSdcard(false, "english/" + ENGLISH_SPEECH_MALE_MODEL_NAME, mSampleDirPath + "/"
                + ENGLISH_SPEECH_MALE_MODEL_NAME);
        copyFromAssetsToSdcard(false, "english/" + ENGLISH_TEXT_MODEL_NAME, mSampleDirPath + "/"
                + ENGLISH_TEXT_MODEL_NAME);
    }

    private void makeDir(String dirPath) {
        File file = new File(dirPath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    /**
     * 将sample工程需要的资源文件拷贝到SD卡中使用（授权文件为临时授权文件，请注册正式授权）
     *
     * @param isCover 是否覆盖已存在的目标文件
     * @param source
     * @param dest
     */
    private void copyFromAssetsToSdcard(boolean isCover, String source, String dest) {
        File file = new File(dest);
        if (isCover || (!isCover && !file.exists())) {
            InputStream is = null;
            FileOutputStream fos = null;
            try {
                is = getResources().getAssets().open(source);
                String path = dest;
                fos = new FileOutputStream(path);
                byte[] buffer = new byte[1024];
                int size = 0;
                while ((size = is.read(buffer, 0, 1024)) >= 0) {
                    fos.write(buffer, 0, size);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void initialTts() {
        this.mSpeechSynthesizer = SpeechSynthesizer.getInstance();
        this.mSpeechSynthesizer.setContext(this);
        // 文本模型文件路径 (离线引擎使用)
        this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE, mSampleDirPath + "/"
                + TEXT_MODEL_NAME);
        // 声学模型文件路径 (离线引擎使用)
        this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE, mSampleDirPath + "/"
                + SPEECH_FEMALE_MODEL_NAME);
        // 本地授权文件路径,如未设置将使用默认路径.设置临时授权文件路径，LICENCE_FILE_NAME请替换成临时授权文件的实际路径，仅在使用临时license文件时需要进行设置，如果在[应用管理]中开通了正式离线授权，不需要设置该参数，建议将该行代码删除（离线引擎）
        // 如果合成结果出现临时授权文件将要到期的提示，说明使用了临时授权文件，请删除临时授权即可。
        this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_LICENCE_FILE, mSampleDirPath + "/"
                + LICENSE_FILE_NAME);
        // 请替换为语音开发者平台上注册应用得到的App ID (离线授权)
        this.mSpeechSynthesizer.setAppId("9853844"/*这里只是为了让Demo运行使用的APPID,请替换成自己的id。*/);
        // 请替换为语音开发者平台注册应用得到的apikey和secretkey (在线授权)
        this.mSpeechSynthesizer.setApiKey("uIBCbf6XjLt9AHPz3MkCf2yL",
                "bc0071d56382d1bc35f083b4b8437b7e"/*这里只是为了让Demo正常运行使用APIKey,请替换成自己的APIKey*/);
        // 发音人（在线引擎），可用参数为0,1,2,3。。。（服务器端会动态增加，各值含义参考文档，以文档说明为准。0--普通女声，1--普通男声，2--特别男声，3--情感男声。。。）
        this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, "0");
        // 设置Mix模式的合成策略
        this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_MIX_MODE, SpeechSynthesizer.MIX_MODE_DEFAULT);
        // 授权检测接口(只是通过AuthInfo进行检验授权是否成功。)
        // AuthInfo接口用于测试开发者是否成功申请了在线或者离线授权，如果测试授权成功了，可以删除AuthInfo部分的代码（该接口首次验证时比较耗时），不会影响正常使用（合成使用时SDK内部会自动验证授权）
        AuthInfo authInfo = this.mSpeechSynthesizer.auth(TtsMode.MIX);
        if (authInfo.isSuccess()) {
            Log.d(TAG, "auth success");
        } else {
            String errorMsg = authInfo.getTtsError().getDetailMessage();
            Log.e(TAG, "auth failed errorMsg=" + errorMsg);
        }
        // 初始化tts
        mSpeechSynthesizer.initTts(TtsMode.MIX);
        // 加载离线英文资源（提供离线英文合成功能）
        int result = mSpeechSynthesizer.loadEnglishModel(mSampleDirPath + "/" + ENGLISH_TEXT_MODEL_NAME, mSampleDirPath
                + "/" + ENGLISH_SPEECH_FEMALE_MODEL_NAME);
        Log.d(TAG, "loadEnglishModel result=" + result);
    }

    private void initialListener() {

        //设置语音识别的
        mSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {
                //有时候没有识别出结果,我会让他继续监听
                Intent intent = new Intent();
                mSpeechRecognizer.startListening(intent);

            }

            @Override
            public void onResults(Bundle bundle) {
                ArrayList<String> results = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                String s = Arrays.toString(results.toArray(new String[results.size()]));
                final String lastResult = s.substring(1, s.length() - 1);
                Log.e(TAG, "语音识别結果：" + lastResult + "\r\n");
                if (!(lastResult.contains("百度") || lastResult.contains("小度") || lastResult.contains("小杜"))) {
                    if (isWake) {
                        MessageModel personModel = new MessageModel(lastResult, 1);
                        mList.add(personModel);
                        mAdapter.notifyDataSetChanged();
                        if ("退下吧".equals(lastResult)) {
                            // 停止唤醒监听
                            isWake = false;
                            mSpeechRecognizer.stopListening();  //这边必须要加这一句否则还可以语音识别一次
                            MessageModel model = new MessageModel("进入休眠状态", 0);
                            mList.add(model);
                            mAdapter.notifyDataSetChanged();
                            speak("进入休眠状态");
                        } else {
                            String text = "";
                            if (lastResult.contains("天气")) {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        String params = "{\"scene_id\":2985,\"session_id\":\"\",\"query\":\"" + lastResult + "\"}";
                                        String accessToken = AuthService.getAuthUnit();
                                        if (accessToken != null) {
                                            try {
                                                final String result = HttpUtil2.post(api, accessToken, params);
                                                if (!TextUtils.isEmpty(result)) {
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            speak(result);
                                                            MessageModel model = new MessageModel(result, 0);
                                                            mList.add(model);
                                                            mAdapter.notifyDataSetChanged();
                                                        }
                                                    });
                                                } else {
                                                    speak("sorry，没有查询到相关结果");
                                                    MessageModel model = new MessageModel(result, 0);
                                                    mList.add(model);
                                                    mAdapter.notifyDataSetChanged();
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        } else {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    speak("获取token失败");
                                                    MessageModel model = new MessageModel("获取token失败", 0);
                                                    mList.add(model);
                                                    mAdapter.notifyDataSetChanged();
                                                }
                                            });
                                        }
                                    }
                                }).start();
                            } else if (lastResult.contains("研发")) {
                                if (lastResult.contains("打开")) {
                                    text = "请稍等，正在打开研发部灯";
                                } else if (lastResult.contains("关闭")) {
                                    text = "请稍等，正在关闭研发部灯";
                                }

                            } else if (lastResult.contains("开关")) {
                                if (MyApplication.switchDeviceInfos != null && MyApplication.switchDeviceInfos.size() > 0) {
                                    if (lastResult.contains("打开")) {
                                        text = "请稍等，正在打开开关";
                                        DeviceInfo deviceInfo = MyApplication.switchDeviceInfos.get(1);
                                        MyApplication.mSerial.setDeviceState(deviceInfo, 1);
                                        deviceInfo.setDeviceStatus((byte) 1);

                                    } else {
                                        text = "请稍等，正在关闭开关";
                                        DeviceInfo deviceInfo = MyApplication.switchDeviceInfos.get(1);
                                        MyApplication.mSerial.setDeviceState(deviceInfo, 0);
                                        deviceInfo.setDeviceState((byte) 0);
                                    }
                                } else {
                                    Log.e(TAG, "没有搜索到设备");
                                    text = "sorry，没有搜索到设备";
                                }
                            } else if (lastResult.contains("温湿度")) {
                                DeviceInfo deviceInfo = MyApplication.thtbDeviceInfos.get(0);
                                int sensordata = deviceInfo.getSensordata();
                                text = "当前温度：" + ZigbeeUtil.getThValue(sensordata) + ",湿度：" + ZigbeeUtil.getTbValue(sensordata);

                            } else if (lastResult.contains("门状态")) {
                                if (MyApplication.doorDeviceInfos != null && MyApplication.doorDeviceInfos.size() > 0) {
                                    DeviceInfo deviceInfo = MyApplication.doorDeviceInfos.get(0);
                                    if (deviceInfo.getSensordata() == 0) {
                                        text = "门当前状态为：关闭";
                                    } else {
                                        text = "门当前状态为：打开";
                                    }

                                } else {
                                    Log.e(TAG, "没有搜索到设备");
                                    text = "sorry，没有搜索到设备";
                                }
                            } else if (lastResult.contains("网关")) {
                                if (lastResult.contains("连接")) {
                                    if (connect()) {
                                        text = "已连接网关";
                                    } else {
                                        text = "sorry,连接网关失败";
                                    }
                                }
                            } else {
                                text = "sorry，我不知道你在说什么。";
                            }
                            if (!TextUtils.isEmpty(text)) {
                                MessageModel model = new MessageModel(text, 0);
                                mList.add(model);
                                mAdapter.notifyDataSetChanged();
                                speak(text);
                                sendUDP("03" + lastResult);
                            }
                        }
                    }
                }
            }


            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });
        //设置语音合成的
        mSpeechSynthesizer.setSpeechSynthesizerListener(new SpeechSynthesizerListener() {
            @Override
            public void onSynthesizeStart(String s) {

            }

            @Override
            public void onSynthesizeDataArrived(String s, byte[] bytes, int i) {

            }

            @Override
            public void onSynthesizeFinish(String s) {

            }

            @Override
            public void onSpeechStart(String s) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mSpeechRecognizer.stopListening();
                    }
                });


            }

            @Override
            public void onSpeechProgressChanged(String s, int i) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mSpeechRecognizer.stopListening();
                    }
                });
            }

            @Override
            public void onSpeechFinish(String s) {
                //这边机器语音说完之后再开始监听,注意这个必须在主线程中
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //分两种进入休眠状态和不进入休眠状态
                        if (!isWake) {
                            mSpeechRecognizer.stopListening();
                        } else {
                            Intent intent = new Intent();
                            mSpeechRecognizer.startListening(intent);
                        }
                    }
                });


            }

            @Override
            public void onError(String s, SpeechError error) {
                Log.e("zziafyc", s + "             " + error + " ");

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        // 唤醒功能打开步骤
        // 1) 创建唤醒事件管理器
        mWpEventManager = EventManagerFactory.create(WakeUpActivity.this, "wp");

        // 2) 注册唤醒事件监听器
        mWpEventManager.registerListener(new EventListener() {
            @Override
            public void onEvent(String name, String params, byte[] data, int offset, int length) {
                try {
                    JSONObject json = new JSONObject(params);
                    if ("wp.data".equals(name)) { // 每次唤醒成功, 将会回调name=wp.data的时间, 被激活的唤醒词在params的word字段
                        String word = json.getString("word");
                        Log.e(TAG, "唤醒成功，唤醒词为: " + word + "\r\n");
                        MessageModel personModel = new MessageModel(word, 1);
                        mList.add(personModel);
                        mAdapter.notifyDataSetChanged();
                        if ("小度你好".equals(word) || "百度一下".equals(word)) {
                            isWake = true;
                            //在唤醒之后进入语音识别阶段
                            MessageModel model = new MessageModel("主人，有什么可以为你服务的吗", 0);
                            mList.add(model);
                            mAdapter.notifyDataSetChanged();
                            speak("主人，有什么可以为你服务的吗");
                        }

                    } else if ("wp.exit".equals(name)) {
                        Log.e(TAG, "唤醒已经停止: " + params + "\r\n");
                    }
                } catch (JSONException e) {
                    throw new AndroidRuntimeException(e);
                }
            }
        });


        // 3) 通知唤醒管理器, 启动唤醒功能
        HashMap params = new HashMap();
        params.put("kws-file", "assets:///WakeUp.bin"); // 设置唤醒资源, 唤醒资源请到 http://yuyin.baidu.com/wake#m4 来评估和导出
        mWpEventManager.send("wp.start", new JSONObject(params).toString(), null, 0, 0);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        this.mSpeechSynthesizer.release();
        this.mSpeechRecognizer.destroy();
        this.mWpEventManager.send("wp.stop", null, null, 0, 0);
        client.close();
        unregisterReceiver(mReceiver);
        MyApplication.getInstance().exit();
        super.onDestroy();
    }

    public void speak(String text) {
        //需要合成的文本text的长度不能超过1024个GBK字节。
        if (TextUtils.isEmpty(text)) {

        } else {
            int result = this.mSpeechSynthesizer.speak(text);
            if (result < 0) {
                Log.e(TAG, "error,please look up error code in doc or URL:http://yuyin.baidu.com/docs/tts/122 ");
            }
        }
    }

    private void sendUDP(final String order) {
        new Thread() {
            private byte[] sendBuf;

            public void run() {
                try {
                    sendBuf = order.getBytes("utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                DatagramPacket packet = new DatagramPacket(sendBuf, sendBuf.length, mAddress, appPort);
                try {
                    client.send(packet);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private int isExist(int uid) {
        int size = MyApplication.deviceInfos.size();
        int positon = -1;
        for (int i = 0; i < size; i++) {
            if (uid == MyApplication.deviceInfos.get(i).getUId()) {
                positon = i;
                return positon;
            }
        }
        return positon;
    }

}
