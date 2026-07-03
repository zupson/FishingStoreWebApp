package hr.algebra.fishingstore.controller.mvc;

import hr.algebra.fishingstore.dal.dto.CategoryDto;
import hr.algebra.fishingstore.dal.services.CategoryService;
import hr.algebra.fishingstore.utilities.PathConst;
import hr.algebra.fishingstore.utilities.ViewPathConst;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(CategoryMvcController.BASE_URL)
@RequiredArgsConstructor
public class CategoryMvcController {
    static final String BASE_URL = PathConst.MVC + PathConst.CATEGORIES;
    private static final String REDIRECT_LIST = PathConst.REDIRECT_KEYWORD + BASE_URL;

    private static final String MODEL_KEY = "categories";
    private static final String CATEGORY_ID = "categoryId";

    private final CategoryService categoryService;

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute(MODEL_KEY, categoryService.getAll());
        return ViewPathConst.CATEGORIES_LIST;
    }

    @GetMapping(PathConst.ID)
    public String getById(@PathVariable Long id, Model model) {
        model.addAttribute(MODEL_KEY, categoryService.getById(id));
        return ViewPathConst.CATEGORIES_DETAILS;
    }

    @GetMapping(PathConst.NEW)
    public String createForm(Model model) {
        model.addAttribute(MODEL_KEY, new CategoryDto.CreateDto());
        return ViewPathConst.CATEGORIES_FORM_CREATE;
    }

    @PostMapping(PathConst.CREATE)
    public String create(@Valid @ModelAttribute(MODEL_KEY) CategoryDto.CreateDto createDto,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return ViewPathConst.CATEGORIES_FORM_CREATE;

        categoryService.create(createDto);
        return REDIRECT_LIST;
    }

    @GetMapping(PathConst.EDIT + PathConst.ID)
    public String editForm(@PathVariable Long id, Model model) {
        CategoryDto.ResponseDto category = categoryService.getById(id);

        CategoryDto.EditDto editDto = new CategoryDto.EditDto();
        editDto.setName(category.getName());

        model.addAttribute(MODEL_KEY, editDto);
        model.addAttribute(CATEGORY_ID, id);
        return ViewPathConst.CATEGORIES_FORM_UPDATE;
    }

    @PostMapping(PathConst.UPDATE + PathConst.ID)
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute(MODEL_KEY) CategoryDto.EditDto editDto,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return ViewPathConst.CATEGORIES_FORM_UPDATE;

        categoryService.update(id, editDto);
        return REDIRECT_LIST;
    }

    @PostMapping(PathConst.DELETE + PathConst.ID)
    public String delete(@PathVariable Long id) {
        categoryService.delete(id);
        return REDIRECT_LIST;
    }
}