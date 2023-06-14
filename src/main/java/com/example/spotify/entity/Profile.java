package com.example.spotify.entity;

import jakarta.persistence.*;

import java.util.List;

@Table(name = "profile")
@Entity
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToMany()
    @JoinColumn(name = "song_id")
    private List<Song> listFavoriteSongs;

}
