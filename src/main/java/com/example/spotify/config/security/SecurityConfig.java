package com.example.spotify.config.security;

import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity //remplaza el extends webSecurityConfig..
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;


    //whiteList que no requieren autenticación(todas tienen esto)--> login.. no necesitamos token porque no lo tenemos

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.
                cors()
                        .disable().
                csrf()
                .disable()
                .authorizeHttpRequests()
                .requestMatchers("api/v1/auth/**") //representa nuestros path
                .permitAll() //permite todo los pattern de arriba
                .anyRequest()
                .authenticated() //todas las demas (anyRequest) necesitan estar autenticado el user
                .and() //add new configuration
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) //nueva sesion para cada peticion
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class); //antes del filtro UsernamePasswordFilter(defecto) el de nosotros.

        return http.build();
    }
}
