package kx.rnd.com.permissionstest.mvp.base;

import java.util.Map;

/**
 * author: 梦境缠绕
 * created on: 2018/3/14 0014 14:20
 * description:
 */

public abstract class BaseModel<T> {
    protected Map<String,String> mParams;

    public BaseModel params(Map<String,String> args) {
        mParams = args;
        return this;
    }

    public abstract void execute(CallBack<T> callBack);

    protected void requestGetApi(String url, CallBack<T> callBack) {
    }

    protected void requestPostApi(String url, Map params, CallBack<T> callBack) {
    }
}
