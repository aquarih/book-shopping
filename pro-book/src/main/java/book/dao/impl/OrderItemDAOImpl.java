package book.dao.impl;

import book.dao.OrderItemDAO;
import book.pojo.OrderItem;
import myssm.basedao.BaseDAO;

public class OrderItemDAOImpl extends BaseDAO<OrderItem> implements OrderItemDAO {
    @Override
    public void addOrderItem(OrderItem orderItem) {
        String sql = "insert into t_order_item values (0,?,?,?)";
        executeUpdate(sql, orderItem.getBook().getId(), orderItem.getBuyCount(), orderItem.getOrderBean().getId());
    }
}
