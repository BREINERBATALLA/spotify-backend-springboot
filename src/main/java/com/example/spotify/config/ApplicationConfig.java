package com.example.spotify.config;

import com.example.spotify.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final UserRepository userRepository;

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByEmail(username) //aca se retorna el UserDetail.
                .orElseThrow(()-> new UsernameNotFoundException("User not found")); //nuestra entity implm el UserDetails
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        //Es un Dao que se encarga de fecth userDEtails from bd, encoded password
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        //debemos decirle que userDetailsService va a usar para traer la info del user.
        authenticationProvider.setUserDetailsService(userDetailsService());
        //hay varias una en memory
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        //esta es la minima implementación.
        return authenticationProvider;
    }

    @Bean  //hold la info(config) default impl de spring boot del authentication manager el config-->
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        //responsable de manejar la authentication, pero la delega a un provider
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
