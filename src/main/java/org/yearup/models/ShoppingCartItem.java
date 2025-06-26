package org.yearup.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class ShoppingCartItem
{
    private Product product;
    private int quantity = 1;
    private BigDecimal discountPercent = BigDecimal.ZERO;

    public Product getProduct()
    {
        return product;
    }

    public void setProduct(Product product)
    {
        this.product = product;
    }

    public int getQuantity()
    {
        return quantity;
    }

    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }

    public BigDecimal getDiscountPercent()
    {
        return discountPercent;
    }

    public void setDiscountPercent(BigDecimal discountPercent)
    {
        this.discountPercent = discountPercent;
    }

    @JsonIgnore
    public int getProductId()
    {
        return product != null ? product.getProductId() : -1;  // safer
    }

    public BigDecimal getLineTotal()
    {
        if (product == null || product.getPrice() == null)
            return BigDecimal.ZERO;

        BigDecimal basePrice = product.getPrice();
        BigDecimal subTotal = basePrice.multiply(BigDecimal.valueOf(quantity));
        BigDecimal discountAmount = subTotal.multiply(discountPercent);

        return subTotal.subtract(discountAmount).setScale(2, RoundingMode.HALF_UP);
    }
}
