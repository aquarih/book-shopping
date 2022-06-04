package book.dao;

import book.pojo.CartItem;
import book.pojo.User;

import java.util.List;

public interface CartItemDAO {
    //新增購物車項
    void addCartItem(CartItem cartItem);
    //修改特定的購物車項
    void updateCartItem(CartItem cartItem);
    //獲取特定用戶的購物車項
    List<CartItem> getCartItemList(User user);
    //刪除指定的購物車項
    void deleteCartItem(CartItem cartItem);
}
