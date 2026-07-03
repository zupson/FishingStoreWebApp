package hr.algebra.fishingstore.controller.mvc;

import hr.algebra.fishingstore.dal.dto.ProductDto;
import hr.algebra.fishingstore.dal.services.ProductService;
import hr.algebra.fishingstore.session.CartSession;
import hr.algebra.fishingstore.utilities.PathConst;
import hr.algebra.fishingstore.utilities.ViewPathConst;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
@RequestMapping(CartProductMvcController.BASE_URL)
@RequiredArgsConstructor
public class CartProductMvcController {
    static final String BASE_URL = PathConst.MVC + PathConst.CART_PRODUCTS;
    private static final String REDIRECT_LIST = PathConst.REDIRECT_KEYWORD + BASE_URL;

    private static final String CART_ITEMS = "cartItems";
    private static final String DEFAULT_QUANTITY = "1";
    private static final String TOTAL = "total";

    private final CartSession cartSession;
    private final ProductService productService;

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute(CART_ITEMS, getCartProducts());
        model.addAttribute(TOTAL, getTotalPrice());
        return ViewPathConst.CART_PRODUCTS_LIST_VIEW;
    }

    private BigDecimal getTotalPrice() {
        Map<ProductDto.ResponseDto, Integer> cartProducts = getCartProducts();

        return cartProducts.entrySet().stream()
                .map(e ->
                        e.getKey()
                        .getPrice()
                        .multiply(BigDecimal.valueOf(e.getValue())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Map<ProductDto.ResponseDto, Integer> getCartProducts() {
        Map<ProductDto.ResponseDto, Integer> cartProducts = new LinkedHashMap<>();

        cartSession.getCartItems().forEach(
                (productId, quantity) -> cartProducts.put(productService.getById(productId), quantity));
        return cartProducts;
    }

    @GetMapping(PathConst.NEW)
    public String create(@RequestParam Long productId,
                         @RequestParam(defaultValue = DEFAULT_QUANTITY) Integer quantity) {
        cartSession.addCartItem(productId, quantity);
        return REDIRECT_LIST;
    }

    @PostMapping(PathConst.UPDATE + PathConst.ID)
    public String update(@PathVariable Long id, @RequestParam Integer quantity) {
        cartSession.updateItem(id, quantity);
        return REDIRECT_LIST;
    }

    @PostMapping(PathConst.DELETE + PathConst.ID)
    public String delete(@PathVariable Long id) {
        cartSession.removeCartItem(id);
        return REDIRECT_LIST;
    }
}