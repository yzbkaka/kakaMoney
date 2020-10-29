package com.example.yzbkaka.kakamoney;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.Random;

public class WebActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        webView = (WebView)findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        int num = new Random().nextInt(4);
        switch (num) {
            case 1:
                webView.loadUrl("http://qy.znz888.com/tradesoft.php?from=360_111_214002999_400558391_26948645173_4960464749&classifyid=2");
                break;
            case 2:
                webView.loadUrl("https://www.emstock.com.cn/dianjin/360zyqs2/index.html?src=360&kw=-2825228959668885687&plid=3435235147&grid=635075049&ad=1312327081&ag_kwid=14639-7-25fef150564b56dd.98b035e2be16e4b7&qhclickid=5a3c42061fabe0ea");
                break;
            case 3:
                webView.loadUrl("https://www.futuhk.com/tuiguang/sem?channel=321&subchannel=169000034");
                break;
            case 4:
                webView.loadUrl("http://wwv.jct5.cn/?wore=%20%E4%BC%97%E6%88%90%E8%AF%81%E5%88%B8");
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(WebActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
