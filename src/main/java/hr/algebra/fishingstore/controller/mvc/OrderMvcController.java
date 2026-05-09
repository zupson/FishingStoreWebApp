package hr.algebra.fishingstore.controller.mvc;

import hr.algebra.fishingstore.dal.dto.OrderDto;
import hr.algebra.fishingstore.dal.services.OrderService;
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
    static final String BASE_URL = "/mvc/orders";
    private static final String REDIRECT_LIST = "redirect:" + BASE_URL;
    private static final String VIEW_PREFIX = "orders/";
    private static final String LIST_VIEW = VIEW_PREFIX + "list";
    private static final String DETAILS_VIEW = VIEW_PREFIX + "details";
    private static final String FORM_CREATE = VIEW_PREFIX + "form-create";
    private static final String FORM_UPDATE = VIEW_PREFIX + "form-update";
    private static final String MODEL_KEY = "orders";

    private final OrderService orderService;

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute(MODEL_KEY, orderService.getAll());
        return LIST_VIEW;
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable Long id, Model model) {
        model.addAttribute(MODEL_KEY, orderService.getById(id));
        return DETAILS_VIEW;
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute(MODEL_KEY, new OrderDto.CreateDto());
        return FORM_CREATE;
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute(MODEL_KEY) OrderDto.CreateDto createDto,
                         BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return FORM_CREATE;
        }
        orderService.create(createDto);
        return REDIRECT_LIST;
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        OrderDto.ResponseDto order = orderService.getById(id);

        OrderDto.EditDto editDto = new OrderDto.EditDto();
        editDto.setOrderStatus(order.getOrderStatus());

        model.addAttribute(MODEL_KEY, editDto);
        model.addAttribute("orderId", id);
        return FORM_UPDATE;
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute(MODEL_KEY) OrderDto.EditDto editDto,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return FORM_UPDATE;
        }
        orderService.update(id, editDto);
        return REDIRECT_LIST;
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        orderService.delete(id);
        return REDIRECT_LIST;
    }
}