package hr.algebra.fishingstore.controller.mvc;

import hr.algebra.fishingstore.dal.dto.AddressDto;
import hr.algebra.fishingstore.dal.services.AddressService;
import hr.algebra.fishingstore.utilities.PathConst;
import hr.algebra.fishingstore.utilities.ViewPathConst;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(AddressMvcController.BASE_URL)
@RequiredArgsConstructor
public class AddressMvcController {
    static final String BASE_URL = PathConst.MVC + PathConst.ADDRESSES;
    private static final String REDIRECT_LIST = PathConst.REDIRECT_KEYWORD + BASE_URL;
    private static final String REDIRECT_ORDERS_NEW = PathConst.REDIRECT_KEYWORD + PathConst.MVC + PathConst.ORDERS + PathConst.NEW;
    public static final String MODEL_KEY = "addresses";

    private final AddressService addressService;

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute(MODEL_KEY, addressService.getAll());
        return ViewPathConst.ADDRESSES_LIST;
    }

    @GetMapping(PathConst.ID)
    public String getById(@PathVariable Long id, Model model) {
        model.addAttribute(MODEL_KEY, addressService.getById(id));
        return ViewPathConst.ADDRESSES_DETAILS;
    }

    @GetMapping(PathConst.NEW)
    public String createForm(Model model) {
        model.addAttribute(MODEL_KEY, new AddressDto.CreateDto());
        return ViewPathConst.ADDRESSES_FORM_CREATE;
    }

    @PostMapping(PathConst.CREATE)
    public String create(@Valid @ModelAttribute(MODEL_KEY) AddressDto.CreateDto createDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return ViewPathConst.ADDRESSES_FORM_CREATE;
        addressService.create(createDto);
        return REDIRECT_ORDERS_NEW;
    }

    @PostMapping(PathConst.DELETE + PathConst.ID)
    public String delete(@PathVariable Long id) {
        addressService.delete(id);
        return REDIRECT_LIST;
    }
}