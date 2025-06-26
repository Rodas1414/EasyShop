package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.OrderDao;
import org.yearup.models.Order;
import org.yearup.models.OrderLineItem;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;

@Component
public class MySqlOrderDao extends MySqlDaoBase implements OrderDao {

    public MySqlOrderDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public Order create(Order order) {
        String sql = "INSERT INTO orders (user_id, total, created_at) VALUES (?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, order.getUserId());
            ps.setBigDecimal(2, order.getTotal());
            ps.setTimestamp(3, Timestamp.valueOf(order.getCreatedAt()));

            ps.executeUpdate();

            // Get the auto-generated order ID
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                int orderId = keys.getInt(1);
                order.setOrderId(orderId);
                return order;
            } else {
                throw new RuntimeException("Order ID generation failed.");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to create order.", e);
        }
    }

    @Override
    public void addLineItem(OrderLineItem item) {
        String sql = "INSERT INTO order_line_items (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, item.getOrderId());
            ps.setInt(2, item.getProductId());
            ps.setInt(3, item.getQuantity());
            ps.setBigDecimal(4, item.getPrice());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to add order line item.", e);
        }
    }
}
