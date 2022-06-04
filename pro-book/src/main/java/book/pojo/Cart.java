package book.pojo;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

public class Cart {
    private Map<Integer, CartItem> cartItemMap;  //購物車中購物車項的集合，這個Map集合中的Key是Book的id
    private Double totalMoney;                   //購物車的總金額
    private Integer totalCount;                  //購物車中購物項的數量
    private Integer totalBookCount;              //購物車中書本的總數量，而不是購物車項的總數量

    public Cart() {
    }

    public Map<Integer, CartItem> getCartItemMap() {
        return cartItemMap;
    }

    public void setCartItemMap(Map<Integer, CartItem> cartItemMap) {
        this.cartItemMap = cartItemMap;
    }

    public Double getTotalMoney() {
        totalMoney = 0.0;
        if(cartItemMap != null && cartItemMap.size() > 0) {
            Set<Map.Entry<Integer, CartItem>> entries = cartItemMap.entrySet();
            for(Map.Entry<Integer, CartItem> cartItemEntry : entries) {
                CartItem cartItem = cartItemEntry.getValue();
                totalMoney = totalMoney + cartItem.getSubTotal();
            }
        }
        return totalMoney;
    }

    public Integer getTotalCount() {
        if(cartItemMap != null && cartItemMap.size() > 0) {
            totalCount = cartItemMap.size();
        }
        return totalCount;
    }

    public Integer getTotalBookCount() {
        totalBookCount = 0;
        if(cartItemMap != null && cartItemMap.size() > 0) {
            Set<Map.Entry<Integer, CartItem>> entries = cartItemMap.entrySet();
            for(Map.Entry<Integer, CartItem> cartItemEntry : entries) {
                CartItem cartItem = cartItemEntry.getValue();
                totalBookCount = totalBookCount + cartItem.getBuyCount();
            }
        }
        return totalBookCount;
    }
}
