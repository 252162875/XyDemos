package kx.rnd.com.permissionstest;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.baidu.mapapi.SDKInitializer;
import com.squareup.leakcanary.LeakCanary;
import com.taobao.sophix.PatchStatus;
import com.taobao.sophix.SophixManager;
import com.taobao.sophix.listener.PatchLoadStatusListener;
import com.tencent.bugly.crashreport.CrashReport;

import kx.rnd.com.permissionstest.utils.UiUtils;


/**
 * 自定义的Application 1、运行的过程中只有一个实例 2、一个应用程序最先执行的方法 运行在主线程中
 * 注意：在AndroidManifest文件中进行注册
 */
public class BaseApplication extends Application {


    private static Context context;
    private static Handler handler;
    private static int mainThreadId;

    @Override
    public void onCreate() {
        super.onCreate();


        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(this);
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        // Normal app init code...

        // 初始化context对象，context对象使用的非常多
        context = getApplicationContext();
        // 获取主线程的handler 相当于获取主线程的消息队列
        handler = new Handler();
        // 获取主线程的id 哪一个方法调用了myTid myTid就返回那个方法所在线程的id
        mainThreadId = android.os.Process.myTid();
        // bugly异常处理
        CrashReport.initCrashReport(getApplicationContext(), "5df4a7882c", false);
        boolean runOnUiThread = UiUtils.isRunOnUiThread();
        if (runOnUiThread) {
            // queryAndLoadNewPatch不可放在attachBaseContext 中，否则无网络权限，建议放在后面任意时刻，如onCreate中
            SophixManager.getInstance().queryAndLoadNewPatch();
        } else {
            UiUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // queryAndLoadNewPatch不可放在attachBaseContext 中，否则无网络权限，建议放在后面任意时刻，如onCreate中
                    SophixManager.getInstance().queryAndLoadNewPatch();
                }
            });
        }
    }

    public static Context getContext() {
        return context;
    }

    public static Handler getHandler() {
        return handler;
    }

    public static int getMainThreadId() {
        return mainThreadId;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
        String appVersion;
        try {
            appVersion = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
        } catch (Exception e) {
            appVersion = "1.0.0";
        }
        // initialize最好放在attachBaseContext最前面，初始化直接在Application类里面，切勿封装到其他类
        SophixManager.getInstance().setContext(this)
                .setAppVersion(appVersion)
                .setAesKey("0123456789123456")
                .setSecretMetaData("24787410-1", "1f47b2978e5740d0414f83a723455f5d", "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDiF2eT9OVIy35WhlyrYI8p3YcQxRADuNQYWnOaDLEhgXHCfT6NPePXwlbvQG0V1iXtlMzNKek1bwB6y2GX2Xtdy02eFjDjveicYl4ZnSqtHB1yBpVI2zuiWR7oz6XgsvsQHLqHRoHrVsHAjaVCmWqAo4CjCn33gQVE5xKHkuVFxp6P434oPRy3v5rNRSEohYeOGJMzN/+fknR3LArHjq92Nsan5Yffn7DPRd0ETU9yjLAHmrh39aN/rqM3DGdwmYIgs2bQ4uIPITtY2kWSPdg2V63cEx5Os9n0fJNSPIAxnYRwASrPALeKkFmpFRYkFU7CUNNwg1y4n2XL+5Dg9SIlAgMBAAECggEAGySsLrsr+71SgZjH71s8VM5ftMez0H6UGAhj8I6thVRBEBRxOgvAGtseDMMzVXfMPeWrYMYzVVLqP9PC97QId44810navLzD/GJi4bhYs0e40UOxEHohgrAoYdLsGznhomGQk0UHu/VCdZeLy58XhjIa83gC/3/h3g/Pza75D5M7FQRgiDfnPmszh1MMJYjoIB9yGA0OGT04hbGxrK3nzZB3vPo2vb+DsykSVJfnhLuIwQtsdnD5EX48A8IUcM1EzRvKUJfZcLpMUUpoApLBR4GXkyKeL1Mh9gKW9Ao00XYtvWItsjCGIV/YNKGxLaz1eq4Nhwbozp9pNZq0X2L+gQKBgQD2zaXqZsfCccVYG9DP/1EsEY64SjhGBdlbVbI3v3i1iBJdVLi3XplqqEiVn9PA2FDyoF/ba8T7x75yzz/UI13pOe7kVy2AeUnvLp/q1KVOzhlVlh75XxBgLflmb39SncXe/3338nmYQAGbe16u8H//UlHHqP77op0Gqg9+ybOuqQKBgQDqhC2ExPVOGBYMeDxe8AkYKrCRIgf8B/rlPLwXBX8epilx3hvnipT/p89rIPxaAI9gVDj+C1zKhI18/1pMqwNzmhdurje5Bo6kKHKJK97ziK/c//5AuXRSCxUKDP/JHU+Ju3rmMU/MlfqVbUr4/Li85zn+/WJJOBnNuYv1IUwxHQKBgQDFRGSscdCTM9q37Su/ROT9o17/IJvQJFaycVHMy2dROdzr6o7mSl+356B0r9ZSWtSAyf00ZfSCBwrLu6naBxbGFz9CFghRmyCkZBEAY01qKsQHK8HAMuLk7XpeDRqoz2DcODzYlV/peKeQPlDU5Xh5fLPPMd31Beoa7I0MG25aWQKBgQCBb+rfuQXb0Jd60nIfxESAvi8uK6ZUiOMF23vFCRechwGhA4JLTcXnRWvKKS6fTmfUNygtr3/Ll1WwhBAG+OyyxX3hbO78hI3+NQTiiQBJUkweonxmpPV6wvsdzQbwrejUkFHI87ajqLPh/ay/uF0OhmEQSDITT4+OaLbC2VoAUQKBgQCi8LjRcKh3DA/d9RPx7Q6ulLkSuZ8ofrLWzqq9DkfhYd8jDX/SvX6rN0DxtzO0FIs4VOx0G74xihklWkSAENysNe9OP96qPWaAeTrc6ecQhhLRoGJAAG1tkqFHjiOjFjSgandJurvghZ810t6ECI29ITOYGq7P/4cKzqFrp7UvOw==")
                .setEnableDebug(true)
                .setPatchLoadStatusStub(new PatchLoadStatusListener() {
                    @Override
                    public void onLoad(final int mode, final int code, final String info, final int handlePatchVersion) {
                        // 补丁加载回调通知
                        if (code == PatchStatus.CODE_LOAD_SUCCESS) {
                            Log.e("HOTFIX", info);
                            // 表明补丁加载成功
                        } else if (code == PatchStatus.CODE_LOAD_RELAUNCH) {
                            Log.e("HOTFIX", info);
                            // 表明新补丁生效需要重启. 开发者可提示用户或者强制重启;
                            // 建议: 用户可以监听进入后台事件, 然后调用killProcessSafely自杀，以此加快应用补丁，详见1.3.2.3
                        } else {
                            // 其它错误信息, 查看PatchStatus类说明
                            Log.e("HOTFIX", info);
                        }
                    }
                }).initialize();
    }
}