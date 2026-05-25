package hr.algebra.fishingstore.controller.mvc;

import hr.algebra.fishingstore.dal.dto.CategoryDto;
import hr.algebra.fishingstore.dal.services.CategoryService;
import hr.algebra.fishingstore.utilities.PathConst;
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

    private static final String LIST_VIEW = PathConst.CATEGORIES + PathConst.LIST;
    private static final String DETAILS_VIEW = PathConst.CATEGORIES + PathConst.DETAILS;
    private static final String FORM_CREATE_VIEW = PathConst.CATEGORIES + PathConst.FORM_CREATE;
    private static final String FORM_UPDATE_VIEW = PathConst.CATEGORIES + PathConst.FORM_UPDATE;
    private static final String MODEL_KEY = "categories";
    public static final String CATEGORY_ID = "categoryId";

    private final CategoryService categoryService;

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute(MODEL_KEY, categoryService.getAll());
        return LIST_VIEW;
    }

    @GetMapping(PathConst.ID)
    public String getById(@PathVariable Long id, Model model) {
        model.addAttribute(MODEL_KEY, categoryService.getById(id));
        return DETAILS_VIEW;
    }

    @GetMapping(PathConst.NEW)
    public String createForm(Model model) {
        model.addAttribute(MODEL_KEY, new CategoryDto.CreateDto());
        return FORM_CREATE_VIEW;
    }

    @PostMapping(PathConst.CREATE)
    public String create(@Valid @ModelAttribute(MODEL_KEY) CategoryDto.CreateDto createDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return FORM_CREATE_VIEW;
        }
        categoryService.create(createDto);
        return REDIRECT_LIST;
    }

    @GetMapping(PathConst.EDIT + PathConst.ID)
    public String editForm(@PathVariable Long id, Model model) {
        CategoryDto.ResponseDto category = categoryService.getById(id);

        CategoryDto.EditDto editDto = new CategoryDto.EditDto();
        editDto.setName(category.getName());
        editDto.setDescription(category.getDescription());

        model.addAttribute(MODEL_KEY, editDto);
        model.addAttribute(CATEGORY_ID, id);
        return FORM_UPDATE_VIEW;
    }

    @PostMapping(PathConst.UPDATE + PathConst.ID)
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute(MODEL_KEY) CategoryDto.EditDto editDto,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return FORM_UPDATE_VIEW;
        }
        categoryService.update(id, editDto);
        return REDIRECT_LIST;
    }

    @PostMapping(PathConst.DELETE + PathConst.ID)
    public String delete(@PathVariable Long id) {
        categoryService.delete(id);
        return REDIRECT_LIST;
    }
}