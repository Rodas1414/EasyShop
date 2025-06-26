package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.math.BigDecimal;

@Component
public class MySqlShoppingCartDao extends MySqlDaoBase implements ShoppingCartDao
{
    public MySqlShoppingCartDao(DataSource dataSource)
    {
        super(dataSource);
    }

    @Override
    public ShoppingCart getByUserId(int userId)
    {
        ShoppingCart cart = new ShoppingCart();

        String query = """
            SELECT * 
            FROM shopping_cart AS sc
            JOIN products AS p ON p.product_id = sc.product_id
            WHERE user_id = ?;
        """;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query))
        {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next())
            {
                Product product = new Product(
                        rs.getInt("product_id"),
                        rs.getString("name"),
                        rs.getBigDecimal("price"),
                        rs.getInt("category_id"),
                        rs.getString("description"),
                        rs.getString("color"),
                        rs.getInt("stock"),
                        rs.getBoolean("featured"),
                        rs.getString("image_url")
                );

                int quantity = rs.getInt("quantity");

                ShoppingCartItem item = new ShoppingCartItem();
                item.setProduct(product);
                item.setQuantity(quantity);
                item.setDiscountPercent(BigDecimal.ZERO); // Optional

                cart.add(item);
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Failed to load shopping cart", e);
        }

        return cart;
    }

    @Override
    public Product addOrIncrement(int userId, int productId)
    {
        String sql = """
            INSERT INTO shopping_cart (user_id, product_id, quantity)
            VALUES (?, ?, 1)
            ON DUPLICATE KEY UPDATE quantity = quantity + 1
        """;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql))
        {
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            ps.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Failed to add/increment product in cart", e);
        }
        return null;
    }

    @Override
    public void updateQuantity(int userId, int productId, int quantity)
    {
        // If quantity is 0 or less, remove the item from the cart
        if (quantity <= 0)
        {
            removeItem(userId, productId);
            return;
        }

        String sql = """
            UPDATE shopping_cart
            SET quantity = ?
            WHERE user_id = ? AND product_id = ?
        """;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql))
        {
            ps.setInt(1, quantity);
            ps.setInt(2, userId);
            ps.setInt(3, productId);
            ps.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Failed to update quantity", e);
        }
    }

    @Override
    public void clear(int userId)
    {
        String sql = "DELETE FROM shopping_cart WHERE user_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql))
        {
            ps.setInt(1, userId);
            ps.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Failed to clear shopping cart", e);
        }
    }

    @Override
    public void removeItem(int userId, int productId)
    {
        String sql = "DELETE FROM shopping_cart WHERE user_id = ? AND product_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql))
        {
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            ps.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Failed to remove item from cart", e);
        }
    }
}
