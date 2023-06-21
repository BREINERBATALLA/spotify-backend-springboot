package com.example.spotify.entity;

import jakarta.persistence.*;
import lombok.Data;

@Table(name = "song")
@Data
@Entity
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name_song")
    private String nameSong;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "artist_name")
    private String artistName;

    @Column(name = "genre")
    private String genre;

    @Column(name = "duration")
    private Long duration;
}
