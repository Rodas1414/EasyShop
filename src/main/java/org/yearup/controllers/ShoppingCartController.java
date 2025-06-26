package org.yearup.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProductDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.data.UserDao;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.User;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/cart")
@CrossOrigin
public class ShoppingCartController
{
    private final ShoppingCartDao shoppingCartDao;
    private final UserDao userDao;
    private final ProductDao productDao;

    public ShoppingCartController(ShoppingCartDao shoppingCartDao, UserDao userDao, ProductDao productDao)
    {
        this.shoppingCartDao = shoppingCartDao;
        this.userDao = userDao;
        this.productDao = productDao;
    }

    // GET /cart
    @GetMapping
    public ShoppingCart getCart(Principal principal)
    {
        User user = getAuthenticatedUser(principal);
        try {
            return shoppingCartDao.getByUserId(user.getId());
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to get shopping cart.", e);
        }
    }

    // POST /cart/products/{productId}
    @PostMapping("/products/{productId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Product addToCart(@PathVariable int productId, Principal principal)
    {
        User user = getAuthenticatedUser(principal);
        try {
            return shoppingCartDao.addOrIncrement(user.getId(), productId);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to add product to cart.", e);
        }
    }

    // PUT /cart/products/{productId}
    @PutMapping("/products/{productId}")
    public void updateCartItem(@PathVariable int productId,
                               @RequestBody Map<String, Integer> body,
                               Principal principal)
    {
        User user = getAuthenticatedUser(principal);
        try {
            int quantity = body.getOrDefault("quantity", 1);
            shoppingCartDao.updateQuantity(user.getId(), productId, quantity);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to update cart item.", e);
        }
    }

    // DELETE /cart
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clearCart(Principal principal)
    {
        User user = getAuthenticatedUser(principal);
        try {
            shoppingCartDao.clear(user.getId());
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to clear shopping cart.", e);
        }
    }

    // Shared method to get current user
    private User getAuthenticatedUser(Principal principal)
    {
        if (principal == null)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated.");

        String username = principal.getName();
        User user = userDao.getByUserName(username);

        if (user == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found: " + username);

        return user;
    }
}
