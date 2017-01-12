package cn.zheteng123.hdu.dao;

import cn.zheteng123.hdu.pojo.User;
import cn.zheteng123.hdu.util.JdbcUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2017/1/11.
 */
public class UserDao {

    /**
     * 查询所有用户信息
     * @return 用户信息列表
     */
    public List<User> selectUserAll() {
        List<User> userList = new ArrayList<User>();
        try {
            Connection conn = JdbcUtil.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT id, username, password, tel, subject_id FROM user");
            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                User user = new User();
                user.setId(result.getInt("id"));
                user.setUsername(result.getString("username"));
                user.setPassword(result.getString("password"));
                user.setTel(result.getString("tel"));
                user.setSubjectId(result.getString("subject_id"));
                userList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userList;
    }

    /**
     * 更新指定用户的 num_of_score
     * @param id 用户id
     * @param numOfScore 上一次查询时的成绩数
     * @return 更新记录的条数
     */
    public int updateNumOfScoreById(int id, int numOfScore) {
        try {
            Connection conn = JdbcUtil.getConnection();
            PreparedStatement stmt = conn.prepareStatement("UPDATE user SET num_of_score = ? WHERE id=?");
            stmt.setInt(1, id);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int updateSubjectIdById(int id, String subjectId) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = JdbcUtil.getConnection();
            statement = connection.prepareStatement("UPDATE user SET subject_id = ? WHERE id=?");
            statement.setString(1, subjectId);
            statement.setInt(2, id);
            return statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(null, statement, connection);
        }
        return -1;
    }

}
