package com.example.spotify.entity;

import jakarta.persistence.*;

@Table(name = "song")
@Entity
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name_song")
    private String name;

    @Column(name = "artist_name")
    private String artist;

    @Column(name = "genre")
    private String genre;

    @Column(name = "duration")
    private String duration;






}
