package kx.rnd.com.permissionstest.interfaces;

import kx.rnd.com.permissionstest.bean.DouBanBean;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface DouBanService {
    @GET("top250")
    Call<DouBanBean> getTopMovie1(@Query("start") int start, @Query("count") int count);

    @GET("top250")
    Observable<DouBanBean> getTopMovie2(@Query("start") int start, @Query("count") int count);
}