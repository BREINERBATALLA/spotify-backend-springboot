package com.example.spotify.service;

import com.example.spotify.dto.request.UserDto;
import com.example.spotify.dto.request.UserRegisterDTO;
import com.example.spotify.dto.response.ResponseUserDto;
import com.example.spotify.dto.response.SongResponseDto;
import com.example.spotify.entity.User;
import com.example.spotify.exception.UserNotFoundException;
import com.example.spotify.mapper.SongMapper;
import com.example.spotify.mapper.UserMapper;
import com.example.spotify.repository.UserRepository;
import com.example.spotify.security.Role;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    private final SongMapper songMapper = Mappers.getMapper(SongMapper.class);

    //este va en Login al igual que log-in y log-out.
    public ResponseUserDto registerUser(UserRegisterDTO user)  {
        User newUser = new User();
        newUser.setEmail(user.email());
        newUser.setUsername(user.username());
        String passwordGenerated = generateRandomPassword(10);
        newUser.setPassword(passwordGenerated);
        newUser.setRole(Role.USER);
        userRepository.save(newUser);
        return new ResponseUserDto(passwordGenerated);
    }

    public ResponseUserDto registerUserByAdmin(UserRegisterDTO userDTO) {
        User newUser = new User();
        newUser.setEmail(userDTO.email());
        newUser.setUsername(userDTO.username());
        String passwordGenerated = generateRandomPassword(12);
        newUser.setPassword(passwordGenerated);
        newUser.setRole(Role.ADMIN);
        userRepository.save(newUser);
        return new ResponseUserDto(passwordGenerated);
    }


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

    public void deleteSongOfListFavoriteSongs(Integer idSong) {

    }

    public void addSongtoListFavoriteSongs(Integer idSong) {

    }

    public Page<User> getAll(int offset, int pageSize, String field) {
        return userRepository.findAll(PageRequest.of(offset, pageSize).withSort(Sort.by(Sort.Direction.ASC, field)));
    }

    // Método para generar una contraseña alfanumérica aleatoria de una longitud específica
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
