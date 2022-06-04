package book.service.impl;

import book.dao.CartItemDAO;
import book.dao.OrderDAO;
import book.dao.OrderItemDAO;
import book.pojo.CartItem;
import book.pojo.Order;
import book.pojo.OrderItem;
import book.pojo.User;
import book.service.OrderService;

import java.util.List;
import java.util.Map;

public class OrderServiceImpl implements OrderService {
    private OrderDAO orderDAO;
    private OrderItemDAO orderItemDAO;
    private CartItemDAO cartItemDAO;

    @Override
    public void addOrder(Order order) {
        //1. 訂單表添加一條紀錄
        orderDAO.addOrderBean(order);       //執行完這步，order中的id是有值的

        //2. 訂單詳情添加七條記錄
        //order中的orderItemList是空的，此處我們應該根據用戶的購物車中的購物車項去轉換成一個一個的訂單項
//        List<OrderItem> orderItemList = order.getOrderItemList();
//        for(OrderItem orderItem : orderItemList) {
//            orderItemDAO.addOrderItem(orderItem);
//        }
        User currentUser = order.getOrderUser();
        Map<Integer, CartItem> cartItemMap = currentUser.getCart().getCartItemMap();
        for(CartItem cartItem : cartItemMap.values()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setBook(cartItem.getBook());
            orderItem.setBuyCount(cartItem.getBuyCount());
            orderItem.setOrderBean(order);

            orderItemDAO.addOrderItem(orderItem);
        }

        //3. 購物車項表中需要刪除對應的七條記錄
        for(CartItem cartItem : cartItemMap.values()) {
            cartItemDAO.deleteCartItem(cartItem);
        }
    }

    @Override
    public List<Order> getOrderList(User user) {
        List<Order> orderList = orderDAO.getOrderList(user);
        for (Order order : orderList) {
            Integer totalBookCount = orderDAO.getOrderTotalBookCount(order);
            order.setTotalBookCount(totalBookCount);
        }

        return orderList;
    }
}
