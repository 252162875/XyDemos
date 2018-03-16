package kx.rnd.com.permissionstest.mvpdagger.view;

import kx.rnd.com.permissionstest.mvpdagger.base.BaseView;

/**
 * author: 梦境缠绕
 * created on: 2018/3/14 0014 14:55
 * description:
 */

public interface MvpView extends BaseView {
    /**
     * 当数据请求成功后，调用此接口显示数据
     *
     * @param data 数据源
     */
    void showData(String data);
}
