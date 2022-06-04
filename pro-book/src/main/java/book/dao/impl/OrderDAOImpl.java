package book.dao.impl;

import book.dao.OrderDAO;
import book.pojo.Order;
import book.pojo.User;
import myssm.basedao.BaseDAO;

import java.math.BigDecimal;
import java.util.List;

public class OrderDAOImpl extends BaseDAO<Order> implements OrderDAO {
    @Override
    public void addOrderBean(Order order) {
        String sql = "insert into t_order values (0,?,?,?,?,?)";
        int orderBeanId = executeUpdate(sql, order.getOrderNo(), order.getOrderDate(), order.getOrderUser().getId(), order.getOrderMoney(), order.getOrderStatus());
        order.setId(orderBeanId);
    }

    @Override
    public List<Order> getOrderList(User user) {
        String sql = "select * from t_order where orderUser = ?";
        return executeQuery(sql, user.getId());
    }

    @Override
    public Integer getOrderTotalBookCount(Order order) {
        String sql = "select sum(buyCount) as totalBookCount, orderBean from t_order_item where orderBean = ? group by orderBean";
        return ((BigDecimal)executeComplexQuery(sql, order.getId())[0]).intValue();
    }
}
