package com.example.spotify.service;

import com.example.spotify.dto.request.AuthUserDto;
import com.example.spotify.dto.request.UserDto;
import com.example.spotify.dto.request.UserRegisterDTO;
import com.example.spotify.dto.response.JwtResponseDto;
import com.example.spotify.dto.response.ResponseUserDto;
import com.example.spotify.dto.response.SongResponseDto;
import com.example.spotify.entity.Song;
import com.example.spotify.entity.User;
import com.example.spotify.exception.SongNotFoundException;
import com.example.spotify.exception.UserNotFoundException;
import com.example.spotify.mapper.SongMapper;
import com.example.spotify.mapper.UserMapper;
import com.example.spotify.repository.SongRepository;
import com.example.spotify.repository.UserRepository;
import com.example.spotify.config.security.Role;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final SongRepository songRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    private final SongMapper songMapper = Mappers.getMapper(SongMapper.class);

    public void deleteUser(Integer idUser) {
        userRepository.deleteById(idUser);
    }

    public UserDto getUserById(Integer id) {
        return userRepository.findById(id)
                .map( user -> userMapper.toUserDto(user))
        .orElseThrow(()-> new UserNotFoundException("User with id: "+ "wasn't found"));
    }


    public List<SongResponseDto> favoriteSongs(Integer idUser) {
        return userRepository.findById(idUser)
                .map(user-> songMapper.toSongsResponseDto(user.getFavoriteSongs()))
                .orElseThrow(()-> new UserNotFoundException("User with id: "+ "wasn't found"));
    }

    public void deleteSongOfListFavoriteSongs(Integer idUser, Integer idSong) {
        User userFound = userRepository.findById(idUser).orElseThrow(()-> new UserNotFoundException("User with id "+ idUser+ "wasn't found "));
        Song song = songRepository.findById(idSong).orElseThrow(()-> new SongNotFoundException("Song with id "+ idSong+ "wasn't found"));
        userFound.getFavoriteSongs().remove(song);
        userRepository.save(userFound);
    }

    public void addSongtoListFavoriteSongs(Integer idUser, Integer idSong) {
        User userFound = userRepository.findById(idUser).orElseThrow(()-> new UserNotFoundException("User with id "+ idUser+ "wasn't found "));
        Song song = songRepository.findById(idSong).orElseThrow(()-> new SongNotFoundException("Song with id "+ idSong+ "wasn't found"));
        userFound.getFavoriteSongs().add(song);
        userRepository.save(userFound);

    }

    public Page<User> getAll(int offset, int pageSize, String field) {
        return userRepository.findAll(PageRequest.of(offset, pageSize).withSort(Sort.by(Sort.Direction.ASC, field)));
    }




}
