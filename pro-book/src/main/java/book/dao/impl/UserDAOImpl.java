package book.dao.impl;

import book.dao.UserDAO;
import book.pojo.User;
import myssm.basedao.BaseDAO;

public class UserDAOImpl extends BaseDAO<User> implements UserDAO {
    @Override
    public User getUser(String uname, String pwd) {
        String sql = "select * from t_user where uname like ? and pwd like ?";
        return load(sql, uname, pwd);
    }

    @Override
    public void addUser(User user) {
        String sql = "insert into t_user values (0,?,?,?,0)";
        executeUpdate(sql, user.getUname(), user.getPwd(), user.getEmail());
    }
}
