package org.yearup.models;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Order
{
    private int orderId;
    private int userId;
    private BigDecimal total;
    private LocalDateTime createdAt;

    private List<OrderLineItem> lineItems = new ArrayList<>();

    public Order() {
    }

    public Order(int orderId, int userId, BigDecimal total, LocalDateTime createdAt) {
        this.orderId = orderId;
        this.userId = userId;
        this.total = total;
        this.createdAt = createdAt;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<OrderLineItem> getLineItems() {
        return lineItems;
    }

    public void setLineItems(List<OrderLineItem> lineItems) {
        this.lineItems = lineItems;
    }

    public void addLineItem(OrderLineItem item) {
        this.lineItems.add(item);
    }
}



