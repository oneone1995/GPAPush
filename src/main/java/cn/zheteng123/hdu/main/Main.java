package cn.zheteng123.hdu.main;


import cn.zheteng123.hdu.dao.UserDao;
import cn.zheteng123.hdu.network.Network;
import cn.zheteng123.hdu.pojo.Score;
import cn.zheteng123.hdu.pojo.User;
import cn.zheteng123.hdu.util.MyStringUtil;
import cn.zheteng123.hdu.util.SMSUtil;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;

import java.io.IOException;
import java.util.List;



/**
 * Created on 2016/6/20.
 */
public class Main {

    public static void main(String[] args) {

        while (true) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            UserDao userDao = new UserDao();
            List<User> userList = userDao.selectUserAll();
            List<Score> scoreList = null;
            for (User user : userList) {
                System.out.println("开始查询" + user.getUsername() + "的成绩……");

                try {
                    scoreList = Network.getScore(user.getUsername(), user.getPassword());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (scoreList != null) {
                    List<Score> pushScores = MyStringUtil.getNewScore(scoreList, user.getSubjectId());

                    // 推送成绩
                    String subjectId = user.getSubjectId();
                    for (Score pushScore : pushScores) {
                        boolean result = SMSUtil.sendSMS(pushScore.getName(), pushScore.getScore(), user.getTel());
                        if (!result) {
                            // TODO: 2017/1/12 以后补上
                        } else {
                            subjectId += pushScore.getSubjectId() + ",";
                            userDao.updateSubjectIdById(user.getId(), subjectId);
                        }
                    }
                }

                System.out.println(user.getUsername() + "的成绩推送完毕……");
            }
        }
    }

}
