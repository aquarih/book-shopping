package book.service.impl;

import book.dao.CartItemDAO;
import book.pojo.Book;
import book.pojo.Cart;
import book.pojo.CartItem;
import book.pojo.User;
import book.service.BookService;
import book.service.CartItemService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartItemServiceImpl implements CartItemService {
    private CartItemDAO cartItemDAO;
    private BookService bookService;

    @Override
    public void addCartItem(CartItem cartItem) {
        cartItemDAO.addCartItem(cartItem);
    }

    @Override
    public void updateCartItem(CartItem cartItem) {
        cartItemDAO.updateCartItem(cartItem);
    }

    @Override
    public void addOrUpdateCartItem(CartItem cartItem, Cart cart) {
        //1. 如果當前用戶的購物車中已經存在這個圖書，那麼購物車中的這本圖書的數量+1
        //2. 否則，在我的購物中新增一個這本圖書的CartItem，數量是1
        //判斷當前用戶的購物車中是否有這本書的CartItem，有 -> update，沒有 -> add
        if(cart != null) {
            Map<Integer, CartItem> cartItemMap = cart.getCartItemMap();
            if(cartItemMap == null) {
                cartItemMap = new HashMap<>();
            }

            if(cartItemMap.containsKey(cartItem.getBook().getId())) {
                CartItem cartItemTemp = cartItemMap.get(cartItem.getBook().getId());
                cartItemTemp.setBuyCount(cartItemTemp.getBuyCount()+1);
                updateCartItem(cartItemTemp);
            } else {
                addCartItem(cartItem);
            }
        } else {
            addCartItem(cartItem);
        }
    }

    @Override
    public List<CartItem> getCartItemList(User user) {
        List<CartItem> cartItemList = cartItemDAO.getCartItemList(user);
        for(CartItem cartItem : cartItemList) {
            Book book = bookService.getBook(cartItem.getBook().getId());
            cartItem.setBook(book);
            //此處需要調用getSubTotal(),目的是執行其內部代碼，從而計算出SubTotal的值
            cartItem.getSubTotal();
        }

        return cartItemList;
    }

    @Override
    public Cart getCart(User user) {
        List<CartItem> cartItemList = getCartItemList(user);
        Map<Integer,CartItem> cartItemMap = new HashMap<>();
        for(CartItem cartItem : cartItemList) {
            cartItemMap.put(cartItem.getBook().getId(), cartItem);
        }
        Cart cart = new Cart();
        cart.setCartItemMap(cartItemMap);
        return cart;
    }
}
