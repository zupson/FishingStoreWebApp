package hr.algebra.fishingstore.controller.mvc;

import hr.algebra.fishingstore.dal.dto.ProductDto;
import hr.algebra.fishingstore.dal.services.CategoryService;
import hr.algebra.fishingstore.dal.services.ProductService;
import hr.algebra.fishingstore.utilities.PathConst;
import hr.algebra.fishingstore.utilities.ViewPathConst;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping(ProductMvcController.BASE_URL)
@RequiredArgsConstructor
public class ProductMvcController {
    static final String BASE_URL = PathConst.MVC + PathConst.PRODUCTS;
    private static final String REDIRECT_LIST = PathConst.REDIRECT_KEYWORD + BASE_URL;

    private static final String MODEL_KEY = "products";
    private static final String MODEL_KEY_CATEGORIES = "categories";
    private static final String CATEGORY_ID = "categoryId";
    private static final String IMAGE = "image";

    private final ProductService productService;
    private final CategoryService categoryService;

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute(MODEL_KEY, productService.getAll());
        return ViewPathConst.PRODUCTS_LIST;
    }

    @GetMapping(PathConst.ID)
    public String getById(@PathVariable Long id,
                          @RequestParam(required = false) Long categoryId,
                          Model model) {
        model.addAttribute(MODEL_KEY, productService.getById(id));
        model.addAttribute(CATEGORY_ID, categoryId);
        return ViewPathConst.PRODUCTS_DETAILS;
    }

    @GetMapping(PathConst.CATEGORIES + PathConst.ID)
    public String getByCategories(@PathVariable Long id, Model model) {
        model.addAttribute(MODEL_KEY, productService.getByCategoryId(id));
        model.addAttribute(CATEGORY_ID, id);
        return ViewPathConst.PRODUCTS_LIST;
    }

    @GetMapping(PathConst.NEW)
    public String createForm(@RequestParam Long categoryId, Model model) {
        ProductDto.CreateDto createDto = new ProductDto.CreateDto();
        createDto.setCategoryId(categoryId);

        model.addAttribute(MODEL_KEY, createDto);
        model.addAttribute(MODEL_KEY_CATEGORIES, categoryService.getAll());
        return ViewPathConst.PRODUCTS_FORM_CREATE;
    }

    @PostMapping(PathConst.CREATE)
    public String create(@Valid @ModelAttribute(MODEL_KEY) ProductDto.CreateDto createDto,
                         BindingResult bindingResult,
                         @RequestParam(value = IMAGE, required = false) MultipartFile image) {

        if (bindingResult.hasErrors())
            return ViewPathConst.PRODUCTS_FORM_CREATE;

        productService.create(createDto, image);
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
        model.addAttribute(MODEL_KEY_CATEGORIES, categoryService.getAll());

        return ViewPathConst.PRODUCTS_FORM_UPDATE;
    }

    @PostMapping(PathConst.EDIT + PathConst.ID)
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute(MODEL_KEY) ProductDto.EditDto editDto,
                         BindingResult bindingResult,
                         @RequestParam(value = IMAGE, required = false) MultipartFile image) {
        if (bindingResult.hasErrors())
            return ViewPathConst.PRODUCTS_FORM_UPDATE;

        productService.update(id, editDto, image);
        return REDIRECT_LIST;
    }

    @PostMapping(PathConst.DELETE + PathConst.ID)
    public String delete(@PathVariable Long id) {
        productService.delete(id);
        return REDIRECT_LIST;
    }
}