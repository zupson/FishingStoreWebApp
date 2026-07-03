package hr.algebra.fishingstore.controller.mvc;

import hr.algebra.fishingstore.dal.dto.OrderDto;
import hr.algebra.fishingstore.dal.dto.PaymentDto;
import hr.algebra.fishingstore.dal.services.PaymentService;
import hr.algebra.fishingstore.utilities.PathConst;
import hr.algebra.fishingstore.utilities.ViewPathConst;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(PaymentMvcController.BASE_URL)
@RequiredArgsConstructor
public class PaymentMvcController {
    static final String BASE_URL = PathConst.MVC + PathConst.PAYMENTS;
    private static final String REDIRECT_LIST = PathConst.REDIRECT_KEYWORD + BASE_URL;

    private static final String MODEL_KEY = "payments";
    public static final String ORDER_ID = "orderId";

    private final PaymentService paymentService;

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute(MODEL_KEY, paymentService.getAll());
        return ViewPathConst.PAYMENTS_LIST_VIEW;
    }

    @GetMapping(PathConst.ID)
    public String getById(@PathVariable Long id,
                          Model model) {
        model.addAttribute(MODEL_KEY, paymentService.getById(id));
        return ViewPathConst.PAYMENTS_DETAILS_VIEW;
    }

    @GetMapping(PathConst.NEW)
    public String createForm(Model model) {
        model.addAttribute(MODEL_KEY, new OrderDto.CreateDto());
        return ViewPathConst.PAYMENTS_FORM_CREATE_VIEW;
    }

    @PostMapping(PathConst.CREATE)
    public String create(@Valid @ModelAttribute(MODEL_KEY) PaymentDto.CreateDto createDto,
                         BindingResult bindingResult) {

        if (bindingResult.hasErrors())
            return ViewPathConst.PAYMENTS_FORM_CREATE_VIEW;

        paymentService.create(createDto);
        return REDIRECT_LIST;
    }

    @GetMapping(PathConst.EDIT + PathConst.ID)
    public String editForm(@PathVariable Long id,
                           Model model) {
        PaymentDto.ResponseDto payment = paymentService.getById(id);

        PaymentDto.EditDto editDto = new PaymentDto.EditDto();
        editDto.setPaymentStatus(payment.getPaymentStatus());

        model.addAttribute(MODEL_KEY, editDto);
        model.addAttribute(ORDER_ID, id);
        return ViewPathConst.PAYMENTS_FORM_UPDATE_VIEW;
    }

    @PostMapping(PathConst.UPDATE + PathConst.ID)
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute(MODEL_KEY) PaymentDto.EditDto editDto,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return ViewPathConst.PAYMENTS_FORM_UPDATE_VIEW;

        paymentService.update(id, editDto);
        return REDIRECT_LIST;
    }
}