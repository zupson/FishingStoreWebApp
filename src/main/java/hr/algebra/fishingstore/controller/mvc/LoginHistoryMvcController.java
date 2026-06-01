package hr.algebra.fishingstore.controller.mvc;

import hr.algebra.fishingstore.dal.services.LoginHistoryService;
import hr.algebra.fishingstore.utilities.PathConst;
import hr.algebra.fishingstore.utilities.ViewPathConst;
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
    static final String BASE_URL = PathConst.MVC + PathConst.LOGIN_HISTORIES;
    private static final String MODEL_KEY = "loginHistories";

    private final LoginHistoryService loginHistoryService;

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute(MODEL_KEY, loginHistoryService.getAll());
        return ViewPathConst.LOGIN_HISTORY_LIST_VIEW;
    }

    @GetMapping(PathConst.ID)
    public String getById(@PathVariable Long id, Model model) {
        model.addAttribute(MODEL_KEY, loginHistoryService.getById(id));
        return  ViewPathConst.LOGIN_HISTORY_DETAILS_VIEW;
    }
}