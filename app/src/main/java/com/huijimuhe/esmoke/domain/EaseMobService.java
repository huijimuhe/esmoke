package com.huijimuhe.esmoke.domain;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.huijimuhe.esmoke.bean.UserBean;
import com.huijimuhe.esmoke.core.AppContext;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMChatManager;
import com.hyphenate.chat.EMChatRoom;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMCursorResult;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.EMLog;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Huijimuhe on 2016/3/19.
 * This is a copy of Esmoke
 * belongs to com.huijimuhe.esmoke.domain
 * please enjoy the day and night when you work hard on this.
 */
public class EaseMobService {

    protected static final String TAG = "EaseMobService";
    /**
     * 单例
     */
    private static EaseMobService instance=null;

    /**
     * application context
     */
    private Context appContext = null;

    /**
     * init flag: test if the sdk has been inited before, we don't need to init again
     */
    private boolean sdkInited = false;
    /**
     * 遍历聊天室列表cursor
     */
    private String mRoomCursor,mRoomId;
    /**
     * 计入聊天室失败次数
     */
    int failedNm=0;
    /**
     * 状体监听
     */
    private EMConnectionListener connectionListener;

    /**
     * 消息监听
     */
    private EMMessageListener msgListener;

    /**
     * 广播管理
     */
    private LocalBroadcastManager broadcastManager;


    private EaseMobService() {
    }

    public synchronized static EaseMobService getInstance() {
        if (instance == null) {
            instance = new EaseMobService();
        }
        return instance;
    }

    /**
     * 初始化
     * @param context
     */
    public EaseMobService init(Context context) {
        appContext=context;
        if(sdkInited){
            return this;
        }

        EMOptions options = initChatOptions();
        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
        // 如果app启用了远程的service，此application:onCreate会被调用2次
        // 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
        // 默认的app会在以包名为默认的process name下运行，如果查到的process name不是app的process name就立即返回
        if (processAppName == null ||!processAppName.equalsIgnoreCase(appContext.getPackageName())) {
            return this;
        }
        EMClient.getInstance().init(context, options);

        //设为调试模式，打成正式包时，最好设为false，以免消耗额外的资源
        EMClient.getInstance().setDebugMode(true);

        //设置全局监听
        setGlobalListeners();
        broadcastManager = LocalBroadcastManager.getInstance(appContext);
        sdkInited=true;
        return  this;
    }

    public EaseMobService initEnvr(){
        EMClient.getInstance().groupManager().loadAllGroups();
        EMClient.getInstance().chatManager().loadAllConversations();
        return this;
    }

    private EMOptions initChatOptions() {

        // 获取到EMChatOptions对象
        EMOptions options = new EMOptions();
        // 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);
        // 设置是否需要已读回执
        options.setRequireAck(false);
        // 设置是否需要已送达回执
        options.setRequireDeliveryAck(false);
        // 设置从db初始化加载时, 每个conversation需要加载msg的个数
        options.setNumberOfMessagesLoaded(1);

        //在小米手机上当app被kill时使用小米推送进行消息提示，同GCM一样不是必须的
        options.setMipushConfig("2882303761517426801", "5381742660801");

        options.allowChatroomOwnerLeave(true);
        options.setDeleteMessagesAsExitGroup(true);
        options.setAutoAcceptGroupInvitation(true);

        return options;
    }

    /**
     * 设置全局事件监听
     */
    protected void setGlobalListeners(){

        // create the global connection listener
        connectionListener = new EMConnectionListener() {
            @Override
            public void onDisconnected(int error) {
                if (error == EMError.USER_REMOVED) {
                }else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                }
            }

            @Override
            public void onConnected() {
                // in case group and contact were already synced, we supposed to notify sdk we are ready to receive the events

            }
        };

        //注册连接监听
        EMClient.getInstance().addConnectionListener(connectionListener);
        //注册群组和联系人监听
        //  registerGroupAndContactListener();
        //注册消息事件监听
        registerEventListener();

    }

    /**
     * 全局事件监听
     * 因为可能会有UI页面先处理到这个消息，所以一般如果UI页面已经处理，这里就不需要再次处理
     * activityList.size() <= 0 意味着所有页面都已经在后台运行，或者已经离开Activity Stack
     */
    protected void registerEventListener() {
        msgListener = new EMMessageListener() {
            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                for (EMMessage message : messages) {
                    //EMLog.d(TAG, "onMessageReceived id : " + message.getMsgId());
                    Intent broadcastIntent = new Intent(AppContext.DANMAKU_BROADCAST);
                    broadcastIntent.putExtra("danmaku", message.getBody().toString().split(":")[1]);
                    appContext.sendBroadcast(broadcastIntent, null);
                }
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> messages) {
                for (EMMessage message : messages) {

                    //获取消息body
                    EMCmdMessageBody cmdMsgBody = (EMCmdMessageBody) message.getBody();
                    final String action = cmdMsgBody.action();//获取自定义action

                    //获取扩展属性 此处省略
                    //message.getStringAttribute("");
                    //EMLog.d(TAG, String.format("透传消息：action:%s,message:%s", action,message.toString()));
                    Intent broadcastIntent = new Intent(AppContext.RING_BROADCAST);
                    appContext.sendBroadcast(broadcastIntent, null);
                }
            }

            @Override
            public void onMessageReadAckReceived(List<EMMessage> messages) {
            }

            @Override
            public void onMessageDeliveryAckReceived(List<EMMessage> message) {
            }

            @Override
            public void onMessageChanged(EMMessage message, Object change) {

            }
        };

        EMClient.getInstance().chatManager().addMessageListener(msgListener);
    }

    /**
     * 用户登录
     *
     * @param handler
     */
    public void easeMobLogin(final Handler handler) {
        UserBean user=AppContext.getInstance().getUser();
        String userName=user.getOpen_id();
        String password="pwd"+user.getOpen_id();
        EMClient.getInstance().login(userName, password, new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                handler.post(new Runnable() {
                    public void run() {
                        EMClient.getInstance().groupManager().loadAllGroups();
                        EMClient.getInstance().chatManager().loadAllConversations();
                        Log.d("main", "登陆聊天服务器成功！");
                    }
                });
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                Log.d("main", "登陆聊天服务器失败！");
            }
        });
    }

    /**
     * 登录聊天室
     */
    public void enterChatRoom() {

        EMClient.getInstance().chatroomManager().joinChatRoom(getRoomId(), new EMValueCallBack<EMChatRoom>() {
            @Override
            public void onSuccess(EMChatRoom value) {
                //加入聊天室成功
                Log.d("main", "加入聊天室成功！No." + String.valueOf(mRoomId));
            }

            @Override
            public void onError(final int error, String errorMsg) {
                Log.d("main", "加入聊天室失败！." + errorMsg + String.valueOf(error));
                //加入聊天室失败
                if (failedNm < 5) {
                    enterChatRoom();
                    failedNm++;
                } else {
                    failedNm = 0;
                }
            }
        });
    }

    /**
     * 退出聊天室
     */
    public void quiteRoom() {
        EMClient.getInstance().chatroomManager().leaveChatRoom(AppContext.getInstance().getUser().getOpen_id());
    }

    /**
     * 获取聊天室ID
     * @return
     */
    private String getRoomId() {
        try {
            EMCursorResult<EMChatRoom> result = EMClient.getInstance().chatroomManager().fetchPublicChatRoomsFromServer(1, mRoomCursor);
            mRoomCursor = result.getCursor();
            mRoomId= result.getData().get(0).getId();
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
        return mRoomId;
    }

    public void sendRingMsg(){
        EMMessage cmdMsg = EMMessage.createSendMessage(EMMessage.Type.CMD);
        cmdMsg.setChatType(EMMessage.ChatType.ChatRoom);
        String action="ring";
        EMCmdMessageBody cmdBody = new EMCmdMessageBody(action);
        String toUsername = mRoomId;//发送给某个人
        cmdMsg.setReceipt(toUsername);
        cmdMsg.addBody(cmdBody);
        EMClient.getInstance().chatManager().sendMessage(cmdMsg);
    }

    public void sendDanmaku(String danmaku){
        EMMessage message = EMMessage.createTxtSendMessage(danmaku, mRoomId);
        message.setChatType(EMMessage.ChatType.ChatRoom);
        EMClient.getInstance().chatManager().sendMessage(message);
    }

    /**
     * check the application process name if process name is not qualified, then we think it is a service process and we will not init SDK
     * @param pID
     * @return
     */
    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) appContext.getSystemService(Context.ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = appContext.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    CharSequence c = pm.getApplicationLabel(pm.getApplicationInfo(info.processName, PackageManager.GET_META_DATA));
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {

            }
        }
        return processName;
    }
}
