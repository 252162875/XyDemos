package kx.rnd.com.permissionstest.mvpdagger.component;

import dagger.Component;
import kx.rnd.com.permissionstest.mvpdagger.MvpPlusDaggerActivity;

/**
 * author: 梦境缠绕
 * created on: 2018/3/15 0015 16:10
 * description:
 */
@Component
public interface MvpPlusDaggerComponent {
    void inject(MvpPlusDaggerActivity mvpPlusDaggerActivity);
}
