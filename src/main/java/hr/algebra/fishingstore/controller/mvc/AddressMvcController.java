package hr.algebra.fishingstore.controller.mvc;

import hr.algebra.fishingstore.dal.dto.AddressDto;
import hr.algebra.fishingstore.dal.services.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("mvc/addresses")
@RequiredArgsConstructor
public class AddressMvcController {
    private final AddressService addressService;

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("addresses", addressService.getAll());
        return "addresses/list";
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable Long id, Model model) {
        model.addAttribute("addresses", addressService.getById(id));
        return "addresses/details";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("addresses", new AddressDto.CreateDto());
        return "addresses/form-create";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("addresses") AddressDto.CreateDto createDto,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "addresses/form-create";
        }
        addressService.create(createDto);
        return "redirect:/addresses";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        addressService.delete(id);
        return "redirect:/addresses";
    }
}