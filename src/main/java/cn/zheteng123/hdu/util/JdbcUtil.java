package cn.zheteng123.hdu.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;  
import java.sql.ResultSet;  
import java.sql.SQLException;  
import java.sql.Statement;
import java.util.Properties;

public class JdbcUtil {

    private static String driverName = "com.mysql.jdbc.Driver";  
  
    public JdbcUtil() {  
        super();  
        // TODO Auto-generated constructor stub  
    }  
  
    static {  
        try {  
            Class.forName(driverName);  
        } catch (ClassNotFoundException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
    }  
  
    public static Connection getConnection() throws SQLException {
        InputStream inStream = SMSUtil.class.getClassLoader().getResourceAsStream("config.properties");

        Properties prop = new Properties();
        try {
            prop.load(inStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return DriverManager.getConnection(prop.getProperty("jdbc_url"), prop.getProperty("jdbc_username"), prop.getProperty("jdbc_pwd"));
    }  
  
    public static void close(ResultSet rs, Statement st, Connection conn) {  
        try {  
            if (rs != null) {  
                rs.close();  
  
            }  
        } catch (SQLException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }finally{  
            try {  
                if(st!=null){  
                    st.close();  
                }  
            } catch (SQLException e) {  
                // TODO Auto-generated catch block  
                e.printStackTrace();  
            }finally{  
                if(conn!=null){  
                    try {  
                        conn.close();  
                    } catch (SQLException e) {  
                        // TODO Auto-generated catch block  
                        e.printStackTrace();  
                    }  
                }  
            }  
        }  
  
    }  
}  