package hr.algebra.fishingstore.controller.mvc;

import hr.algebra.fishingstore.dal.services.LoginHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(LoginHistoryMvcController.BASE_URL)
@RequiredArgsConstructor
public class LoginHistoryMvcController {
    static final String BASE_URL = "/mvc/login-histories";
    private static final String VIEW_PREFIX = "login-histories/";
    private static final String MODEL_KEY = "loginHistories";
    private static final String LIST_VIEW = VIEW_PREFIX + "list";
    private static final String DETAILS_VIEW = VIEW_PREFIX + "details";

    private final LoginHistoryService loginHistoryService;

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute(MODEL_KEY, loginHistoryService.getAll());
        return LIST_VIEW;
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable Long id, Model model) {
        model.addAttribute(MODEL_KEY, loginHistoryService.getById(id));
        return DETAILS_VIEW;
    }
}