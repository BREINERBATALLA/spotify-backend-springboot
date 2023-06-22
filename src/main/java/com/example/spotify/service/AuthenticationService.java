package com.example.spotify.service;

import com.example.spotify.config.security.JwtService;
import com.example.spotify.config.security.Role;
import com.example.spotify.dto.request.AuthUserDto;
import com.example.spotify.dto.request.UserRegisterDTO;
import com.example.spotify.dto.response.JwtResponseDto;
import com.example.spotify.dto.response.ResponseUserDto;
import com.example.spotify.entity.User;
import com.example.spotify.mapper.SongMapper;
import com.example.spotify.mapper.UserMapper;
import com.example.spotify.repository.SongRepository;
import com.example.spotify.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final NotificationService notificationService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public ResponseUserDto registerUser(UserRegisterDTO user)  {
        User newUser = new User();
        newUser.setEmail(user.email());
        newUser.setUsername(user.username());
        String passwordGenerated = generateRandomPassword(10);
        newUser.setPassword(passwordEncoder.encode(passwordGenerated));
        newUser.setRole(Role.USER);
        userRepository.save(newUser);
        notificationService.subscribeUser(user.email());
        return new ResponseUserDto(passwordGenerated);
    }

    public ResponseUserDto registerUserByAdmin(UserRegisterDTO userDTO) {
        User newUser = new User();
        newUser.setEmail(userDTO.email());
        newUser.setUsername(userDTO.username());
        String passwordGenerated = generateRandomPassword(12);
        newUser.setPassword(passwordEncoder.encode(passwordGenerated));
        newUser.setRole(Role.ADMIN);
        userRepository.save(newUser);
        notificationService.subscribeUser(userDTO.email());
        return new ResponseUserDto(passwordGenerated);
    }

    public JwtResponseDto singIn(AuthUserDto authUserDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authUserDto.email(), authUserDto.password()
                )
        ); //lanza exepcion si no encuentra dicho user y pass
        //both are coreect si llega acá.
        var user = userRepository.findByEmail(authUserDto.email())
                .orElse(null);
        var jwtToken = jwtService.generateToken(user);
        return new JwtResponseDto(jwtToken);
    }

    private String generateRandomPassword(int len)
    {
        // Rango ASCII – alfanumérico (0-9, a-z, A-Z)
        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        // cada iteración del bucle elige aleatoriamente un carácter del dado
        // rango ASCII y lo agrega a la instancia `StringBuilder`

        for (int i = 0; i < len; i++)
        {
            int randomIndex = random.nextInt(chars.length());
            sb.append(chars.charAt(randomIndex));
        }

        return sb.toString();
    }
}
