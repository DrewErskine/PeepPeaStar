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
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/home", "/login", "/register", "/contact-peeppea", "/peepuser-account", "/aboutPeepPea", "/peepuser-account", 
                                "/blog/{id}", "/blog-list", "/getAllBlogs",
                                "/register-user",
                                "/about", "/css/**", "/js/**", "/images/**")
                        .permitAll()
                        .requestMatchers("/newBlog", "/saveBlog", "/peep-user-page").authenticated()
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login-user")
                        .defaultSuccessUrl("/home", true)
                        .permitAll())
                .logout(logout -> logout
                        .logoutSuccessUrl("/login-user?logout")
                        .permitAll());
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        User.UserBuilder users = User.builder().passwordEncoder(passwordEncoder::encode);
        UserDetails drew = users
                .username("drew")
                .password("peeppea33")
                .roles("USER")
                .build();
        UserDetails peeppea = users
                .username("peeppea")
                .password("peeppea33")
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(drew, peeppea);
    }
}