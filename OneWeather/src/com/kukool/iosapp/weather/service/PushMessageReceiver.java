package com.kukool.iosapp.weather.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.baidu.android.pushservice.PushConstants;

public class PushMessageReceiver extends BroadcastReceiver {
    private static final String TAG = "PushMessageReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, ">>> Receive intent: \r\n" + intent);

        if (intent.getAction().equals(PushConstants.ACTION_MESSAGE)) {
            //获取消息内容
            String message = intent.getExtras().getString(
                    PushConstants.EXTRA_PUSH_MESSAGE_STRING);

            //用户自定义内容读取方式，CUSTOM_KEY值和管理界面发送时填写的key对应
            //String customContentString = intent.getExtras().getString(CUSTOM_KEY);
            Log.i(TAG, "onMessage: " + message);

            String content = intent.getExtras().getString(PushConstants.EXTRA_CONTENT);
            Log.d(TAG, "WIIKII==ACTION_MESSAGE===message=" + message);
            Log.d(TAG, "WIIKII==ACTION_MESSAGE===content=" + content);

            //用户在此自定义处理消息,以下代码为demo界面展示用
//            // TODO
//            AlertDialog.Builder builder = new AlertDialog.Builder(context);
//            builder.setMessage(message);
//            builder.setCancelable(true);
//            Dialog dialog = builder.create();
//            dialog.setCanceledOnTouchOutside(true);
//            dialog.show();
            //这个showDialog方法大家如果需要再找我。
//            Utils.showDialog(context, null, message, "确定", null);

            //处理绑定等方法的返回数据
            //注:PushManager.startWork()的返回值通过PushConstants.METHOD_BIND得到
        } else if (intent.getAction().equals(PushConstants.ACTION_RECEIVE)) {
            //获取方法
            final String method = intent
                    .getStringExtra(PushConstants.EXTRA_METHOD);
            //方法返回错误码,您需要恰当处理。比如，方法为bind时，若失败，需要重新bind,即重新调用startWork
            final int errorCode = intent
                    .getIntExtra(PushConstants.EXTRA_ERROR_CODE,
                            PushConstants.ERROR_SUCCESS);
            //返回内容
            final String content = new String(
                    intent.getByteArrayExtra(PushConstants.EXTRA_CONTENT));


            //用户在此自定义处理消息,以下代码为demo界面展示用

            Log.d(TAG, "WIIKII==ACTION_RECEIVE===method=" + method + "...errorcode=" + errorCode + ",,,,content=" + content);

//            Log.d(TAG, "onMessage: method : " + method);
//            Log.d(TAG, "onMessage: result : " + errorCode);
//            Log.d(TAG, "onMessage: content : " + content);
//            Toast.makeText(
//                    context,
//                    "method : " + method + "\n result: " + errorCode
//                            + "\n content = " + content, Toast.LENGTH_SHORT)
//                    .show();

            //TODO

            //可选。通知用户点击事件处理
        } else if (intent.getAction().equals(
                PushConstants.ACTION_RECEIVER_NOTIFICATION_CLICK)) {
            Log.d(TAG, "intent=" + intent.toUri(0));
            Log.d(TAG, "WIIKII==ACTION_RECEIVER_NOTIFICATION_CLICK===" + intent.toUri(0));
//            Intent aIntent = new Intent();
//            aIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            aIntent.setClass(context, CustomActivity.class);
//            String title = intent
//                    .getStringExtra(PushConstants.EXTRA_NOTIFICATION_TITLE);
//            aIntent.putExtra(PushConstants.EXTRA_NOTIFICATION_TITLE, title);
//            String content = intent
//                    .getStringExtra(PushConstants.EXTRA_NOTIFICATION_CONTENT);
//            aIntent.putExtra(PushConstants.EXTRA_NOTIFICATION_CONTENT, content);
//
//            context.startActivity(aIntent);

            //TODO
        }
    }
}
