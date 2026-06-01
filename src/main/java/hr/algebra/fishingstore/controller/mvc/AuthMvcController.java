package hr.algebra.fishingstore.controller.mvc;

import hr.algebra.fishingstore.dal.dto.UserDto;
import hr.algebra.fishingstore.dal.services.AuthService;
import hr.algebra.fishingstore.model.enums.Role;
import hr.algebra.fishingstore.utilities.PathConst;
import hr.algebra.fishingstore.utilities.ViewPathConst;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(AuthMvcController.BASE_URL)
@RequiredArgsConstructor
public class AuthMvcController {
    static final String BASE_URL = PathConst.MVC + PathConst.AUTH;
    private static final String REDIRECT_LOGIN = PathConst.REDIRECT_KEYWORD + BASE_URL;
    private static final String REDIRECT_PAYMENT = PathConst.REDIRECT_KEYWORD + BASE_URL;

    public static final String MODEL_KEY_LOGIN = "loginDto";
    public static final String MODEL_KEY_REGISTER = "registerDto";
    private static final String HIDE_BUTTON = "hideButton";

    private final AuthService authService;

    @GetMapping(PathConst.LOGIN)
    public String loginPage(Model model) {
        model.addAttribute(MODEL_KEY_LOGIN, new UserDto.LoginDto());
        model.addAttribute(HIDE_BUTTON, true);
        return ViewPathConst.AUTH_LOGIN_VIEW;
    }

    @GetMapping(PathConst.REGISTER)
    public String registerPage(Model model) {
        model.addAttribute(MODEL_KEY_REGISTER, new UserDto.RegisterDto());
        model.addAttribute(HIDE_BUTTON, true);
        return ViewPathConst.AUTH_REGISTER_VIEW;
    }

    @PostMapping(PathConst.REGISTER)
    public String register(@Valid @ModelAttribute UserDto.RegisterDto registerDto,
                           BindingResult result) {
        if (result.hasErrors())
            return ViewPathConst.AUTH_REGISTER_VIEW;

        authService.register(registerDto, Role.USER);

        //TODO: dodaj da redirekta na plaćanje proizvoda u košarici
        return REDIRECT_PAYMENT;
    }

    @PostMapping(PathConst.LOGOUT)
    public String logout(HttpSession session) {
        session.invalidate();
        return REDIRECT_LOGIN;
    }
}