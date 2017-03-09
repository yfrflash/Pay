package rflash.com.allpay;

/**
 * @author: R.flash
 * @CreateDate: 2017-03-07-15:21
 * @Description: [一句话描述该类的功能]
 * @UpdateUser: [修改人]
 * @UpdateDate: [修改日期 如：1979/1/1 0:0 24小时制]
 * @UpdateRemark: [说明本次修改内容]
 * @Version: [v1.0]
 */
public class Constant {
    public static final String HTTP_FLAG_SDK = "sdk";
    public static final String HTTP_FLAG_FAST = "fast";
    public static final String HTTP_RESULT_NULL = "h00";
    public static final String ALIPAY_SUCCESS = "9000";//支付成功
    public static final String ALIPAY_DEALING = "8000";//支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
    public static final String ALIPAY_CANCLE = "6001";//支付取消
    public static final String ALIPAY_ERROR_NETWORK = "6002";//"网络连接出错"
    public static final String ALIPAY_ERROR_PAY = "4000";//支付错误
    public static final String ALIPAY_NO_APPID = "01";//支付宝支付没有配置appid
    public static final String WXPAY_LOW_SDK = "02";//未安装微信或微信版本过低
    public static final String WXPAY_ERROR_PAY = "03";//微信支付失败

    public static final int FAST_RESPONSE_CODE = 123456;
    public static final String FAST_PAY_REQUEST_ERROR_CODE = "f001";//请求快捷支付时，参数缺失；code
    public static final String FAST_PAY_REQUEST_ERROR_MSG = "参数缺失";//请求快捷支付时，参数缺失；msg
    public static final String FAST_PAY_REQUEST_MES = "resMes";//onActivityResult回调函数中intent的键
    public static final String FAST_PAY_REQUEST_CODE = "code";//onActivityResult回调函数中intent的键
}
