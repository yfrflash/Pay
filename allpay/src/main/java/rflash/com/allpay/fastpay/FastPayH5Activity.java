package rflash.com.allpay.fastpay;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import rflash.com.allpay.Constant;
import rflash.com.allpay.R;


public class FastPayH5Activity extends AppCompatActivity {

    private WebView fastPayWeb;
    private String baseUrl ;
    private Intent setIntent;
    WebSettings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fast_pay_h5);
        fastPayWeb = (WebView) findViewById(R.id.wb_fast_pay);
        setIntent = new Intent();
        initUrl();
        initWebView();
    }

    private void initWebView() {
        settings = fastPayWeb.getSettings();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        fastPayWeb.requestFocus();//触摸焦点起作用
        fastPayWeb.requestFocusFromTouch();//支持获取手势焦点，输入用户名、密码或其他
        fastPayWeb.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);//取消滚动条
        fastPayWeb.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);//设置允许js弹出alert对话框
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(true);//将图片调整到适合webview的大小
        settings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        fastPayWeb.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(baseUrl);
                return true;
            }
        });
        fastPayWeb.addJavascriptInterface(new JSHook() ,"android");
    }

    private void initUrl() {
        Intent intent = getIntent();
        if(intent != null){
            Bundle bundle = intent.getExtras();
            String orderNo = bundle.getString("orderNo");
            String orgNo = bundle.getString("orgNo");
            String subMerNo = bundle.getString("subMerNo");
            String transAmt = bundle.getString("transAmt");
            String url = bundle.getString("url");
            this.baseUrl = url + "?orderNo="+orderNo+"&orgNo="+orgNo+"&subMerNo="+subMerNo+"&transAmt="+transAmt;
        }else {
            setIntent.putExtra(Constant.FAST_PAY_REQUEST_MES,Constant.FAST_PAY_REQUEST_ERROR);
            setResult(Constant.FAST_RESPONSE_CODE,setIntent);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        settings.setJavaScriptEnabled(false);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && fastPayWeb.canGoBack()) {
            fastPayWeb.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

   public class JSHook{
       @JavascriptInterface
       public void activityResult(int code,String msg){
           setIntent.putExtra(Constant.FAST_PAY_REQUEST_MES,msg);
           setResult(code,setIntent);
           finish();
       }
   }
}
