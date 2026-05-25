package hr.algebra.fishingstore.dal.security;

import hr.algebra.fishingstore.model.enums.Role;
import hr.algebra.fishingstore.utilities.PathConst;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
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
                        .requestMatchers(PathConst.API + PathConst.USERS + PathConst.LOGIN).permitAll()
                        .requestMatchers(PathConst.API + PathConst.USERS + PathConst.REGISTER).permitAll()
                        .requestMatchers(PathConst.API + PathConst.USERS + PathConst.REFRESH).permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain mvcFilterChain(HttpSecurity http) throws Exception {
        return http
                .securityMatcher(PathConst.MVC + PathConst.ALL)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(PathConst.MVC + PathConst.PRODUCTS).permitAll()
                        .requestMatchers(PathConst.MVC + PathConst.PRODUCTS + PathConst.ID).permitAll()

                        .requestMatchers(PathConst.MVC + PathConst.CATEGORIES).permitAll()
                        .requestMatchers(PathConst.MVC + PathConst.CATEGORIES + PathConst.ID).permitAll()

                        .requestMatchers(PathConst.MVC + PathConst.CART_PRODUCTS).hasAnyRole(Role.USER_ROLE, Role.ADMIN_ROLE)
                        .requestMatchers(PathConst.MVC + PathConst.ORDERS).hasAnyRole(Role.USER_ROLE, Role.ADMIN_ROLE)
                        .requestMatchers(PathConst.MVC + PathConst.PAYMENTS).hasAnyRole(Role.USER_ROLE, Role.ADMIN_ROLE)

                        .requestMatchers(PathConst.MVC + PathConst.USERS + PathConst.ALL).hasRole(Role.ADMIN_ROLE)
                        .requestMatchers(PathConst.MVC + PathConst.ADDRESSES + PathConst.ALL).hasRole(Role.ADMIN_ROLE)
                        .requestMatchers(PathConst.MVC + PathConst.LOGIN_HISTORIES + PathConst.ALL).hasRole(Role.ADMIN_ROLE)
                        .requestMatchers(PathConst.MVC + PathConst.PRODUCT_ORDERS + PathConst.ALL).hasRole(Role.ADMIN_ROLE)

                        .requestMatchers(PathConst.MVC + PathConst.PRODUCTS + PathConst.NEW).hasRole(Role.ADMIN_ROLE)
                        .requestMatchers(PathConst.MVC + PathConst.PRODUCTS + PathConst.EDIT + PathConst.ALL).hasRole(Role.ADMIN_ROLE)
                        .requestMatchers(PathConst.MVC + PathConst.PRODUCTS + PathConst.DELETE + PathConst.ALL).hasRole(Role.ADMIN_ROLE)

                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage(PathConst.MVC + PathConst.LOGIN)
                        .defaultSuccessUrl(PathConst.MVC + PathConst.PRODUCTS)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl(PathConst.MVC + PathConst.LOGOUT)
                        .logoutSuccessUrl(PathConst.MVC + PathConst.PRODUCTS)
                )
                .build();
    }
}