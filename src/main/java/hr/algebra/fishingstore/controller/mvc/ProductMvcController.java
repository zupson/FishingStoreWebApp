package hr.algebra.fishingstore.controller.mvc;

import hr.algebra.fishingstore.dal.dto.ProductDto;
import hr.algebra.fishingstore.dal.services.ProductService;
import hr.algebra.fishingstore.utilities.PathConst;
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
    static final String BASE_URL = PathConst.MVC + PathConst.PRODUCTS;

    private static final String REDIRECT_LIST = PathConst.REDIRECT_KEYWORD + BASE_URL;
    private static final String LIST_VIEW = PathConst.PRODUCTS + PathConst.LIST;
    private static final String DETAILS_VIEW = PathConst.PRODUCTS + PathConst.DETAILS;
    private static final String FORM_CREATE_VIEW = PathConst.PRODUCTS + PathConst.FORM_CREATE;
    private static final String FORM_UPDATE_VIEW = PathConst.PRODUCTS + PathConst.FORM_UPDATE;
    private static final String MODEL_KEY = "products";
    public static final String CATEGORY_ID = "categoryId";

    private final ProductService productService;

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute(MODEL_KEY, productService.getAll());
        return LIST_VIEW;
    }

    @GetMapping(PathConst.ID)
    public String getById(@PathVariable Long id, Model model) {
        model.addAttribute(MODEL_KEY, productService.getById(id));
        return DETAILS_VIEW;
    }

    @GetMapping(PathConst.NEW)
    public String createForm(Model model) {
        model.addAttribute(MODEL_KEY, new ProductDto.CreateDto());
        return FORM_CREATE_VIEW;
    }

    @PostMapping(PathConst.CREATE)
    public String create(@Valid @ModelAttribute(MODEL_KEY) ProductDto.CreateDto createDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors())
            return FORM_CREATE_VIEW;

        productService.create(createDto);
        return REDIRECT_LIST;
    }

    @GetMapping(PathConst.EDIT + PathConst.ID)
    public String editForm(@PathVariable Long id, Model model) {
        ProductDto.ResponseDto product = productService.getById(id);

        ProductDto.EditDto editDto = new ProductDto.EditDto();
        editDto.setName(product.getName());
        editDto.setDescription(product.getDescription());
        editDto.setPrice(product.getPrice());
        editDto.setOnStock(product.isOnStock());
        editDto.setCategoryId(product.getCategoryId());

        model.addAttribute(MODEL_KEY, editDto);
        model.addAttribute(CATEGORY_ID, id);
        return FORM_UPDATE_VIEW;
    }

    @PostMapping(PathConst.EDIT + PathConst.ID)
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute(MODEL_KEY) ProductDto.EditDto editDto,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return FORM_UPDATE_VIEW;

        productService.update(id, editDto);
        return REDIRECT_LIST;
    }

    @PostMapping(PathConst.DELETE + PathConst.ID)
    public String delete(@PathVariable Long id) {
        productService.delete(id);
        return REDIRECT_LIST;
    }
}
