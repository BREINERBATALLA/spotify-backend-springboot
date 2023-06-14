package com.example.spotify.service;

import com.example.spotify.entity.Profile;
import com.example.spotify.entity.User;
import com.example.spotify.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User saveUser(User user)  {

        Profile profile = new Profile();
        user.setProfile(profile);

        return userRepository.save(user);
    }
}
