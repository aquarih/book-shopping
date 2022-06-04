package book.controller;

import book.pojo.Order;
import book.pojo.User;
import book.service.OrderService;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class OrderController {
    private OrderService orderService;

    //結帳
    public String checkout(HttpSession session) {
        Order order = new Order();
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        int month = now.getMonthValue();
        int day = now.getDayOfMonth();
        int hour = now.getHour();
        int min = now.getMinute();
        int sec = now.getSecond();
        order.setOrderNo(UUID.randomUUID().toString() + year + month + day + hour + min + sec);
        order.setOrderDate(now);

        User user = (User) session.getAttribute("currentUser");
        order.setOrderUser(user);
        order.setOrderMoney(user.getCart().getTotalMoney());
        order.setOrderStatus(0);

        orderService.addOrder(order);

        return "index";
    }

    public String getOrderList(HttpSession session) {
        User user = (User) session.getAttribute("currentUser");

        List<Order> orderList = orderService.getOrderList(user);
        user.setOrderList(orderList);

        session.setAttribute("currentUser", user);

        return "order/order";
    }
}
