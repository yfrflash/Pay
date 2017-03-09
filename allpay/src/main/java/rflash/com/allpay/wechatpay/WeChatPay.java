package rflash.com.allpay.wechatpay;

import android.app.Activity;

import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import rflash.com.allpay.Constant;
import rflash.com.allpay.callback.PayCallBack;


/**
 * @author: R.flash
 * @CreateDate: 2017-03-07-10:20
 * @Description: [一句话描述该类的功能]
 * @UpdateUser: [修改人]
 * @UpdateDate: [修改日期 如：1979/1/1 0:0 24小时制]
 * @UpdateRemark: [说明本次修改内容]
 * @Version: [v1.0]
 */
public class WeChatPay {

    private static WeChatPay weChatPay;
    private IWXAPI mWXApi;
    private PayCallBack payCallBack;
    private String appid;

    private WeChatPay(Activity mActivity, String wx_appid) {
        this.appid = wx_appid;
        mWXApi = WXAPIFactory.createWXAPI(mActivity, null);
        mWXApi.registerApp(wx_appid);
    }
    public static void init(Activity mActivity, String wx_appid) {
        if(weChatPay == null) {
            weChatPay = new WeChatPay(mActivity, wx_appid);
        }
    }
    public static WeChatPay getInstance(){
        return weChatPay;
    }

    public  IWXAPI getIWXAPI(){
        return mWXApi;
    }



    /**
     * 发起微信支付
     *
     String wxAppId = appid;//微信开放平台审核通过的应用APPID
     String partnerid = subMerNo;//微信支付分配的商户号
     String prepayid = "";//微信返回的支付交易会话ID
     String packages = "Sign=WXPay";//暂填写固定值Sign=WXPay
     String noncestr = "";//随机字符串，不长于32位。推荐随机数生成算法
     String timestamp = "";//时间戳
     String sign = signature;//签名
     */
    public void doPay(PayCallBack payCallBack,String partnerid,String prepayid,String packages,String noncestr,String timestamp,String sign){
        if(!check()) {
            payCallBack.payError(Constant.WXPAY_LOW_SDK,"未安装微信或微信版本过低");
            return;
        }
        this.payCallBack = payCallBack;
        PayReq request = new PayReq();
        request.appId = appid;
        request.partnerId = partnerid;
        request.prepayId = prepayid;
        request.packageValue = packages;
        request.nonceStr = noncestr;
        request.timeStamp = timestamp;
        request.sign = sign;
        mWXApi.sendReq(request);

    }

    //检测是否支持微信支付
    private boolean check() {
        return mWXApi.isWXAppInstalled() && mWXApi.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
    }
    //支付回调响应
    public void onResp(int error_code) {


        if(error_code == 0) {   //成功
            payCallBack.paySuccess();
        } else if(error_code == -1) {   //错误
            payCallBack.payError(Constant.WXPAY_ERROR_PAY,"微信支付失败");
        } else if(error_code == -2) {   //取消
            payCallBack.payCancel();
        }

        payCallBack = null;
    }
}  
