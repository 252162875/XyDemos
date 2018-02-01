package kx.rnd.com.permissionstest.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.just.agentweb.AgentWeb;
import com.just.agentweb.ChromeClientCallbackManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import kx.rnd.com.permissionstest.R;

public class AgentWebViewActivity extends AppCompatActivity {
    @BindView(R.id.ll_root)
    LinearLayout ll_root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_web_view);
        ButterKnife.bind(this);
        ChromeClientCallbackManager.ReceivedTitleCallback receivedTitleCallback = new ChromeClientCallbackManager.ReceivedTitleCallback() {
            @Override
            public void onReceivedTitle(WebView view, String title) {

            }
        };
        AgentWeb agentWeb = AgentWeb.with(this)//传入Activity
                .setAgentWebParent(ll_root, new LinearLayout.LayoutParams(-1, -1))//传入AgentWeb 的父控件 ，如果父控件为 RelativeLayout ， 那么第二参数需要传入 RelativeLayout.LayoutParams
                .useDefaultIndicator()// 使用默认进度条
                .defaultProgressBarColor() // 使用默认进度条颜色
                .setReceivedTitleCallback(receivedTitleCallback) //设置 Web 页面的 title 回调
                .createAgentWeb()//
                .ready()
                .go("http://www.ctolib.com/Justson-AgentWeb.html");

    }
}
