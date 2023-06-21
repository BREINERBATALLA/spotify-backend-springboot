package com.example.spotify.dto.response;

import org.springframework.web.multipart.MultipartFile;

public record SongResponseDto(Integer id, String nameSong, String artistName, String genre, Long duration, String fileName) {
}
