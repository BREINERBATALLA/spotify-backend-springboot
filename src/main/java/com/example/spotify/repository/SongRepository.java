package com.example.spotify.repository;

import com.example.spotify.dto.request.SongDto;
import com.example.spotify.entity.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SongRepository extends JpaRepository<Song, Integer> {

     List<Song> findAllByGenre(String genre);

     @Query("select distinct s.genre from Song s ")
     List<String> getAllGenre();
}
