package cn.upush.cordova;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zengjing on 17/5/19.
 */

public class UPushPlugin extends CordovaPlugin{
    private final static List<String> methodList =
            Arrays.asList(
                    // "addLocalNotification",
                    // "clearAllNotification",
                    // "clearLocalNotifications",
                    // "clearNotificationById",
                    // "getNotification",
                    // "getRegistrationID",
                    "init"
                    // "isPushStopped",
                    // "onPause",
                    // "onResume",
                    // "requestPermission",
                    // "removeLocalNotification",
                    // "reportNotificationOpened",
                    // "resumePush",
                    // "setAlias",
                    // "setBasicPushNotificationBuilder",
                    // "setCustomPushNotificationBuilder",
                    // "setDebugMode",
                    // "setLatestNotificationNum",
                    // "setPushTime",
                    // "setTags",
                    // "setTagsWithAlias",
                    // "setSilenceTime",
                    // "setStatisticsOpen",
                    // "stopPush"
            );

    private ExecutorService threadPool = Executors.newFixedThreadPool(1);
    private static UPushPlugin instance;
    private static Activity cordovaActivity;
    private static String TAG = "UPushPlugin";

    public UPushPlugin(){
        instance = this;
    }

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);

        cordovaActivity = cordova.getActivity();

    }

    @Override
    public void onPause(boolean multitasking) {
        super.onPause(multitasking);
    }

    @Override
    public void onResume(boolean multitasking) {
        super.onResume(multitasking);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cordovaActivity = null;
        instance = null;
    }

    @Override
    public boolean execute(final String action, final JSONArray args, final CallbackContext callbackContext) throws JSONException {
        if (!methodList.contains(action)) {
            return false;
        }
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Method method = UPushPlugin.class.getDeclaredMethod(action,
                            JSONArray.class, CallbackContext.class);
                    method.invoke(UPushPlugin.this, args, callbackContext);
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
            }
        });
        return true;
    }

    void init(JSONArray data, CallbackContext callbackContext) {
        Context context = cordova.getActivity().getApplicationContext();
        PushAgent pushAgent = PushAgent.getInstance(context);
        pushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String s) {
                Log.e("Umeng", "成功");
            }

            @Override
            public void onFailure(String s, String s1) {

            }
        });
//        UmengMessageHandler messageHandler = new UmengMessageHandler(){
//            @Override
//            public void dealWithCustomMessage(final Context context, final UMessage msg) {
//                new Handler(context.getMainLooper()).post(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        // 对于自定义消息，PushSDK默认只统计送达。若开发者需要统计点击和忽略，则需手动调用统计方法。
//                        boolean isClickOrDismissed = true;
//                        if(isClickOrDismissed) {
//                            //自定义消息的点击统计
//                            UTrack.getInstance(context).trackMsgClick(msg);
//                        } else {
//                            //自定义消息的忽略统计
//                            UTrack.getInstance(context).trackMsgDismissed(msg);
//                        }
//
//                        Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
//                    }
//                });
//            }
//        };
//        pushAgent.setMessageHandler(messageHandler);
    }
}
