package com.example.demo.security.confg;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final MyUserDetailService userDetailsService;

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorize) ->
                        authorize
                                .requestMatchers("/",
                                        "/login",
                                        "/register",
                                        "/registerProcessing",
                                        "/loginProcessing",
                                        "/showRates",
                                        "/medicines/sort",
                                        "/medicines/filter",
                                        "/medicines/search",
                                        "/medicines/*",
                                        "/reviewCart",
                                        "/removeFromCart/*",
                                        "/addOneToCart/*",
                                        "removeOneFromCart/*"
                                        )
                                .permitAll()

                                .requestMatchers("/random",
                                        "/order/*",
                                        "/confirmOrder/*",
                                        "/rateUs*",
                                        "/saveRating",
                                        "/addToCart/*")
                                .hasRole("USER")

                                .requestMatchers("/addMedicine",
                                        "/saveMedicine",
                                        "/editMedicine",
                                        "/editMedicine/*",
                                        "/medicineStock",
                                        "/deleteMedicine/*",
                                        "/listOrders",
                                        "/deleteOrder/*",
                                        "/listUsers",
                                        "/deleteUser/*",
                                        "/banUser/*",
                                        "/activateUser/*")
                                .hasRole("ADMIN")

                                .requestMatchers("/profile")
                                .authenticated()

                                .requestMatchers(
                                        antMatcher("/css/**")
                                ).permitAll()

                                .anyRequest().authenticated()
                )
                .logout(
                        logout -> logout
                                .logoutSuccessUrl("/")
                )
                .formLogin(
                        form -> form
                                .loginPage("/login")
                                .usernameParameter("username")
                                .passwordParameter("password")
                                .loginProcessingUrl("/loginProcessing")
                                .defaultSuccessUrl("/", true)
                                .permitAll()).build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

}
