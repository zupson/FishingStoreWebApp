package hr.algebra.fishingstore.exceptions;

import hr.algebra.fishingstore.utilities.PathConst;
import hr.algebra.fishingstore.utilities.ViewPathConst;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    public static final String ERROR = "error";
    public static final String ERROR_500_VIEW = "error/500";
    public static final String ERROR_404_VIEW = "error/404";
    public static final String REDIRECT_MVC_CART_PRODUCTS = PathConst.REDIRECT_KEYWORD + PathConst.MVC + PathConst.CART_PRODUCTS;

    @ExceptionHandler(DuplicateUserException.class)
    public String handleDuplicateUserException(DuplicateUserException e, Model model){
        model.addAttribute(ERROR, e.getMessage());
        return ViewPathConst.AUTH_REGISTER_VIEW;
    }


    @ExceptionHandler(CartEmptyException.class)
    public String handleCartEmpty(CartEmptyException e, Model model) {
        model.addAttribute(ERROR, e.getMessage());
        return REDIRECT_MVC_CART_PRODUCTS;
    }

    @ExceptionHandler(BadCredentialsException.class)
    public String handleBadCredentials(BadCredentialsException e, Model model) {
        model.addAttribute(ERROR, e.getMessage());
        return ViewPathConst.AUTH_LOGIN_VIEW;
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public String handleEntityNotFound(EntityNotFoundException e, Model model) {
        model.addAttribute(ERROR, e.getMessage());
        return ERROR_404_VIEW;
    }

    @ExceptionHandler(RuntimeException.class)
    public String handleRuntime(RuntimeException e, Model model) {
        model.addAttribute(ERROR, e.getMessage());
        return ERROR_500_VIEW;
    }
}