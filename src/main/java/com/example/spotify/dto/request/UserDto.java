package com.example.spotify.dto.request;

import com.example.spotify.config.security.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

public record UserDto(
        Integer id,
        @Email(message = "You must to type a valid email")

        String email,

        @NotBlank(message = "This field can't be blank")
        String username,

        @NotBlank(message = "This field can't be blank")
        String password,


        @CreationTimestamp
        LocalDate createAt,

        @Enumerated(EnumType.STRING)
        Role role
) {
}
