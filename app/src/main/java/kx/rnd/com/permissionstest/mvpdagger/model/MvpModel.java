package kx.rnd.com.permissionstest.mvpdagger.model;

import android.os.Handler;

import kx.rnd.com.permissionstest.mvpdagger.base.BaseModel;
import kx.rnd.com.permissionstest.mvpdagger.base.CallBack;

/**
 * author: 梦境缠绕
 * created on: 2018/3/15 0015 10:21
 * description:
 */

public class MvpModel extends BaseModel<String> {
    @Override
    public void execute(final CallBack<String> callBack) {
        // 模拟网络请求耗时操作
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // mParams 是从父类得到的请求参数
                switch (mParams.get("uid")) {
                    case "normal":
                        callBack.onSuccess("根据参数" + mParams.get("uid") + "的请求网络数据成功");
                        break;
                    case "failure":
                        callBack.onFailure("请求失败：参数有误");
                        break;
                    case "error":
                        callBack.onError();
                        break;
                }
                callBack.onComplete();
            }
        }, 2000);
    }
}

