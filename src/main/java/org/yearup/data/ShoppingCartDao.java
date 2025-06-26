package org.yearup.data;

import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;

public interface ShoppingCartDao
{
    ShoppingCart getByUserId(int userId);
    Product addOrIncrement(int userId, int productId);
    void updateQuantity(int userId, int productId, int quantity);
    void clear(int userId);
    void removeItem(int userId, int productId); // Add this one if missing
}
