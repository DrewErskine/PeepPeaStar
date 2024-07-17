package peep.pea.collection.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/home", "/login-user", "/register-user", "/contact-peeppea", "/peepuser-account", "/aboutPeepPea", "/newUser", "/peepuser", "/peep-user-page", "/addComment",
                        "/blog/{id}", "/blog-list", "/getAllBlogs", "/peeppea-crew/Peepville.html", "/characters/*",
                        "/about", "/css/**", "/js/**", "/images/**")
                .permitAll()
                .requestMatchers("/newBlog", "/saveBlog", "/peep-user-page", "/addComment")
                .authenticated()
                .anyRequest()
                .authenticated())
            .formLogin(form -> form
                .loginPage("/login-user")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/peepuser", true)
                .permitAll())
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login-user?logout")
                .permitAll());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        User.UserBuilder users = User.builder().passwordEncoder(passwordEncoder::encode);
        UserDetails drew = users
            .username("drew")
            .password("peeppea33")
            .authorities("USER")
            .build();
        UserDetails peeppea = users
            .username("peeppea")
            .password("peeppea33")
            .authorities("USER")
            .build();
        return new InMemoryUserDetailsManager(drew, peeppea);
    }
}