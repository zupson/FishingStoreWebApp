package hr.algebra.fishingstore.session;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.net.URI;
import java.net.URISyntaxException;

@Component
@SessionScope

public class RedirectSession {
    public static final String REFERER = "Referer";
    public static final String AUTH = "/auth/";
    private String redirectUrl;

    public void save(HttpServletRequest request) {
        String referer = request.getHeader(REFERER);

        if (referer != null && !referer.contains(AUTH)){
            try{
                this.redirectUrl = new URI(referer).getPath();
            } catch (URISyntaxException e) {
                this.redirectUrl = referer;
            }
        }
    }

    public String getAndClear() {
        String url = this.redirectUrl;
        this.redirectUrl = null;
        return url;
    }
}
