package hr.algebra.fishingstore.controller.mvc;

import hr.algebra.fishingstore.dal.dto.AddressDto;
import hr.algebra.fishingstore.dal.services.AddressService;
import hr.algebra.fishingstore.utilities.PathConst;
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
    private static final String LIST_VIEW = PathConst.ADDRESSES + PathConst.LIST;
    private static final String DETAILS_VIEW = PathConst.ADDRESSES + PathConst.DETAILS;
    private static final String FORM_CREATE_VIEW = PathConst.ADDRESSES + PathConst.FORM_CREATE;
    private static final String MODEL_KEY = "addresses";

    private final AddressService addressService;

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute(MODEL_KEY, addressService.getAll());
        return LIST_VIEW;
    }

    @GetMapping(PathConst.ID)
    public String getById(@PathVariable Long id, Model model) {
        model.addAttribute(MODEL_KEY, addressService.getById(id));
        return DETAILS_VIEW;
    }

    @GetMapping(PathConst.NEW)
    public String createForm(Model model) {
        model.addAttribute(MODEL_KEY, new AddressDto.CreateDto());
        return FORM_CREATE_VIEW;
    }

    @PostMapping(PathConst.CREATE)
    public String create(@Valid @ModelAttribute(MODEL_KEY) AddressDto.CreateDto createDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return FORM_CREATE_VIEW;
        }
        addressService.create(createDto);
        return REDIRECT_LIST;
    }

    @PostMapping(PathConst.DELETE + PathConst.ID)
    public String delete(@PathVariable Long id) {
        addressService.delete(id);
        return REDIRECT_LIST;
    }
}