package book.controller;

import book.pojo.Book;
import book.pojo.Cart;
import book.pojo.CartItem;
import book.pojo.User;
import book.service.CartItemService;
import com.google.gson.Gson;

import javax.servlet.http.HttpSession;

public class CartController {
    private CartItemService cartItemService;

    public String index(HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        Cart cart = cartItemService.getCart(user);
        user.setCart(cart);
        session.setAttribute("currentUser", user);

        return "cart/cart";
    }

    public String addCart(Integer bookId, HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        CartItem cartItem = new CartItem(new Book(bookId), 1, user);

        //將指定的圖書添加到當前用戶的購物車中
        cartItemService.addOrUpdateCartItem(cartItem, user.getCart());

        return "redirect:cart.do";
    }

    public String editCart(Integer cartItemId, Integer buyCount) {
        cartItemService.updateCartItem(new CartItem(cartItemId, buyCount));

        return "";
    }

    public String cartInfo(HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        Cart cart = cartItemService.getCart(user);

        //調用Cart中的三個屬性的get方法，目的是在此處計算這三個屬性的值，否則這三個屬性為null，導致的結果就是下一步的gson轉化時，為null的屬性會被忽略
        cart.getTotalBookCount();
        cart.getTotalCount();
        cart.getTotalMoney();

        Gson gson = new Gson();
        String cartJsonStr = gson.toJson(cart);
        return "json:"+cartJsonStr;
    }
}
