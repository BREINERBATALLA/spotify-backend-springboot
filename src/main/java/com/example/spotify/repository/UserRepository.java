package com.example.spotify.repository;

import com.example.spotify.entity.Song;
import com.example.spotify.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    Song findByEmail(String email);
}
