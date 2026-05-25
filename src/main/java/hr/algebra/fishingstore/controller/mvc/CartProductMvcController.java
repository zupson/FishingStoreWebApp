package hr.algebra.fishingstore.controller.mvc;

import hr.algebra.fishingstore.dal.dto.CartProductDto;
import hr.algebra.fishingstore.dal.services.CartProductService;
import hr.algebra.fishingstore.utilities.PathConst;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(CartProductMvcController.BASE_URL)
@RequiredArgsConstructor
public class CartProductMvcController {
    static final String BASE_URL = PathConst.MVC + PathConst.CART_PRODUCTS;

    private static final String REDIRECT_LIST = PathConst.REDIRECT_KEYWORD + BASE_URL;
    private static final String LIST_VIEW = PathConst.CART_PRODUCTS + PathConst.LIST;
    private static final String DETAILS_VIEW = PathConst.CART_PRODUCTS + PathConst.DETAILS;
    private static final String FORM_CREATE_VIEW = PathConst.CART_PRODUCTS + PathConst.FORM_CREATE;
    private static final String MODEL_KEY = "cartProducts";

    private final CartProductService cartProductService;

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute(MODEL_KEY, cartProductService.getAll());
        return LIST_VIEW;
    }

    @GetMapping(PathConst.ID)
    public String getById(@PathVariable Long id, Model model) {
        model.addAttribute(MODEL_KEY, cartProductService.getById(id));
        return DETAILS_VIEW;
    }

    @GetMapping(PathConst.NEW)
    public String createForm(Model model) {
        model.addAttribute(MODEL_KEY, new CartProductDto.CreateDto());
        return FORM_CREATE_VIEW;
    }

    @PostMapping(PathConst.CREATE)
    public String create(@Valid @ModelAttribute(MODEL_KEY) CartProductDto.CreateDto createDto,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return FORM_CREATE_VIEW;
        }
        cartProductService.create(createDto);
        return REDIRECT_LIST;
    }

    @DeleteMapping(PathConst.DELETE + PathConst.ID)
    public String delete(@PathVariable Long id) {
        cartProductService.delete(id);
        return REDIRECT_LIST;
    }
}