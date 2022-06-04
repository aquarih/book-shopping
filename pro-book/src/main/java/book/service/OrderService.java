package book.service;

import book.pojo.Order;
import book.pojo.User;

import java.util.List;

public interface OrderService {
    void addOrder(Order order);

    List<Order> getOrderList(User user);
}
