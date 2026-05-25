package hr.algebra.fishingstore.controller.mvc;

import hr.algebra.fishingstore.dal.dto.UserDto;
import hr.algebra.fishingstore.dal.services.UserService;
import hr.algebra.fishingstore.utilities.PathConst;
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
    static final String BASE_URL = PathConst.MVC +  PathConst.USERS;

    private static final String REDIRECT_LIST = PathConst.REDIRECT_KEYWORD + BASE_URL;
    private static final String LIST_VIEW = PathConst.USERS + PathConst.LIST;
    private static final String DETAILS_VIEW = PathConst.USERS + PathConst.DETAILS;
    private static final String FORM_UPDATE_VIEW = PathConst.USERS + PathConst.FORM_UPDATE;
    private static final String FORM_CHANGE_PASSWORD_VIEW = PathConst.USERS + PathConst.FORM_CHANGE_PASSWORD;
    private static final String MODEL_KEY = "users";
    public static final String USER_ID = "userId";
    public static final String CHANGE_PASSWORD_NAME = "changePassword";

    private final UserService userService;

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute(MODEL_KEY, userService.getAll());
        return LIST_VIEW;
    }

    @GetMapping(PathConst.ID)
    public String getById(@PathVariable Long id, Model model) {
        model.addAttribute(MODEL_KEY, userService.getById(id));
        return DETAILS_VIEW;
    }

    @GetMapping(PathConst.EDIT + PathConst.ID)
    public String editForm(@PathVariable Long id, Model model) {
        UserDto.ResponseDto user = userService.getById(id);

        UserDto.EditDto editDto = new UserDto.EditDto();
        editDto.setFirstName(user.getFirstName());
        editDto.setLastName(user.getLastName());
        editDto.setEmail(user.getEmail());
        editDto.setUsername(user.getUsername());

        model.addAttribute(MODEL_KEY, editDto);
        model.addAttribute(USER_ID, id);
        return FORM_UPDATE_VIEW;
    }

    @PostMapping(PathConst.UPDATE + PathConst.ID)
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute(MODEL_KEY) UserDto.EditDto editDto,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return FORM_UPDATE_VIEW;

        userService.update(id, editDto);
        return REDIRECT_LIST;
    }

    @GetMapping(PathConst.CHANGE_PASSWORD)
    public String changePasswordForm(Model model) {
        model.addAttribute(CHANGE_PASSWORD_NAME, new UserDto.ChangePasswordDto());
        return FORM_CHANGE_PASSWORD_VIEW;
    }

    @PostMapping(PathConst.CHANGE_PASSWORD)
    public String changePassword(@Valid @ModelAttribute(CHANGE_PASSWORD_NAME) UserDto.ChangePasswordDto dto,
                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return FORM_CHANGE_PASSWORD_VIEW;

        userService.changePassword(dto);
        return REDIRECT_LIST;
    }

    @PostMapping(PathConst.DELETE + PathConst.ID)
    public String delete(@PathVariable Long id) {
        userService.delete(id);
        return REDIRECT_LIST;
    }
}
