package rflash.com.allpay.wechatpay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

import rflash.com.allpay.R;


/**
 * @author: R.flash
 * @CreateDate: 2017-03-07-17:07
 * @Description: [一句话描述该类的功能]
 * @UpdateUser: [修改人]
 * @UpdateDate: [修改日期 如：1979/1/1 0:0 24小时制]
 * @UpdateRemark: [说明本次修改内容]
 * @Version: [v1.0]
 */
public class WXPayCallbackActivity extends Activity implements IWXAPIEventHandler {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wxpay_call_back);
        if (WeChatPay.getInstance() != null) {
            WeChatPay.getInstance().getIWXAPI().handleIntent(getIntent(), this);
        } else {
            finish();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        if (WeChatPay.getInstance() != null) {
            WeChatPay.getInstance().getIWXAPI().handleIntent(intent, this);
        }
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        if (baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            WeChatPay.getInstance().onResp(baseResp.errCode);
            finish();
        }
    }
}
