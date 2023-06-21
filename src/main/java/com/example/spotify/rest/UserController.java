package com.example.spotify.rest;

import com.example.spotify.dto.request.SongDto;
import com.example.spotify.dto.request.UserRegisterDTO;
import com.example.spotify.dto.response.ResponseUserDto;
import com.example.spotify.dto.response.SongResponseDto;
import com.example.spotify.entity.User;
import com.example.spotify.service.NotificationService;
import com.example.spotify.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final NotificationService notificationService;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ResponseUserDto> save(@RequestBody UserRegisterDTO userRegisterDTO) {
        notificationService.subscribeUser(userRegisterDTO.email());
        return new ResponseEntity<>(userService.registerUser(userRegisterDTO), HttpStatus.CREATED);
    }

    @PostMapping("/register/admin")
    public ResponseEntity<ResponseUserDto> saveUserByAdmin(UserRegisterDTO user) {
        notificationService.subscribeUser(user.email());
        return new ResponseEntity<>(userService.registerUserByAdmin(user), HttpStatus.CREATED);
    }

    @GetMapping("/favorite-songs")
    public ResponseEntity<List<SongResponseDto>> getFavoriteSongs(){
        //Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //String userId = authentication.getName();
        //obtener id-->
        return new ResponseEntity<>(userService.favoriteSongs(1), HttpStatus.OK);
    }

    @DeleteMapping("/favorite-songs/{idSong}")
    public ResponseEntity<Void> deleteFavoriteSong(@PathVariable(value = "idSong") Integer id) {
        userService.deleteSongOfListFavoriteSongs(1, id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/favorite-songs/{idSong}")
    public ResponseEntity<Void> addFavoriteSong(@PathVariable(value = "idSong") Integer id) {
        userService.addSongtoListFavoriteSongs(1, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
