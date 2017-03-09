package rflash.com.allpay.http;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

import rflash.com.allpay.callback.HttpRequestCallBack;


/**
 * @author: R.flash
 * @CreateDate: 2017-03-08-10:25
 * @Description: [一句话描述该类的功能]
 * @UpdateUser: [修改人]
 * @UpdateDate: [修改日期 如：1979/1/1 0:0 24小时制]
 * @UpdateRemark: [说明本次修改内容]
 * @Version: [v1.0]
 */
public class HttpRequestMethod {

    public static final String TAG = "HttpRequestMethod";

    public static String requestPost(HashMap<String, String> paramsMap, String baseUrl, HttpRequestCallBack httpRequestCallBack) {
        HttpURLConnection urlConn = null;
        try {
            //合成参数
            StringBuilder tempParams = new StringBuilder();
            int pos = 0;
            for (String key : paramsMap.keySet()) {
                if (pos > 0) {
                    tempParams.append("&");
                }
                tempParams.append(String.format("%s=%s", key, URLEncoder.encode(paramsMap.get(key), "utf-8")));
                pos++;
            }
            String params = tempParams.toString();
            // 请求的参数转换为byte数组
            byte[] postData = params.getBytes();
            // 新建一个URL对象
            URL url = new URL(baseUrl);
            // 打开一个HttpURLConnection连接
            urlConn = (HttpURLConnection) url.openConnection();
            // 设置连接超时时间
            urlConn.setConnectTimeout(5 * 1000);
            //设置从主机读取数据超时
            urlConn.setReadTimeout(5 * 1000);
            // Post请求必须设置允许输出 默认false
            urlConn.setDoOutput(true);
            //设置请求允许输入 默认是true
            urlConn.setDoInput(true);
            // Post请求不能使用缓存
            urlConn.setUseCaches(false);
            // 设置为Post请求
            urlConn.setRequestMethod("POST");
            //设置本次连接是否自动处理重定向
            urlConn.setInstanceFollowRedirects(true);
            // 配置请求Content-Type
            urlConn.setRequestProperty("Content-Type", "application/json");
            // 开始连接
            urlConn.connect();
            // 发送请求参数
            DataOutputStream dos = new DataOutputStream(urlConn.getOutputStream());
            dos.write(postData);
            dos.flush();
            dos.close();
            int responseCode = urlConn.getResponseCode();
            Log.i(TAG, "请求相应码:" + responseCode);
            // 判断请求是否成功
            if (responseCode == 200) {
                // 获取返回的数据
                String result = streamToString(urlConn.getInputStream());
                Log.e(TAG, "Post方式请求成功，result--->" + result);
                return result;
            } else {
                httpRequestCallBack.httpError(responseCode + "", "根据返回的请求响应码定义");
            }
//            else if( responseCode >= 400  && responseCode < 500) {
//                Log.e(TAG, "请求错误");
//                Toast.show(mActivity, responseCode + "  请求错误");
//
//            }else if(responseCode >= 500 ){
//                Log.e(TAG, "服务器错误");
//                Toast.show(mActivity,responseCode+"  服务器错误");
//            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        } finally {
            urlConn.disconnect();
        }
        return null;
    }

    /**
     * 将输入流转换成字符串
     *
     * @param is 从网络获取的输入流
     * @return
     */
    private static String streamToString(InputStream is) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            baos.close();
            is.close();
            byte[] byteArray = baos.toByteArray();
            return new String(byteArray);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }

    public static String requestGet(HashMap<String, String> paramsMap, String baseUrl, HttpRequestCallBack httpRequestCallBack) {
        try {
            StringBuilder tempParams = new StringBuilder();
            int pos = 0;
            for (String key : paramsMap.keySet()) {
                if (pos > 0) {
                    tempParams.append("&");
                }
                tempParams.append(String.format("%s=%s", key, URLEncoder.encode(paramsMap.get(key), "utf-8")));
                pos++;
            }
            String requestUrl = baseUrl + tempParams.toString();
            // 新建一个URL对象
            URL url = new URL(requestUrl);
            // 打开一个HttpURLConnection连接
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            // 设置连接主机超时时间
            urlConn.setConnectTimeout(5 * 1000);
            //设置从主机读取数据超时
            urlConn.setReadTimeout(5 * 1000);
            // 设置是否使用缓存  默认是true
            urlConn.setUseCaches(true);
            // 设置为Post请求
            urlConn.setRequestMethod("GET");
            //urlConn设置请求头信息
            //设置请求中的媒体类型信息。
            urlConn.setRequestProperty("Content-Type", "application/json");
            //设置客户端与服务连接类型
            urlConn.addRequestProperty("Connection", "Keep-Alive");
            // 开始连接
            urlConn.connect();
            // 判断请求是否成功
            int responseCode = urlConn.getResponseCode();
            if (responseCode == 200) {
                // 获取返回的数据
                String result = streamToString(urlConn.getInputStream());
                return result;
            } else {
                httpRequestCallBack.httpError(responseCode + "", "根据返回的请求响应码定义");
            }
            // 关闭连接
            urlConn.disconnect();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }
}  
