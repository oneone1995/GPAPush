package cn.zheteng123.hdu.util;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by wangl on 2017/1/12.
 */
public class SMSUtilTest {
    @Test
    public void sendSMS() throws Exception {
        System.out.println(SMSUtil.sendSMS("test", "test", "test"));
    }

}