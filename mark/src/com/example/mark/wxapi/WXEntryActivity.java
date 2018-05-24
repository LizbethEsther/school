
package com.example.mark.wxapi;

import com.example.mark.Constants;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        
        
        Constants.wx_api.handleIntent(getIntent(), this);
    }
        
   
   
   
 //΢��������Ӧ
	@Override
	public void onReq(BaseReq arg0) {
		// TODO Auto-generated method stub
		
	}

	 //���͵�΢���������Ӧ���
	public void onResp(BaseResp resp) {
		// TODO Auto-generated method stub
		switch (resp.errCode) {
        case BaseResp.ErrCode.ERR_OK:
            Log.i("WXTest","onResp OK");
            
            if(resp instanceof SendAuth.Resp){
                SendAuth.Resp newResp = (SendAuth.Resp) resp;
                //��ȡ΢�Ŵ��ص�code
                String code = newResp.code;
                Log.i("WXTest","onResp code = "+code);
            }
            
            break;
        case BaseResp.ErrCode.ERR_USER_CANCEL:
            Log.i("WXTest","onResp ERR_USER_CANCEL ");
            //����ȡ��
            break;
        case BaseResp.ErrCode.ERR_AUTH_DENIED:
            Log.i("WXTest","onResp ERR_AUTH_DENIED");
            //���ͱ��ܾ�
            break;
        default:
            Log.i("WXTest","onResp default errCode " + resp.errCode);
            //���ͷ���
            break;
	}
		finish();
	}
		
}