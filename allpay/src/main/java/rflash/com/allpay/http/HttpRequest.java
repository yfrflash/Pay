package rflash.com.allpay.http;

import android.app.Activity;
import android.os.AsyncTask;

import java.util.HashMap;

import rflash.com.allpay.Constant;
import rflash.com.allpay.callback.HttpRequestCallBack;
import rflash.com.allpay.callback.PayCallBack;


/**
 * @author: R.flash
 * @CreateDate: 2017-03-06-13:19
 * @Description: [一句话描述该类的功能]
 * @UpdateUser: [修改人]
 * @UpdateDate: [修改日期 如：1979/1/1 0:0 24小时制]
 * @UpdateRemark: [说明本次修改内容]
 * @Version: [v1.0]
 */
public class HttpRequest extends AsyncTask<Void,Integer,String>{

    public static final String TAG = "HttpRequest";
    private  String baseUrl;
    private HashMap<String, String> paramsMap;
    private Activity mActivity;
//    private WaitDialog waitDialog;
    private HttpRequestCallBack httpRequestCallBack;
    private PayCallBack payCallBack;

    public HttpRequest(String url,HashMap<String, String> paramsMap,Activity mActivity,HttpRequestCallBack httpRequestCallBack,PayCallBack payCallBack) {
        this.baseUrl = url;
        this.mActivity = mActivity;
        this.paramsMap = paramsMap;
        this.httpRequestCallBack = httpRequestCallBack;
        this.payCallBack = payCallBack;
//        waitDialog = new WaitDialog(mActivity);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        httpRequestCallBack.onPreExecute();
//        waitDialog.show();
    }





    @Override
    protected String doInBackground(Void... params) {
        return HttpRequestMethod.requestPost(this.paramsMap, baseUrl, httpRequestCallBack);
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
//        waitDialog.dismiss();
//        waitDialog = null;
        if(result != null){//解析结果
            httpRequestCallBack.httpSuccess();
            JsonRequest.parseSDKJsonObject(result, mActivity, httpRequestCallBack, payCallBack);
        }else {
            httpRequestCallBack.httpError(Constant.HTTP_RESULT_NULL,"请求返回值为空");
        }
    }
}
