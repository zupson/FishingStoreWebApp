package hr.algebra.fishingstore.controller.mvc;

import hr.algebra.fishingstore.dal.dto.CartProductDto;
import hr.algebra.fishingstore.dal.services.CartProductService;
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
    static final String BASE_URL = "/mvc/cart-products";
    private static final String REDIRECT_LIST = "redirect:" + BASE_URL;
    private static final String VIEW_PREFIX = "cart-products/";
    private static final String LIST_VIEW = VIEW_PREFIX + "list";
    private static final String DETAILS_VIEW = VIEW_PREFIX + "details";
    private static final String FORM_CREATE = VIEW_PREFIX + "form-create";
    private static final String MODEL_KEY = "cartProducts";

    private final CartProductService cartProductService;

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute(MODEL_KEY, cartProductService.getAll());
        return LIST_VIEW;
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable Long id, Model model) {
        model.addAttribute(MODEL_KEY, cartProductService.getById(id));
        return DETAILS_VIEW;
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute(MODEL_KEY, new CartProductDto.CreateDto());
        return FORM_CREATE;
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute(MODEL_KEY) CartProductDto.CreateDto createDto,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return FORM_CREATE;
        }
        cartProductService.create(createDto);
        return REDIRECT_LIST;
    }

    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        cartProductService.delete(id);
        return REDIRECT_LIST;
    }
}