package cn.zheteng123.hdu.pojo;

/**
 * Created on 2017/1/11.
 */
public class User {

    private int id;
    private String username;
    private String password;
    private int numOfScore;
    private String tel;
    private String subjectId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getNumOfScore() {
        return numOfScore;
    }

    public void setNumOfScore(int numOfScore) {
        this.numOfScore = numOfScore;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }
}
