package com.example.spotify.dto.request;

import org.springframework.web.multipart.MultipartFile;

public record SongDto(Integer id, String nameSong, String artistName, String genre, MultipartFile file, Long duration, String fileName) {
}
