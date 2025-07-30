package com.javadominicano.configuracion;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SeguridadConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login", "/css/**", "/js/**", "/ws/**").permitAll()
                .requestMatchers("/", "/dashboard", "/api/datos-meteorologicos").hasAnyRole("USER", "ADMIN")
                .anyRequest().hasRole("ADMIN")
            )
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/ws/**") // Ignorar CSRF para rutas WebSocket
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/?pagina=0&tamanoPagina=10", true)
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout=true")
                .permitAll());

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        PasswordEncoder encoder = passwordEncoder();

        UserDetails dariel = User.builder()
            .username("dariel")
            .password(encoder.encode("dariel1234"))
            .roles("ADMIN")
            .build();

        UserDetails scarlet = User.builder()
            .username("scarlet")
            .password(encoder.encode("scarlet1234"))
            .roles("ADMIN")
            .build();

        return new InMemoryUserDetailsManager(dariel, scarlet);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
