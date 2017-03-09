package rflash.com.allpay.alipay;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;

import java.util.Map;

import rflash.com.allpay.Constant;
import rflash.com.allpay.callback.PayCallBack;


/**
 * @author: R.flash
 * @CreateDate: 2017-03-06-10:06
 * @Description: [一句话描述该类的功能]
 * @UpdateUser: [修改人]
 * @UpdateDate: [修改日期 如：1979/1/1 0:0 24小时制]
 * @UpdateRemark: [说明本次修改内容]
 * @Version: [v1.0]
 */
public class AliPay {

    public static class Builder {
        private Activity mActivity;

        public Builder(Activity activity) {
            this.mActivity = activity;
        }

        private String sign = "";
        private String APPID = "";
        //业务参数
        private String bizContent = "";
        //请求时间
        private String timeStamp = "";
        private String notifyUrl = "";
        private static final int SDK_PAY_FLAG = 1;
        private PayCallBack payCallBack;



        /**
         * 设置APPID
         */
        public Builder setAPPID(String appid) {
            this.APPID = appid;
            return this;
        }
        public Builder setNotifyUrl(String notifyUrl) {
            this.notifyUrl = notifyUrl;
            return this;
        }
        /**
         * 设置业务参数
         */
        public Builder setBizContent(String bizContent) {
            this.bizContent = bizContent;
            return this;
        }
        /**
         * 设置请求时间
         */
        public Builder setTimeStamp(String timeStamp) {
            this.timeStamp = timeStamp;
            return this;
        }
        /**
         * 设置签名
         */
        public Builder setSign(String sign) {
            this.sign = sign;
            return this;
        }
        /**
         * 设置回调
         */
        public Builder setPayCallBack(PayCallBack payCallBack) {
            this.payCallBack = payCallBack;
            return this;
        }

        @SuppressLint("HandlerLeak")
        private Handler mHandler = new Handler() {
            @SuppressWarnings("unused")
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case SDK_PAY_FLAG:
                        PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                        /**
                         * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                         */
                        String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                        String resultStatus = payResult.getResultStatus();
                        // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                        if (TextUtils.equals(resultStatus, Constant.ALIPAY_SUCCESS)) {
                                payCallBack.paySuccess();
                        } else if(TextUtils.equals(resultStatus, Constant.ALIPAY_CANCLE)) {		//支付取消
                            payCallBack.payCancel();
                        }else if(TextUtils.equals(resultStatus, Constant.ALIPAY_DEALING)) { //支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                            payCallBack.payDealing();
                        } else if(TextUtils.equals(resultStatus, Constant.ALIPAY_ERROR_NETWORK)) {     //网络连接出错
                            payCallBack.payError(Constant.ALIPAY_ERROR_NETWORK,"网络连接出错");
                        } else if(TextUtils.equals(resultStatus,Constant.ALIPAY_ERROR_PAY)) {        //支付错误
                            payCallBack.payError(Constant.ALIPAY_ERROR_PAY,"支付错误");
                        } else {
                            payCallBack.payError(resultStatus,"根据支付返回的错误码定义");
                        }
                        break;
                    default:
                        break;
                }
            }
        };

        /**
         * 支付宝支付业务
         */

        public void payV2() {
//            String bizContent,String timeStamp  判断是否配置
            if (TextUtils.isEmpty(APPID) ) {
                payCallBack.payError(Constant.ALIPAY_NO_APPID, "需要配置APPID");
                return;
            }
            Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID, bizContent, timeStamp, notifyUrl);
            String orderParam = OrderInfoUtil2_0.buildOrderParam(params);
            final String orderInfo = orderParam + "&" + sign;

            Runnable payRunnable = new Runnable() {

                @Override
                public void run() {
                    // 构造PayTask 对象
                    PayTask alipay = new PayTask(mActivity);
                    // 调用支付接口，获取支付结果
                    Map<String, String> result = alipay.payV2(orderInfo, true);
                    Message msg = new Message();
                    msg.what = SDK_PAY_FLAG;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                }
            };

            // 必须异步调用
            Thread payThread = new Thread(payRunnable);
            payThread.start();

        }

    }
//    private String bizContent;
//    private String timeStamp;
//    private String APPID = "";
//    private String sign = "";
//    private Builder payData;
//    private String notifyUrl;
//    private PayCallBack payCallBack;
//
//    public void setNotifyUrl(String notifyUrl) {
//        this.notifyUrl = notifyUrl;
//    }
//
//    public Builder build(Activity activity) {
//        payData = new Builder(activity);
//        payData.setAPPID(APPID);
//        payData.setBizContent(bizContent);
//        payData.setTimeStamp(timeStamp);
//        payData.setSign(sign);
//        payData.setNotifyUrl(notifyUrl);
//        payData.setPayCallBack(payCallBack);
//        return payData;
//    }
//
//    public void setPayCallBack(PayCallBack payCallBack) {
//        this.payCallBack = payCallBack;
//    }
//
//    public void setSign(String sign) {
//        this.sign = sign;
//    }
//
//    public void setTimeStamp(String timeStamp) {
//        this.timeStamp = timeStamp;
//    }
//
//    public void setBizContent(String bizContent) {
//        this.bizContent = bizContent;
//    }
//
//    public void setAPPID(String APPID) {
//        this.APPID = APPID;
//    }
//
//
//
//    /**
//     * 支付宝支付业务
//     */
//    public void payV2() {
//        if (payData != null) {
//            payData.payV2();
//
//
//        }
//    }


}
