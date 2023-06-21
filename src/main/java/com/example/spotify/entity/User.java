package com.example.spotify.entity;

import com.example.spotify.security.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Table(name = "user")
@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Email(message = "You must to type a valid email")
    @Column(name = "email")
    private String email;

    @Column(name = "fullname")
    private String username;

    @NotBlank(message = "This field can't be blank")
    @Column(name = "password")
    private String password;

    @Column(name = "create_At")
    @CreationTimestamp
    private LocalDate createAt;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToMany
    @JoinTable(name = "user_song",
    joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "song_id", referencedColumnName = "id"))
    private List<Song> favoriteSongs = new ArrayList<>();



}
