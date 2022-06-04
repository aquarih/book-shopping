package myssm.trans;

import myssm.basedao.ConnUtil;

import java.sql.Connection;
import java.sql.SQLException;

public class TransactionManager {

    //開啟事務
    public static void beginTrans() throws SQLException {
        ConnUtil.getConn().setAutoCommit(false);
    }

    //提交事務
    public static void commit() throws SQLException {
        Connection conn = ConnUtil.getConn();
        conn.commit();
        ConnUtil.closeConn();
    }

    //回滾事務
    public static void rollback() throws SQLException {
        Connection conn = ConnUtil.getConn();
        conn.rollback();
        ConnUtil.closeConn();
    }
}
