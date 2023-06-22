package com.example.spotify.rest;


import com.example.spotify.dto.request.AuthUserDto;
import com.example.spotify.dto.request.UserRegisterDTO;
import com.example.spotify.dto.response.JwtResponseDto;
import com.example.spotify.dto.response.ResponseUserDto;
import com.example.spotify.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/auth")
public class AuthController {

    private final AuthenticationService authenticationService; //debo separar en AuthenticationService.

    @PostMapping(path = "/register")
    public ResponseEntity<ResponseUserDto> register(
            @RequestBody UserRegisterDTO userRegisterDTO
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authenticationService.registerUser(userRegisterDTO));
    }
    //tocara quitar este método..
    @PostMapping(path = "/register-admin")
    public ResponseEntity<ResponseUserDto> registerByAdmin(
            @RequestBody UserRegisterDTO userRegisterDTO
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authenticationService.registerUserByAdmin(userRegisterDTO));
    }

    @PostMapping(path = "/sign-in")
    public ResponseEntity<JwtResponseDto> signIn(
            @RequestBody AuthUserDto authUserDto
    ) {
        return ResponseEntity.ok(authenticationService.singIn(authUserDto));
    }
/*
    @PostMapping(path = "/sign-out")
    public ResponseEntity<JwtResponseDto> signOut(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String jwt) {
        return ResponseEntity.ok(authenticationService.signOut(jwt));
    }

 */

}

