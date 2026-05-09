package hr.algebra.fishingstore.controller.mvc;

import hr.algebra.fishingstore.dal.dto.UserDto;
import hr.algebra.fishingstore.dal.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(UserMvcController.BASE_URL)
@RequiredArgsConstructor
public class UserMvcController {
    static final String BASE_URL ="/mvc/users";

    private static final String REDIRECT_LIST = "redirect:" + BASE_URL;
    private static final String VIEW_PREFIX = "users/";
    private static final String MODEL_KEY = "users";
    private static final String LIST_VIEW = VIEW_PREFIX + "list";
    private static final String DETAILS_VIEW = VIEW_PREFIX + "details";
    private static final String FORM_UPDATE = VIEW_PREFIX + "form-update";
    private static final String FORM_CHANGE_PASSWORD = VIEW_PREFIX + "form-change-password";

    private final UserService userService;

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute(MODEL_KEY, userService.getAll());
        return LIST_VIEW;
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable Long id, Model model) {
        model.addAttribute(MODEL_KEY, userService.getById(id));
        return DETAILS_VIEW;
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        UserDto.ResponseDto user = userService.getById(id);

        UserDto.EditDto editDto = new UserDto.EditDto();
        editDto.setFirstName(user.getFirstName());
        editDto.setLastName(user.getLastName());
        editDto.setEmail(user.getEmail());
        editDto.setUsername(user.getUsername());

        model.addAttribute(MODEL_KEY, editDto);
        model.addAttribute("userId", id);
        return FORM_UPDATE;
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute(MODEL_KEY) UserDto.EditDto editDto,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return FORM_UPDATE;
        }
        userService.update(id, editDto);
        return REDIRECT_LIST;
    }

    @GetMapping("/change-password")
    public String changePasswordForm(Model model) {
        model.addAttribute("changePassword", new UserDto.ChangePasswordDto());
        return FORM_CHANGE_PASSWORD;
    }

    @PostMapping("/change-password")
    public String changePassword(@Valid @ModelAttribute("changePassword") UserDto.ChangePasswordDto dto,
                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return FORM_CHANGE_PASSWORD;
        }
        userService.changePassword(dto);
        return REDIRECT_LIST;
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        userService.delete(id);
        return REDIRECT_LIST;
    }
}
