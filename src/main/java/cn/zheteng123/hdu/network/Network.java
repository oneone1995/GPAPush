package cn.zheteng123.hdu.network;

import cn.zheteng123.hdu.pojo.Score;
import cn.zheteng123.hdu.util.CaptchaUtil;
import cn.zheteng123.hdu.util.MyCookieJar;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created on 2017/1/11.
 */
public class Network {

    private static OkHttpClient okHttpClient;
    private static final String CAPTCHA_PATH = "D:\\Captcha.jpg";

    static {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        okHttpClient = new OkHttpClient
                .Builder()
                .readTimeout(5, TimeUnit.SECONDS)
                .cookieJar(new MyCookieJar())
                .addInterceptor(httpLoggingInterceptor)
                .build();
    }

    /**
     * 根据用户名密码查询成绩信息
     * @param username 用户名（学号）
     * @param password 密码（经过 MD5 加密）
     * @return 成绩列表
     */
    public static List<Score> getScore(String username, String password) throws IOException {


        // 清除 cookie
        ((MyCookieJar)okHttpClient.cookieJar()).deleteCookie();

        // Get 访问登录页，获取隐藏表单项 lt
        Request reqGetLt = new Request.Builder().get()
                .url("http://jxgl.hdu.edu.cn/")
                .build();
        Response resGetLt = okHttpClient.newCall(reqGetLt).execute();
        String dataLt = resGetLt.body().string();
        Document docLt = Jsoup.parse(dataLt);
        String lt = docLt.select("input[name=lt]").val();
        System.out.println(lt);
        resGetLt.close();

        // 识别验证码
        Request reqGetCaptcha = new Request.Builder().get()
                .url("http://cas.hdu.edu.cn/cas/Captcha.jpg")
                .build();
        Response resGetCaptcha = okHttpClient.newCall(reqGetCaptcha).execute();
        InputStream isCaptcha = resGetCaptcha.body().byteStream();
        File file = new File(CAPTCHA_PATH);
        OutputStream os = new FileOutputStream(file);
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        while ((bytesRead = isCaptcha.read(buffer, 0, 8192)) != -1) {
            os.write(buffer, 0, bytesRead);
        }
        os.close();
        isCaptcha.close();
        String captchaResult = CaptchaUtil.recogniseCaptcha(CAPTCHA_PATH);
        System.out.println(captchaResult);

        // Post 登录教务系统
        Request reqPostLogin = new Request.Builder().post(
                new FormBody.Builder()
                        .add("username", username)
                        .add("password", password)
                        .add("lt", lt)
                        .add("captcha", captchaResult)
                        .build())
                .url("http://cas.hdu.edu.cn/cas/login")
                .build();
        Response resPostLogin = okHttpClient.newCall(reqPostLogin).execute();
        String dataPostLogin = resPostLogin.body().string();
        if (dataPostLogin.contains("账号或密码错误")) {
            System.out.println("账号或密码错误！");
            return null;
        }
        if (dataPostLogin.contains("错误信息提示")) {
            System.out.println("验证码错误！");
            return null;
        }
        resPostLogin.close();


        // 进入选课系统
        Request reqGetEduAdmin = new Request.Builder().get()
                .url("http://i.hdu.edu.cn/dcp/forward.action?path=dcp/apps/sso/jsp/ssoDcpSelf&appid=1142")
                .build();
        Response resGetEduAdmin = okHttpClient.newCall(reqGetEduAdmin).execute();
        String dataTemp = resGetEduAdmin.body().string();
        resGetEduAdmin.close();
//        System.out.println(dataTemp);
        while (dataTemp.contains("认证转向")) {
            Document docUrlJump = Jsoup.parse(dataTemp);
            String urlJump = docUrlJump.select("a").get(0).attr("href");
            Request reqTemp = new Request.Builder().get()
                    .url(urlJump)
                    .build();
            Response resTemp = okHttpClient.newCall(reqTemp).execute();
            dataTemp = resTemp.body().string();
            resTemp.close();
        }
//        System.out.println(dataTemp);


        // 获取成绩
        System.out.println("获取成绩");
        Request reqGetScore = new Request.Builder().get()
                .url("http://jxgl.hdu.edu.cn/xscjcx_dq.aspx?xh=" + username)
                .addHeader("Referer", "http://jxgl.hdu.edu.cn/xscjcx_dq.aspx?xh=" + username)
                .build();
        Response resGetScore = okHttpClient.newCall(reqGetScore).execute();
        String dataScore = resGetScore.body().string();
        resGetScore.close();
        Document docScore = Jsoup.parse(dataScore);
        Elements trs = docScore.select("#DataGrid1 tr");
//        System.out.println(trs.size());
        if (trs.size() > 0) {
            trs.remove(0);  // 删去表格的第一行表头
        }
        List<Score> scoreList = new ArrayList<Score>();
        for (Element tr : trs) {
            Elements tds = tr.select("td");
            Score score = new Score(tds.get(3).text(), tds.get(7).text(), tds.get(2).text());
            scoreList.add(score);
        }

        return scoreList;
    }
}
