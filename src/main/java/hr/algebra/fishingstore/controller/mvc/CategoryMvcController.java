package hr.algebra.fishingstore.controller.mvc;

import hr.algebra.fishingstore.dal.dto.CategoryDto;
import hr.algebra.fishingstore.dal.services.CategoryService;
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
    static final String BASE_URL = "/mvc/categories";
    private static final String REDIRECT_LIST = "redirect:" + BASE_URL;
    private static final String VIEW_PREFIX = "categories/";
    private static final String LIST_VIEW = VIEW_PREFIX + "list";
    private static final String DETAILS_VIEW = VIEW_PREFIX + "details";
    private static final String FORM_CREATE = VIEW_PREFIX + "form-create";
    private static final String FORM_UPDATE = VIEW_PREFIX + "form-update";
    private static final String MODEL_KEY = "categories";

    private final CategoryService categoryService;

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute(MODEL_KEY, categoryService.getAll());
        return LIST_VIEW;
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable Long id, Model model) {
        model.addAttribute(MODEL_KEY, categoryService.getById(id));
        return DETAILS_VIEW;
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute(MODEL_KEY, new CategoryDto.CreateDto());
        return FORM_CREATE;
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute(MODEL_KEY) CategoryDto.CreateDto createDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return FORM_CREATE;
        }
        categoryService.create(createDto);
        return REDIRECT_LIST;
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        CategoryDto.ResponseDto category = categoryService.getById(id);

        CategoryDto.EditDto editDto = new CategoryDto.EditDto();
        editDto.setName(category.getName());
        editDto.setDescription(category.getDescription());

        model.addAttribute(MODEL_KEY, editDto);
        model.addAttribute("categoryId", id);
        return FORM_UPDATE;
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute(MODEL_KEY) CategoryDto.EditDto editDto,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return FORM_UPDATE;
        }
        categoryService.update(id, editDto);
        return REDIRECT_LIST;
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        categoryService.delete(id);
        return REDIRECT_LIST;
    }
}