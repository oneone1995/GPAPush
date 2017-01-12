package cn.zheteng123.hdu.util;

import cn.zheteng123.hdu.pojo.Score;

import java.util.List;

/**
 * Created by wangl on 2017/1/12.
 */
public class MyStringUtil {
    public static List<Score> getNewScore(List<Score> newScoreList, String subjectId) {
        for (int i = newScoreList.size() - 1; i >= 0; i--) {
            if (subjectId.contains(newScoreList.get(i).getSubjectId())) {
                newScoreList.remove(i);
            }
        }
        return newScoreList;
    }

    public static String getNewSubjectId(List<Score> scores) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Score score : scores) {
            stringBuilder.append(score.getSubjectId());
            stringBuilder.append(",");
        }
        return stringBuilder.toString();
    }
}
