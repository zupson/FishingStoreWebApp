package hr.algebra.fishingstore.controller.mvc;

import hr.algebra.fishingstore.dal.services.CartService;
import hr.algebra.fishingstore.utilities.PathConst;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(CartMvcController.BASE_URL)
@RequiredArgsConstructor
public class CartMvcController {
    static final String BASE_URL = PathConst.MVC + PathConst.CARTS;

    private static final String DETAILS_VIEW = PathConst.CARTS + PathConst.DETAILS;
    private static final String MODEL_KEY = "carts";

    private final CartService cartService;

    @GetMapping(PathConst.ID)
    public String getById(@PathVariable Long id, Model model) {
        model.addAttribute(MODEL_KEY, cartService.getByUserId(id));
        return DETAILS_VIEW;
    }
}