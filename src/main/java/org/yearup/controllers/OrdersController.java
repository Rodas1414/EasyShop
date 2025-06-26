package org.yearup.controllers;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.yearup.data.OrderDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.data.UserDao;
import org.yearup.models.Order;
import org.yearup.models.OrderLineItem;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/orders")
@CrossOrigin
@PreAuthorize("isAuthenticated()")
public class OrdersController {

    private final UserDao userDao;
    private final ShoppingCartDao cartDao;
    private final OrderDao orderDao;

    @Autowired
    public OrdersController(UserDao userDao, ShoppingCartDao cartDao, OrderDao orderDao) {
        this.userDao = userDao;
        this.cartDao = cartDao;
        this.orderDao = orderDao;
    }

    @PostMapping
    public void checkout(Principal principal) {
        // Get the currently logged in user's ID
        String username = principal.getName();
        int userId = userDao.getIdByUsername(username);

        // Get the shopping cart for the user
        ShoppingCart cart = cartDao.getByUserId(userId);

        // Calculate total
        BigDecimal total = cart.getTotal();

        // Create a new order
        Order order = new Order();
        order.setUserId(userId);
        order.setTotal(total);
        order.setCreatedAt(LocalDateTime.now());

        // Save the order and get back the generated order ID
        Order savedOrder = orderDao.create(order);

        // Add each item as an order line item
        for (ShoppingCartItem item : cart.getItems().values()) {
            OrderLineItem lineItem = new OrderLineItem();
            lineItem.setOrderId(savedOrder.getOrderId());
            lineItem.setProductId(item.getProductId());
            lineItem.setQuantity(item.getQuantity());
            lineItem.setPrice(item.getProduct().getPrice());

            orderDao.addLineItem(lineItem);
        }

        // Clear the shopping cart after placing the order
        cartDao.clear(userId);
    }
}
