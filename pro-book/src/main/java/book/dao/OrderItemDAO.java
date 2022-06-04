package book.dao;

import book.pojo.OrderItem;

public interface OrderItemDAO {
    //添加訂單項
    void addOrderItem(OrderItem orderItem);
}
