package cn.zheteng123.hdu.pojo;

/**
 * Created on 2016/6/20.
 */
public class Score {
    private String name;
    private String score;
    private String subjectId;


    public Score(String name, String score) {
        this.name = name;
        this.score = score;
    }

    public Score(String name, String score, String subjectId) {
        this.name = name;
        this.score = score;
        this.subjectId = subjectId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
