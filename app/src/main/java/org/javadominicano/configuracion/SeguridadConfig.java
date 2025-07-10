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
            .csrf(csrf -> csrf.disable())
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
        // Usuario Dariel
        String rawPasswordDariel = "dariel1234";
        String encodedDariel = passwordEncoder().encode(rawPasswordDariel);
        UserDetails dariel = User.builder()
            .username("dariel")
            .password(encodedDariel)
            .roles("ADMIN")
            .build();

        // Usuario Scarlet
        String rawPasswordScarlet = "scarlet1234";
        String encodedScarlet = passwordEncoder().encode(rawPasswordScarlet);
        UserDetails scarlet = User.builder()
            .username("scarlet")
            .password(encodedScarlet)
            .roles("USER")
            .build();

        // DEBUG
        System.out.println("PRUEBA - Usuario cargado: " + dariel.getUsername());
        System.out.println("PRUEBA - Contraseña en texto plano: " + rawPasswordDariel);
        System.out.println("PRUEBA - Usuario cargado: " + scarlet.getUsername());
        System.out.println("PRUEBA - Contraseña en texto plano: " + rawPasswordScarlet);

        return new InMemoryUserDetailsManager(dariel, scarlet);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}