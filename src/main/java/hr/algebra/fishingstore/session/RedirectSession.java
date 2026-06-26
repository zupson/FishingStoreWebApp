package hr.algebra.fishingstore.session;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope

public class RedirectSession {
    public static final String REFERER = "Referer";
    public static final String AUTH = "/auth/";
    private String redirectUrl;

    public void save(HttpServletRequest request) {
        String referer = request.getHeader(REFERER);
        if (referer != null && !referer.contains(AUTH))
            this.redirectUrl = referer;
    }

    public String getAndClear() {
        String url = this.redirectUrl;
        this.redirectUrl = null;
        return url;
    }
}
