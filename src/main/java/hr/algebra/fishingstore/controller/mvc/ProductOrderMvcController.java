package hr.algebra.fishingstore.controller.mvc;

import hr.algebra.fishingstore.dal.dto.ProductDto;
import hr.algebra.fishingstore.dal.dto.ProductOrderDto;
import hr.algebra.fishingstore.dal.services.ProductOrderService;
import hr.algebra.fishingstore.utilities.PathConst;
import hr.algebra.fishingstore.utilities.ViewPathConst;
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
    static final String BASE_URL = PathConst.MVC + PathConst.PRODUCT_ORDERS;
    private static final String REDIRECT_LIST = PathConst.REDIRECT_KEYWORD + BASE_URL;

    private static final String MODEL_KEY = "productOrders";

    private final ProductOrderService productOrderService;

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute(MODEL_KEY, productOrderService.getAll());
        return ViewPathConst.PRODUCT_ORDERS_LIST_VIEW;
    }

    @GetMapping(PathConst.ID)
    public String getById(@PathVariable Long id, Model model) {
        model.addAttribute(MODEL_KEY, productOrderService.getById(id));
        return ViewPathConst.PRODUCT_ORDERS_DETAILS_VIEW;
    }

    @GetMapping(PathConst.ORDERS + PathConst.ID)
    public String getByOrderId(@PathVariable Long id, Model model) {
        model.addAttribute(MODEL_KEY, productOrderService.getByOrderId(id));
        return ViewPathConst.PRODUCT_ORDERS_LIST_VIEW;
    }

    @GetMapping(PathConst.NEW)
    public String createForm(Model model) {
        model.addAttribute(MODEL_KEY, new ProductDto.CreateDto());
        return ViewPathConst.PRODUCT_ORDERS_FORM_CREATE_VIEW;
    }

    @PostMapping(PathConst.CREATE)
    public String create(@Valid @ModelAttribute(MODEL_KEY) ProductOrderDto.CreateDto createDto,
                         BindingResult bindingResult) {

        if (bindingResult.hasErrors())
            return ViewPathConst.PRODUCT_ORDERS_FORM_CREATE_VIEW;

        productOrderService.create(createDto);
        return REDIRECT_LIST;
    }

    @GetMapping(PathConst.EDIT + PathConst.ID)
    public String editForm(@PathVariable Long id, Model model) {
        ProductOrderDto.ResponseDto productOrder = productOrderService.getById(id);

        ProductOrderDto.EditDto editDto = new ProductOrderDto.EditDto();
        editDto.setQuantity(productOrder.getQuantity());

        model.addAttribute(MODEL_KEY, editDto);
        return ViewPathConst.PRODUCT_ORDERS_FORM_UPDATE_VIEW;
    }

    @PostMapping(PathConst.UPDATE + PathConst.ID)
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute(MODEL_KEY) ProductOrderDto.EditDto editDto,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return ViewPathConst.PRODUCT_ORDERS_FORM_UPDATE_VIEW;

        productOrderService.update(id, editDto);
        return REDIRECT_LIST;
    }
}