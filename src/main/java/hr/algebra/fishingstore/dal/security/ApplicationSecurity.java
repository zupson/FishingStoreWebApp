package hr.algebra.fishingstore.dal.security;

import hr.algebra.fishingstore.dal.repos.UserRepository;
import hr.algebra.fishingstore.model.enums.Role;
import hr.algebra.fishingstore.session.RedirectSession;
import hr.algebra.fishingstore.utilities.PathConst;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class ApplicationSecurity {
    public static final String INVALID_CREDENTIALS = "Invalid credentials";
    public static final int FORBIDDEN = 403;
    public static final String ACCESS_DENIED = "Access denied";

    private final JwtAuthFilter jwtAuthFilter;
    private final UserRepository userRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(INVALID_CREDENTIALS));
    }

    @Bean
    @Order(1)
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        return http
                .securityMatcher(PathConst.API + PathConst.ALL)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(
                        session ->
                                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PathConst.API + PathConst.AUTH + PathConst.LOGIN).permitAll()
                        .requestMatchers(PathConst.API + PathConst.AUTH + PathConst.REGISTER).permitAll()
                        .requestMatchers(PathConst.API + PathConst.AUTH + PathConst.REFRESH).permitAll()
                        .requestMatchers(PathConst.API + PathConst.AUTH + PathConst.LOGOUT).permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(ex->ex.accessDeniedHandler((
                        request, response, accessDeniedException) -> {
                    response.setStatus(FORBIDDEN);
                    response.getWriter().write(ACCESS_DENIED);
                }))
                .build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain mvcFilterChain(HttpSecurity http, RedirectSession redirectSession) throws Exception {
        return http
                .securityMatcher(PathConst.MVC + PathConst.ALL)
                .csrf(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(PathConst.MVC + PathConst.PRODUCTS).permitAll()
                        .requestMatchers(PathConst.MVC + PathConst.PRODUCTS + PathConst.ID).permitAll()

                        .requestMatchers(PathConst.MVC + PathConst.CATEGORIES).permitAll()
                        .requestMatchers(PathConst.MVC + PathConst.CATEGORIES + PathConst.ID).permitAll()

                        .requestMatchers(PathConst.MVC + PathConst.CART_PRODUCTS + PathConst.ALL).permitAll()
                        .requestMatchers(PathConst.MVC + PathConst.ORDERS).hasAnyRole(Role.USER.name(), Role.ADMIN.name())
                        .requestMatchers(PathConst.MVC + PathConst.PAYMENTS).hasAnyRole(Role.USER.name(), Role.ADMIN.name())

                        .requestMatchers(PathConst.MVC + PathConst.USERS + PathConst.ALL).hasRole(Role.ADMIN.name())
                        .requestMatchers(PathConst.MVC + PathConst.ADDRESSES + PathConst.ALL).hasAnyRole(Role.USER.name(), Role.ADMIN.name())
                        .requestMatchers(PathConst.MVC + PathConst.LOGIN_HISTORIES + PathConst.ALL).hasRole(Role.ADMIN.name())
                        .requestMatchers(PathConst.MVC + PathConst.PRODUCT_ORDERS + PathConst.ALL).hasRole(Role.ADMIN.name())

                        .requestMatchers(PathConst.MVC + PathConst.PRODUCTS + PathConst.NEW).hasRole(Role.ADMIN.name())
                        .requestMatchers(PathConst.MVC + PathConst.PRODUCTS + PathConst.EDIT + PathConst.ALL).hasRole(Role.ADMIN.name())
                        .requestMatchers(PathConst.MVC + PathConst.PRODUCTS + PathConst.DELETE + PathConst.ALL).hasRole(Role.ADMIN.name())
                        .requestMatchers(PathConst.MVC + PathConst.PRODUCTS + PathConst.CATEGORIES + PathConst.ALL).permitAll()

                        .requestMatchers(PathConst.MVC + PathConst.AUTH + PathConst.REGISTER).permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage(PathConst.MVC + PathConst.AUTH + PathConst.LOGIN)
                        .loginProcessingUrl(PathConst.MVC + PathConst.AUTH + PathConst.LOGIN)
                        .successHandler(
                                (request, response, authentication) -> {

                                    boolean isAdmin = authentication.getAuthorities().stream()
                                            .anyMatch(
                                                    a -> a.getAuthority().equals(Role.ADMIN.toAuthority()));

                                    if(isAdmin){
                                        response.sendRedirect(PathConst.MVC + PathConst.ORDERS);
                                        return;
                                    }

                                    String redirect = redirectSession.getAndClear();
                                    response.sendRedirect(redirect != null
                                            ? redirect
                                            : PathConst.MVC + PathConst.CATEGORIES);
                                })
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl(PathConst.MVC + PathConst.AUTH + PathConst.LOGOUT)
                        .logoutSuccessUrl(PathConst.MVC + PathConst.PRODUCTS)
                )
                .build();
    }
}