package kx.rnd.com.permissionstest.mvp.presenter;

import java.util.Map;

import kx.rnd.com.permissionstest.mvp.base.BasePresenter;
import kx.rnd.com.permissionstest.mvp.base.CallBack;
import kx.rnd.com.permissionstest.mvp.base.DataModelManager;
import kx.rnd.com.permissionstest.mvp.model.MvpModel;
import kx.rnd.com.permissionstest.mvp.view.MvpView;

/**
 * author: 梦境缠绕
 * created on: 2018/3/15 0015 10:18
 * description:
 */

public class MvpPresenter extends BasePresenter<MvpView> {
    public void getData(Map<String, String> params) {
        if (!isViewAttached()) {
            return;
        }
        getView().showLoading();
        MvpModel mvpModel = (MvpModel) DataModelManager.newInstance(MvpModel.class.getName()).params(params);
        mvpModel.execute(new CallBack<String>() {
            @Override
            public void onSuccess(String data) {
                //调用view接口显示数据
                if (isViewAttached()) {
                    getView().showData(data);
                }
            }

            @Override
            public void onFailure(String msg) {
                //调用view接口提示失败信息
                if (isViewAttached()) {
                    getView().showToast(msg);
                }
            }

            @Override
            public void onError() {
                //调用view接口提示请求异常
                if (isViewAttached()) {
                    getView().showErr();
                }
            }

            @Override
            public void onComplete() {
                // 隐藏正在加载进度条
                if (isViewAttached()) {
                    getView().hideLoading();
                }
            }
        });
    }
}
