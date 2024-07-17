package peep.pea.collection.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SecurityConfig(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/",
                    "/home",
                    "/login-user",
                    "/register-user",
                    "/contact-peeppea",
                    "/peepuser-account",
                    "/aboutPeepPea",
                    "/newUser",
                    "/peepuser",
                    "/peep-user-page",
                    "/addPeepComment",
                    "/saveUser",
                    "/sendMessage",
                    "/blog/{id}",
                    "/blog-list",
                    "/getAllBlogs",
                    "/peeppea-crew/Peepville.html",
                    "/characters/*",
                    "/about",
                    "/css/**",
                    "/js/**",
                    "/images/**"
                )
                .permitAll()
                .requestMatchers("/newBlog", "/saveBlog", "/peep-user-page", "/addComment")
                .authenticated()
                .anyRequest()
                .authenticated())
            .formLogin(form -> form
                .loginPage("/login-user")
                .loginProcessingUrl("/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .defaultSuccessUrl("/peepuser", true)
                .permitAll())
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login-user?logout")
                .permitAll());
        return http.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }
}