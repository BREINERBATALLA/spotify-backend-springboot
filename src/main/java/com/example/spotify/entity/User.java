package com.example.spotify.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Table(name = "user")
@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "rol")
    private String rol;

    @Email(message = "You must to type a valid email")
    @Column(name = "email")
    private String email;

    @Column(name = "fullname")
    private String fullName;

    @NotBlank(message = "This field can't be blank")
    @Column(name = "password")
    private String password;

    @Column(name = "create_At")
    @CreationTimestamp
    private LocalDate createAt;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_id")
    private Profile profile;

}
