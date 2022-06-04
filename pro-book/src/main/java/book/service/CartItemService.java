package book.service;

import book.pojo.Cart;
import book.pojo.CartItem;
import book.pojo.User;

import java.util.List;

public interface CartItemService {
    void addCartItem(CartItem cartItem);
    void updateCartItem(CartItem cartItem);
    void addOrUpdateCartItem(CartItem cartItem, Cart cart);
    //獲取指定用戶的所有購物車項列表(需要注意的是，這個方法內部查詢的時候，會將book詳細訊息設置進去)
    List<CartItem> getCartItemList(User user);
    //加載特定用戶的購物車訊息
    Cart getCart(User user);
}
