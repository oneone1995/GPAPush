package cn.zheteng123.hdu.network;

import cn.zheteng123.hdu.pojo.Score;
import cn.zheteng123.hdu.util.MyCookieJar;
import com.zhy.http.okhttp.OkHttpUtils;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created on 2017/1/11.
 */
public class Network {

    /**
     * 根据用户名密码查询成绩信息
     * @param username 用户名（学号）
     * @param password 密码（经过 MD5 加密）
     * @return 成绩列表
     */
    public static List<Score> getScore(String username, String password) throws IOException {

        // 实例化 client，注意实现 cookie 自动管理
        OkHttpClient client = new OkHttpClient
                .Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .cookieJar(new MyCookieJar())
                .build();
        OkHttpUtils.initClient(client);


        // 登录教务系统
        String dataPostLogin;

        // 清除 cookie
        Response resGetLogout = OkHttpUtils.get()
                .url("http://jxgl.hdu.edu.cn/logout0.aspx")
                .addHeader("Referer", "http://jxgl.hdu.edu.cn/xs_main.aspx?xh=" + username)
                .build()
                .execute();
        resGetLogout.close();

        Response resGetLogout2 = OkHttpUtils.get()
                .url("http://i.hdu.edu.cn/dcp/logout0.jsp")
                .addHeader("Referer", "http://jxgl.hdu.edu.cn/logout0.aspx")
                .build()
                .execute();
        resGetLogout2.close();



        // Get 访问登录页，获取隐藏表单项 lt
        Response resGetLt = OkHttpUtils.get()
                .url("http://jxgl.hdu.edu.cn/")
                .build()
                .execute();
        String dataLt = resGetLt.body().string();
        Document docLt = Jsoup.parse(dataLt);
        String lt = docLt.select("input[name=lt]").val();
        System.out.println(lt);
        resGetLt.close();


        // Post 登录教务系统
        Response resPostLogin = OkHttpUtils.post()
                .url("http://cas.hdu.edu.cn/cas/login")
                .addParams("username", username)
                .addParams("password", password)
                .addParams("lt", lt)
                .build()
                .execute();
        dataPostLogin = resPostLogin.body().string();
        if (dataPostLogin.contains("账号或密码错误")) {
            System.out.println("账号或密码错误！");
            return null;
        }
        resPostLogin.close();


        // 进入选课系统
        Response resGetEduAdmin = OkHttpUtils.get()
                .url("http://i.hdu.edu.cn/dcp/forward.action?path=dcp/apps/sso/jsp/ssoDcpSelf&appid=1142")
                .build()
                .execute();
        String dataTemp = resGetEduAdmin.body().string();
        resGetEduAdmin.close();
//        System.out.println(dataTemp);
        while (dataTemp.contains("认证转向")) {
            Document docUrlJump = Jsoup.parse(dataTemp);
            String urlJump = docUrlJump.select("a").get(0).attr("href");
            Response resTemp = OkHttpUtils.get()
                    .url(urlJump)
                    .build()
                    .execute();
            dataTemp = resTemp.body().string();
            resTemp.close();
        }
//        System.out.println(dataTemp);


        // 获取成绩
        System.out.println("获取成绩");
        Response resGetScore = OkHttpUtils.get()
                .url("http://jxgl.hdu.edu.cn/xscjcx_dq.aspx?xh=" + username)
                .addHeader("Referer", "http://jxgl.hdu.edu.cn/xscjcx_dq.aspx?xh=" + username)
                .build()
                .execute();
        String dataScore = resGetScore.body().string();
        resGetScore.close();
        Document docScore = Jsoup.parse(dataScore);
        Elements trs = docScore.select("#DataGrid1 tr");
//        System.out.println(trs.size());
        trs.remove(0);  // 删去表格的第一行表头
        List<Score> scoreList = new ArrayList<Score>();
        for (Element tr : trs) {
            Elements tds = tr.select("td");
            Score score = new Score(tds.get(3).text(), tds.get(7).text(), tds.get(2).text());
            scoreList.add(score);
        }

        return scoreList;
    }
}
