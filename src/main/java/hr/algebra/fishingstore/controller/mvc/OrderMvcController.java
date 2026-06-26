package hr.algebra.fishingstore.controller.mvc;

import hr.algebra.fishingstore.dal.dto.OrderDto;
import hr.algebra.fishingstore.dal.services.AddressService;
import hr.algebra.fishingstore.dal.services.OrderService;
import hr.algebra.fishingstore.dal.services.ProductOrderService;
import hr.algebra.fishingstore.utilities.PathConst;
import hr.algebra.fishingstore.utilities.ViewPathConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping(OrderMvcController.BASE_URL)
@RequiredArgsConstructor
public class OrderMvcController {
    static final String BASE_URL = PathConst.MVC + PathConst.ORDERS;
    private static final String REDIRECT_LIST = PathConst.REDIRECT_KEYWORD + BASE_URL;
    private static final String MODEL_KEY = "orders";
    public static final String PRODUCT_ORDERS_MODEL = "productOrders";

    private static final String ORDER_ID = "orderId";
    private static final String PAY_PAL_SUCCESS = "/paypal/success";
    private static final String PAY_PAL_CANCEL = "/paypal/cancel";
    private static final String SLASH = "/";
    public static final String BACK_URL = "backUrl";
    public static final String REFERER = "Referer";

    private final OrderService orderService;
    private final AddressService addressService;
    private final ProductOrderService productOrderService;

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute(MODEL_KEY, orderService.getAll());
        return ViewPathConst.ORDERS_LIST_VIEW;
    }

    @GetMapping(PathConst.ID)
    public String getById(@PathVariable Long id, Model model, HttpServletRequest request) {
        model.addAttribute(MODEL_KEY, orderService.getById(id));
        model.addAttribute(PRODUCT_ORDERS_MODEL, productOrderService.getByOrderId(id));
        model.addAttribute(BACK_URL, request.getHeader(REFERER));
        return ViewPathConst.ORDERS_DETAILS_VIEW;
    }

    @GetMapping(PathConst.SEARCH)
    public String search(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            Model model) {
        model.addAttribute(MODEL_KEY, orderService.getByFilter(
                username,
                from != null ? from.atStartOfDay() : null,
                to != null ? to.atTime(23, 59, 59) : null));
        return ViewPathConst.ORDERS_LIST_VIEW;
    }

    @GetMapping(PathConst.NEW)
    public String createForm(Model model) {
        model.addAttribute(MODEL_KEY, new OrderDto.CreateDto());
        model.addAttribute(AddressMvcController.MODEL_KEY, addressService.getAll());
        return ViewPathConst.ORDERS_FORM_CREATE_VIEW;
    }
    @PostMapping(PathConst.CREATE)
    public String create(@Valid @ModelAttribute(MODEL_KEY) OrderDto.CreateDto createDto,
                         BindingResult bindingResult) {

        if (bindingResult.hasErrors())
            return ViewPathConst.ORDERS_FORM_CREATE_VIEW;

        OrderDto.ResponseDto response = orderService.create(createDto);

        if (response.getApprovalUrl() != null)
            return PathConst.REDIRECT_KEYWORD + response.getApprovalUrl();

        return REDIRECT_LIST;
    }

    @GetMapping(PAY_PAL_SUCCESS)
    public String paypalSuccess(@RequestParam String token, @RequestParam Long orderId) {
        orderService.confirmPayPalPayment(token, orderId);
        return PathConst.REDIRECT_KEYWORD + BASE_URL + SLASH + orderId;
    }

    @GetMapping(PAY_PAL_CANCEL)
    public String paypalCancel(@RequestParam Long orderId) {
        orderService.cancelOrder(orderId);
        return  PathConst.REDIRECT_KEYWORD + BASE_URL + SLASH + orderId;
    }

    @GetMapping(PathConst.EDIT + PathConst.ID)
    public String editForm(@PathVariable Long id, Model model) {
        OrderDto.ResponseDto order = orderService.getById(id);

        OrderDto.EditDto editDto = new OrderDto.EditDto();
        editDto.setOrderStatus(order.getOrderStatus());

        model.addAttribute(MODEL_KEY, editDto);
        model.addAttribute(ORDER_ID, id);
        return ViewPathConst.ORDERS_FORM_UPDATE_VIEW;
    }

    @PostMapping(PathConst.UPDATE + PathConst.ID)
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute(MODEL_KEY) OrderDto.EditDto editDto,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return ViewPathConst.ORDERS_FORM_UPDATE_VIEW;
        
        orderService.update(id, editDto);
        return REDIRECT_LIST;
    }

    @PostMapping(PathConst.DELETE + PathConst.ID)
    public String delete(@PathVariable Long id) {
        orderService.delete(id);
        return REDIRECT_LIST;
    }
}