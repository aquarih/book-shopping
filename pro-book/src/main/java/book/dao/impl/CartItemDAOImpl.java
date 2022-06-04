package book.dao.impl;

import book.dao.CartItemDAO;
import book.pojo.CartItem;
import book.pojo.User;
import myssm.basedao.BaseDAO;

import java.util.List;

public class CartItemDAOImpl extends BaseDAO<CartItem> implements CartItemDAO {
    @Override
    public void addCartItem(CartItem cartItem) {
        String sql = "insert into t_cart_item values (0,?,?,?)";
        executeUpdate(sql, cartItem.getBook().getId(), cartItem.getBuyCount(), cartItem.getUserBean().getId());
    }

    @Override
    public void updateCartItem(CartItem cartItem) {
        String sql = "update t_cart_item set buyCount = ? where id = ?";
        executeUpdate(sql, cartItem.getBuyCount(), cartItem.getId());
    }

    @Override
    public List<CartItem> getCartItemList(User user) {
        String sql = "select * from t_cart_item where userBean = ?";
        return executeQuery(sql, user.getId());
    }

    @Override
    public void deleteCartItem(CartItem cartItem) {
        String sql = "delete from t_cart_item where id = ?";
        executeUpdate(sql, cartItem.getId());
    }
}
