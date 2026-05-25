package hr.algebra.fishingstore.controller.mvc;

import hr.algebra.fishingstore.dal.dto.OrderDto;
import hr.algebra.fishingstore.dal.services.OrderService;
import hr.algebra.fishingstore.utilities.PathConst;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(OrderMvcController.BASE_URL)
@RequiredArgsConstructor
public class OrderMvcController {
    static final String BASE_URL = PathConst.MVC + PathConst.ORDERS;
    private static final String REDIRECT_LIST = PathConst.REDIRECT_KEYWORD + BASE_URL;
    private static final String LIST_VIEW = PathConst.ORDERS + PathConst.LIST;
    private static final String DETAILS_VIEW = PathConst.ORDERS + PathConst.DETAILS;
    private static final String FORM_CREATE_VIEW = PathConst.ORDERS + PathConst.FORM_CREATE;
    private static final String FORM_UPDATE_VIEW = PathConst.ORDERS + PathConst.FORM_UPDATE;
    private static final String MODEL_KEY = "orders";

    private final OrderService orderService;

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute(MODEL_KEY, orderService.getAll());
        return LIST_VIEW;
    }

    @GetMapping(PathConst.ID)
    public String getById(@PathVariable Long id, Model model) {
        model.addAttribute(MODEL_KEY, orderService.getById(id));
        return DETAILS_VIEW;
    }

    @GetMapping(PathConst.NEW)
    public String createForm(Model model) {
        model.addAttribute(MODEL_KEY, new OrderDto.CreateDto());
        return FORM_CREATE_VIEW;
    }

    @PostMapping(PathConst.CREATE)
    public String create(@Valid @ModelAttribute(MODEL_KEY) OrderDto.CreateDto createDto,
                         BindingResult bindingResult) {

        if (bindingResult.hasErrors())
            return FORM_CREATE_VIEW;

        orderService.create(createDto);
        return REDIRECT_LIST;
    }

    @GetMapping(PathConst.EDIT + PathConst.ID)
    public String editForm(@PathVariable Long id, Model model) {
        OrderDto.ResponseDto order = orderService.getById(id);

        OrderDto.EditDto editDto = new OrderDto.EditDto();
        editDto.setOrderStatus(order.getOrderStatus());

        model.addAttribute(MODEL_KEY, editDto);
        model.addAttribute("orderId", id);
        return FORM_UPDATE_VIEW;
    }

    @PostMapping(PathConst.UPDATE + PathConst.ID)
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute(MODEL_KEY) OrderDto.EditDto editDto,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return FORM_UPDATE_VIEW;
        
        orderService.update(id, editDto);
        return REDIRECT_LIST;
    }

    @PostMapping(PathConst.DELETE + PathConst.ID)
    public String delete(@PathVariable Long id) {
        orderService.delete(id);
        return REDIRECT_LIST;
    }
}