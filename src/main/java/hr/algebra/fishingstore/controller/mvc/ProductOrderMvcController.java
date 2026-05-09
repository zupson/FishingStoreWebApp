package hr.algebra.fishingstore.controller.mvc;

import hr.algebra.fishingstore.dal.dto.ProductDto;
import hr.algebra.fishingstore.dal.dto.ProductOrderDto;
import hr.algebra.fishingstore.dal.services.ProductOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(ProductOrderMvcController.BASE_URL)
@RequiredArgsConstructor
public class ProductOrderMvcController {
    static final String BASE_URL = "/mvc/product-orders";

    private static final String REDIRECT_LIST = "redirect:" + BASE_URL;
    private static final String VIEW_PREFIX = "product-orders/";
    private static final String LIST_VIEW = VIEW_PREFIX + "list";
    private static final String DETAILS_VIEW = VIEW_PREFIX + "details";
    private static final String FORM_CREATE = VIEW_PREFIX + "form-create";
    private static final String FORM_UPDATE = VIEW_PREFIX + "form-update";
    private static final String MODEL_KEY = "product-orders";

    private final ProductOrderService productOrderService;

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute(MODEL_KEY, productOrderService.getAll());
        return LIST_VIEW;
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable Long id, Model model) {
        model.addAttribute(MODEL_KEY, productOrderService.getById(id));
        return DETAILS_VIEW;
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute(MODEL_KEY, new ProductDto.CreateDto());
        return FORM_CREATE;
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute(MODEL_KEY) ProductOrderDto.CreateDto createDto,
                         BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return FORM_CREATE;
        }
        productOrderService.create(createDto);
        return REDIRECT_LIST;
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        ProductOrderDto.ResponseDto productOrder = productOrderService.getById(id);

        ProductOrderDto.EditDto editDto = new ProductOrderDto.EditDto();
        editDto.setQuantity(productOrder.getQuantity());

        model.addAttribute(MODEL_KEY, editDto);
        return FORM_UPDATE;
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute(MODEL_KEY) ProductOrderDto.EditDto editDto,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return FORM_UPDATE;
        }
        productOrderService.update(id, editDto);
        return REDIRECT_LIST;
    }
}