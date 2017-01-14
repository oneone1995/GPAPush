package cn.zheteng123.hdu.util;

import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by wangl on 2017/1/12.
 */
public class SMSUtil {
    public static boolean sendSMS(String name, String score, String tel) {
        InputStream inStream = SMSUtil.class.getClassLoader().getResourceAsStream("config.properties");

        Properties prop = new Properties();
        try {
            prop.load(inStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String appKey = prop.getProperty("app_key");
        String appSecret = prop.getProperty("app_secret");
        System.out.println(appKey);
        System.out.println(appSecret);
        //请填写自己的app key,app secret
        TaobaoClient client = new DefaultTaobaoClient("http://gw.api.taobao.com/router/rest", appKey, appSecret);
        AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();

        req.setSmsType("normal");
        req.setSmsFreeSignName("HDU成绩推送");
        req.setSmsParamString("{\"subject\":\""+ name.substring(0, 14) +"\",\"score\":\""+ score +"\"}");
        //请填写需要接收的手机号码
        req.setRecNum(tel);
        //短信模板id
        req.setSmsTemplateCode("SMS_40865037");

        AlibabaAliqinFcSmsNumSendResponse rsp = null;
        try {
            rsp = client.execute(req);
        } catch (ApiException e) {
            e.printStackTrace();
        }

        System.out.println(rsp.getBody());
        return rsp.getBody().contains("\"success\":true");
    }
}
