package rflash.com.allpay.http;

import android.app.Activity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import rflash.com.allpay.alipay.AliPay;
import rflash.com.allpay.callback.HttpRequestCallBack;
import rflash.com.allpay.callback.PayCallBack;
import rflash.com.allpay.wechatpay.WeChatPay;


/**
 * @author: R.flash
 * @CreateDate: 2017-03-06-15:00
 * @Description: [一句话描述该类的功能]
 * @UpdateUser: [修改人]
 * @UpdateDate: [修改日期 如：1979/1/1 0:0 24小时制]
 * @UpdateRemark: [说明本次修改内容]
 * @Version: [v1.0]
 */
public class JsonRequest {
    //**********统一下单结果参数*************//
    //requestNo 请求流水号  M
    //version 版本号 M V1.0
    //productId  产品类型 M  :0104-微信扫码支付 0105-微信公众号 0106-微信刷卡（反扫）0109-支付宝扫码支付 0110-支付宝刷卡支付
    //transId 交易类型  M 10
    //orgNo 机构号 M
    //orderDate 订单日期 M
    //orderNo 商户订单号 M
    //returnUrl 页面通知地址 M
    //notifyUrl 异步通知地址 M
    //transAmt 交易金额 M
    //appid 接入公众号appid C
    //accessAppid 公众号APPID C
    //openid 微信openId C
    //authCode 授权码 C
    //storeId 商户门店编号 C
    //terminalId 商户机具终端编号 C
    //subMerNo 收款商户号 M
    //respCode 应答码 M
    //respDesc 应答码描述 M
    //codeUrl 二维码链接 C
    //imgUrl 二维码图片 C
    //payInfo 公众号支付信息 C
    //signature 验签字段 M
//##########################################


    /**
     * 解析统一下单结果
     * @param result
     * @param mActivity
     * @param httpRequestCallBack
     * @param payCallBack
     */
    public static void parseSDKJsonObject(String result,Activity mActivity,HttpRequestCallBack httpRequestCallBack,PayCallBack payCallBack){
        try {
            //具体参数解释往上看
            JSONObject jsonObject = new JSONObject(result);
            String requestNo = jsonObject.getString("requestNo");
            String version = jsonObject.getString("version");
            String productId = jsonObject.getString("productId");
            String transId = jsonObject.getString("transId");
            String orgNo = jsonObject.getString("orgNo");
            String orderDate = jsonObject.getString("orderDate");
            String orderNo = jsonObject.getString("orderNo");
            String returnUrl = jsonObject.getString("returnUrl");
            String notifyUrl = jsonObject.getString("notifyUrl");
            String transAmt = jsonObject.getString("transAmt");
            String appid = jsonObject.getString("appid");
            String accessAppid = jsonObject.getString("accessAppid");
            String openid = jsonObject.getString("openid");
            String authCode = jsonObject.getString("authCode");
            String storeId = jsonObject.getString("storeId");
            String terminalId = jsonObject.getString("terminalId");
            String subMerNo = jsonObject.getString("subMerNo");
            String respCode = jsonObject.getString("respCode");
            String respDesc = jsonObject.getString("respDesc");
            String codeUrl = jsonObject.getString("codeUrl");
            String imgUrl = jsonObject.getString("imgUrl");
            String payInfo = jsonObject.getString("payInfo");
            String signature = jsonObject.getString("signature");
            String subject = "";
            String body = "";
            if(respCode.equals("00")){
            //去支付
                if(productId.equals("alipay")){//暂时这么定义,表示支付宝支付
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date();
                    String timeStamp = simpleDateFormat.format(date);
                    //{"timeout_express":"30m","product_code":"QUICK_MSECURITY_PAY","total_amount":"0.01","subject":"1","body":"我是测试数据","out_trade_no":"" + getOutTradeNo() +  ""}
                    String bizContent = "{\"timeout_express\":"+"\"30m\",";
                    bizContent += "\"product_code\":"+"\"QUICK_MSECURITY_PAY\"";
                    bizContent += "total_amount\":"+"\""+transAmt+"\"";
                    bizContent += "subject\":"+"\""+subject+"\"";
                    bizContent += "out_trade_no\":"+"\""+orderNo+"\"";
                    bizContent += "body\":"+"\""+body + "\"";//非必须
                    AliPay.Builder builder = new AliPay.Builder(mActivity);
                    builder.setTimeStamp(timeStamp)
                            .setAPPID(appid)
                            .setBizContent(bizContent)
                            .setSign(signature)
                            .setNotifyUrl(notifyUrl)
                            .setPayCallBack(payCallBack)
                            .payV2();
                }else if(productId.equals("wechat")){//表示微信支付
                    String wxAppId = appid;//微信开放平台审核通过的应用APPID
                    String partnerid = subMerNo;//微信支付分配的商户号
                    String prepayid = "";//微信返回的支付交易会话ID
                    String packages = "Sign=WXPay";//暂填写固定值Sign=WXPay
                    String noncestr = "";//随机字符串，不长于32位。推荐随机数生成算法
                    String timestamp = "";//时间戳
                    String sign = signature;//签名
                    WeChatPay.init(mActivity, appid);
                    WeChatPay.getInstance().doPay(payCallBack,partnerid,prepayid,packages,noncestr,timestamp,sign);
                }
            }else {
                httpRequestCallBack.httpError(respCode,respDesc);
//                Toast.show(mActivity, respCode + "  " + respDesc);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}  
