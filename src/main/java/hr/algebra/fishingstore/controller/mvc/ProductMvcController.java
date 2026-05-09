package hr.algebra.fishingstore.controller.mvc;

import hr.algebra.fishingstore.dal.dto.ProductDto;
import hr.algebra.fishingstore.dal.services.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(ProductMvcController.BASE_URL)
@RequiredArgsConstructor
public class ProductMvcController {
    static final String BASE_URL = "/mvc/products";

    private static final String REDIRECT_LIST = "redirect:" + BASE_URL;
    private static final String VIEW_PREFIX = "products/";
    private static final String LIST_VIEW = VIEW_PREFIX + "list";
    private static final String DETAILS_VIEW = VIEW_PREFIX + "details";
    private static final String FORM_CREATE = VIEW_PREFIX + "form-create";
    private static final String FORM_UPDATE = VIEW_PREFIX + "form-update";
    private static final String MODEL_KEY = "products";

    private final ProductService productService;

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute(MODEL_KEY, productService.getAll());
        return LIST_VIEW;
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable Long id, Model model) {
        model.addAttribute(MODEL_KEY, productService.getById(id));
        return DETAILS_VIEW;
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute(MODEL_KEY, new ProductDto.CreateDto());
        return FORM_CREATE;
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute(MODEL_KEY) ProductDto.CreateDto createDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return FORM_CREATE;
        }
        productService.create(createDto);
        return REDIRECT_LIST;
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        ProductDto.ResponseDto product = productService.getById(id);

        ProductDto.EditDto editDto = new ProductDto.EditDto();
        editDto.setName(product.getName());
        editDto.setDescription(product.getDescription());
        editDto.setPrice(product.getPrice());
        editDto.setOnStock(product.isOnStock());
        editDto.setCategoryId(product.getCategoryId());

        model.addAttribute(MODEL_KEY, editDto);
        model.addAttribute("categoryId", id);
        return FORM_UPDATE;
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute(MODEL_KEY) ProductDto.EditDto editDto,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return FORM_UPDATE;
        }
        productService.update(id, editDto);
        return REDIRECT_LIST;
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        productService.delete(id);
        return REDIRECT_LIST;
    }
}
