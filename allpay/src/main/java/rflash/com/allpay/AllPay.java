package rflash.com.allpay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import java.util.HashMap;

import rflash.com.allpay.callback.HttpRequestCallBack;
import rflash.com.allpay.callback.PayCallBack;
import rflash.com.allpay.fastpay.FastPayH5Activity;
import rflash.com.allpay.http.HttpRequest;


/**
 * @author: R.flash
 * @CreateDate: 2017-03-06-13:41
 * @Description: [一句话描述该类的功能]
 * @UpdateUser: [修改人]
 * @UpdateDate: [修改日期 如：1979/1/1 0:0 24小时制]
 * @UpdateRemark: [说明本次修改内容]
 * @Version: [v1.0]
 */
public class AllPay {
    //   统一下单 请求地址（测试地址）：http://113.98.101.189:8082/paygate/payway/unifiedorder
//   统一下单 请求地址（生产地址）：http://gate.xpaydata.com:8082/paygate/payway/unifiedorder
    public static String URL = "";

    /**
     * 微信支付宝支付
     *
     * @param deBug true 设置生产地址 false  设置测试地址
     */
    public static void doSDKPay(boolean deBug, HashMap<String, String> paramsMap, Activity mActivity, HttpRequestCallBack httpRequestCallBack, PayCallBack payCallBack) {
        if (deBug) {
            URL = "http://gate.xpaydata.com:8082/paygate/payway/unifiedorder";
        } else {
            URL = "http://113.98.101.189:8082/paygate/payway/unifiedorder";
        }

        HttpRequest http = new HttpRequest(URL, paramsMap, mActivity, httpRequestCallBack, payCallBack);
        http.execute();
    }

    public static void doSDKPay(HashMap<String, String> paramsMap, Activity mActivity, HttpRequestCallBack httpRequestCallBack, PayCallBack payCallBack) {
        doSDKPay(true, paramsMap, mActivity, httpRequestCallBack, payCallBack);
    }

    /**
     * 快捷支付
     *
     * @param paramsMap
     * @param mActivity
     */
    public static void doFastPay(HashMap<String, String> paramsMap, Activity mActivity ,int requestCode) {
        doFastPay(true, paramsMap,mActivity,requestCode);
    }

    public static void doFastPay(boolean deBug, HashMap<String, String> paramsMap, Activity mActivity,int requestCode) {
        if (deBug) {
            URL = "";
        } else {
            URL = "";
        }
        Intent intent = new Intent(mActivity, FastPayH5Activity.class);
        Bundle bundle = new Bundle();
        bundle.putString("url", URL);
        bundle.putString("orderNo", paramsMap.get("orderNo"));//订单号。
        bundle.putString("orgNo", paramsMap.get("orgNo"));//机构号
        bundle.putString("subMerNo", paramsMap.get("subMerNo"));//。商户号。
        bundle.putString("transAmt", paramsMap.get("transAmt"));//。金额。。
        intent.putExtras(bundle);
        mActivity.startActivityForResult(intent,requestCode);
    }


}  
