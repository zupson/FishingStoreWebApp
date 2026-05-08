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
@RequestMapping("/mvc/cart-products")
@RequiredArgsConstructor
public class CartProductMvcController {
    private final CartProductService cartProductService;

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("cart-products", cartProductService.getAll());
        return "cart-products/list";
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable Long id, Model model) {
        model.addAttribute("cart-products", cartProductService.getById(id));
        return "cart-products/details";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("cart-products", new CartProductDto.CreateDto());
        return "cart-products/form-create";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("cart-products") CartProductDto.CreateDto createDto,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "cart-products/form-create";
        }
        cartProductService.create(createDto);
        return "redirect:/cart-products";
    }

    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        cartProductService.delete(id);
        return "redirect:/cart-products";
    }
}