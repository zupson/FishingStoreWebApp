package hr.algebra.fishingstore.session;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Component
@SessionScope
public class CartSession implements Serializable {

    private final Map<Long, Integer> cartItems = new HashMap<>();

    public void addCartItem(Long productId, Integer quantity) {
        cartItems.merge(productId, quantity, Integer::sum);
    }

    public void removeCartItem(Long productId) {
        cartItems.remove(productId);
    }

    public Map<Long, Integer> getCartItems() {
        return cartItems;
    }

    public void updateItem(Long productId, Integer quantity) {
        cartItems.put(productId, quantity);
    }

    public void clear() {
        cartItems.clear();
    }

    public boolean isEmpty() {
        return cartItems.isEmpty();
    }
}