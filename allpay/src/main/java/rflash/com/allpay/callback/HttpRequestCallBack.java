package rflash.com.allpay.callback;

/**
 * Created by yang on 2017/3/7.
 */
public interface HttpRequestCallBack {
    void httpSuccess();
    void httpError(String errorCode, String message);
    void onPreExecute();
}
