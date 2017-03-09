package rflash.com.allpay.callback;

/**
 * Created by yang on 2017/3/7.
 */
public interface PayCallBack {
    void paySuccess();
    void payCancel();
    void payError(String errorCode, String message);
    void payDealing();
}
