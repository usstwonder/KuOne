package com.kukool.iosapp.weather.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.kukool.iosapp.weather.R;
import com.umeng.fb.FeedbackAgent;
import com.umeng.socialize.controller.*;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.TencentWBSsoHandler;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-12-17
 * Time: 下午5:36
 * To change this template use File | Settings | File Templates.
 */
public class AboutActivity extends Activity implements View.OnClickListener {
    final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share",
            RequestType.SOCIAL);
    private View viewShare;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_about);
        findViewById(R.id.about_back).setOnClickListener(this);
        findViewById(R.id.btn_share).setOnClickListener(this);
        findViewById(R.id.btn_feedback).setOnClickListener(this);
        findViewById(R.id.btn_checkupdate).setOnClickListener(this);
        findViewById(R.id.btn_outweb).setOnClickListener(this);
        ((TextView)findViewById(R.id.app_version_text)).setText(getText(R.string.str_about_versin)
                + " " + getVersion(this));
        InitForUM();

    }

    private void InitForUM(){
        // 设置分享内容
        mController.setShareContent(getResources().getString(R.string.str_share_content)
                    + "http://www.kuho.me/oneweather/latest.apk");
        // 设置分享图片, 参数2为图片的url地址
        mController.setShareMedia(new UMImage(this,
                "http://www.kuho.me/oneweather/images/post.png"));


        // appID是你在微信开发平台注册应用的AppID
        String appID = "wx620d74908510fabb";
        // 微信图文分享必须设置一个url
        String contentUrl = "http://www.kuho.me/kuone";
        // 添加微信平台，参数1为当前Activity, 参数2为用户申请的AppID, 参数3为点击分享内容跳转到的目标url
        UMWXHandler wxHandler = mController.getConfig().supportWXPlatform(this,appID, contentUrl);
        wxHandler.setWXTitle(getString(R.string.str_share_title));
        // 支持微信朋友圈
        UMWXHandler circleHandler = mController.getConfig().supportWXCirclePlatform(this,appID, contentUrl) ;
        circleHandler.setCircleTitle(getString(R.string.str_share_title));

        //  参数1为当前Activity， 参数2为用户点击分享内容时跳转到的目标地址
        mController.getConfig().supportQQPlatform(this, true, contentUrl);


        // SSO（免登录）分享到QQ空间
        mController.getConfig().setSsoHandler( new QZoneSsoHandler(this) );
        //设置新浪SSO handler
        mController.getConfig().setSsoHandler(new SinaSsoHandler());
        //设置腾讯微博SSO handler
        mController.getConfig().setSsoHandler(new TencentWBSsoHandler());
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.about_back:
                finish();
                //overridePendingTransition(R.anim.slide_out_down, 0);
                break;
            case R.id.btn_share:
                mController.openShare(AboutActivity.this, false);
                break;
            case R.id.btn_feedback:
                FeedbackAgent agent = new FeedbackAgent(AboutActivity.this);
                agent.startFeedbackActivity();
                break;
            case R.id.btn_checkupdate:
                checkUpdate();
                break;
            case R.id.btn_outweb:
                Uri uri = Uri.parse("http://www.kuho.me/kuone");
                startActivity(new Intent(Intent.ACTION_VIEW,uri));
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();    //To change body of overridden methods use File | Settings | File Templates.
    }

    private void checkUpdate(){
        UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
            @Override
            public void onUpdateReturned(int updateStatus,UpdateResponse updateInfo) {
                switch (updateStatus) {
                    case 0: // has update
                        //UmengUpdateAgent.showUpdateDialog(AboutActivity.this, updateInfo);
                        break;
                    case 1: // has no update
                        Toast.makeText(AboutActivity.this, getText(R.string.str_hint_update_no), Toast.LENGTH_SHORT)
                                .show();
                        break;
                    case 2: // none wifi
                        Toast.makeText(AboutActivity.this,getText(R.string.str_hint_update_nowifi) , Toast.LENGTH_SHORT)
                                .show();
                        break;
                    case 3: // time out
                        Toast.makeText(AboutActivity.this, getText(R.string.str_hint_update_timeout), Toast.LENGTH_SHORT)
                                .show();
                        break;
                }
            }
        });
        UmengUpdateAgent.forceUpdate(AboutActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**使用SSO授权必须添加如下代码 */
        UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode) ;
        if(ssoHandler != null){
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UmengUpdateAgent.setUpdateListener(null);
    }

    public static String getVersion(Context context)//获取版本号
    {
        try {
            PackageInfo pi=context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "unknow";
    }

}