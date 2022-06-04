package myssm.basedao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnUtil {
    private static ThreadLocal<Connection> threadLocal = new ThreadLocal<>();

    private static Connection createConn() {
        try{
            InputStream is = BaseDAO.class.getClassLoader().getResourceAsStream("jdbc.properties");
            Properties pros = new Properties();
            pros.load(is);

            //2.读取配置信息
            String user = pros.getProperty("user");
            String password = pros.getProperty("password");
            String url = pros.getProperty("url");
            String driverClass = pros.getProperty("driverClass");

            Class.forName(driverClass);

            Connection conn = DriverManager.getConnection(url, user, password);
            return conn;
        } catch (ClassNotFoundException | SQLException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Connection getConn(){
        Connection conn = threadLocal.get();
        if(conn == null){
            conn = createConn();
            threadLocal.set(conn);
        }

        return threadLocal.get();
    }

    public static void closeConn() throws SQLException {
        Connection conn = threadLocal.get();
        if(conn == null){
            return;
        }
        if(!conn.isClosed()) {
            conn.close();
            threadLocal.set(null);
        }
    }
}
