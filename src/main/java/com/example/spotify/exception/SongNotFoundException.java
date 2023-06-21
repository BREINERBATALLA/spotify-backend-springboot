package com.example.spotify.exception;

import com.example.spotify.entity.Song;

public class SongNotFoundException extends RuntimeException{

    public SongNotFoundException(String message) {
        super(message);
    }
}
