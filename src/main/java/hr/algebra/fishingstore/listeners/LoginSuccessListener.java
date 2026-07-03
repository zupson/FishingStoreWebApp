package hr.algebra.fishingstore.listeners;

import hr.algebra.fishingstore.dal.services.LoginHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoginSuccessListener {
    public static final String UNKNOWN = "unknown";
    private final LoginHistoryService loginHistoryService;

    @EventListener
    public void onLoginSuccess(AuthenticationSuccessEvent event){
        String username = event.getAuthentication().getName();
        Object details = event.getAuthentication().getDetails();
        String ipAddress = details instanceof WebAuthenticationDetails d ? d.getRemoteAddress() : UNKNOWN;

        loginHistoryService.create(ipAddress, username);
    }
}