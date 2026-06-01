package hr.algebra.fishingstore.dal.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import hr.algebra.fishingstore.dal.services.token.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";
    public static final String USE_ACCESS_TOKEN = "Use access token";
    public static final String TOKEN_IS_NOT_VALID = "Token is not valid";

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader(AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith(BEARER)) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(BEARER.length());
        final String username;

        try {
            if (!jwtService.isAccessToken(jwt)) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, USE_ACCESS_TOKEN);
                return;
            }
            username = jwtService.extractUsername(jwt);
        } catch (JWTVerificationException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, TOKEN_IS_NOT_VALID);
            return;
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            SecurityContextHolder.getContext().setAuthentication(buildAuthentication(request, jwt));
        }
        filterChain.doFilter(request, response);
    }


    private UsernamePasswordAuthenticationToken buildAuthentication(HttpServletRequest request, String jwt) {
        String role = jwtService.extractRole(jwt);
        Long userId = jwtService.extractUserId(jwt);

        List<SimpleGrantedAuthority> simpleGrantedAuthorities = List.of(new SimpleGrantedAuthority(role));

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                userId, null, simpleGrantedAuthorities);

        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return auth;
    }
}
