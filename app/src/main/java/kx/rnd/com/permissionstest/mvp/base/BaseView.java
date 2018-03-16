package kx.rnd.com.permissionstest.mvp.base;

import android.content.Context;

/**
 * author: 梦境缠绕
 * created on: 2018/3/14 0014 14:26
 * description:
 */

public interface BaseView {
    /**
     * 显示正在加载view
     */
    void showLoading();

    /**
     * 关闭正在加载view
     */
    void hideLoading();

    /**
     * 显示提示
     *
     * @param msg
     */
    void showToast(String msg);

    /**
     * 显示请求错误提示
     */
    void showErr();

    /**
     * 获取上下文
     *
     * @return 上下文
     */
    Context getContext();
}
