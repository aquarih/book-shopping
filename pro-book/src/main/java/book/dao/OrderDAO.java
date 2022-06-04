package book.dao;

import book.pojo.Order;
import book.pojo.User;

import java.util.List;

public interface OrderDAO {
    //添加訂單
    void addOrderBean(Order order);
    //獲取指定用戶的訂單列表
    List<Order> getOrderList(User user);

    Integer getOrderTotalBookCount(Order order);
}
