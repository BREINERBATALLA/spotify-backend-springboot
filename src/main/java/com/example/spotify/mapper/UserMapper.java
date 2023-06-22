package com.example.spotify.mapper;

import com.example.spotify.dto.request.SongDto;
import com.example.spotify.dto.request.UserDto;
import com.example.spotify.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toUserDto(User user);

    @Mapping(target = "authorities", ignore = true)
    User toUser(UserDto userDto);


    List<UserDto> toUsersDto(List<User> userList);
}
