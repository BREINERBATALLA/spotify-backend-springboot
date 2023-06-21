package com.example.spotify.rest;

import com.example.spotify.dto.request.SongDto;
import com.example.spotify.dto.response.SongResponseDto;
import com.example.spotify.service.FileService;
import com.example.spotify.service.NotificationService;
import com.example.spotify.service.SongService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/songs")
public class SongController {

    private final SongService songService;
    private final FileService fileService;
    private final NotificationService notificationService;

    @PostMapping(value = "")
    public ResponseEntity<SongResponseDto> uploadSong(@ModelAttribute SongDto songDTO)  {
        return ResponseEntity.status(HttpStatus.CREATED).body(songService.create(songDTO));
    }

    @GetMapping("")
    public ResponseEntity<List<SongResponseDto>> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(songService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SongResponseDto> getSong(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(songService.getById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSongById(@PathVariable("id") Integer id) {
        return  ResponseEntity.ok(songService.delete(id));

    }

    @PutMapping("/{id}")
    public ResponseEntity<SongResponseDto> editSong(@PathVariable("id") Integer id, @ModelAttribute SongDto songUpdate){
        return new ResponseEntity<>(songService.editSong(id,songUpdate), HttpStatus.OK);
    }

    @GetMapping("/genres/{genreName}")
    public ResponseEntity<List<SongResponseDto>> getAllSongsByGenre(@PathVariable(name = "genreName") String genre){
        return new ResponseEntity<>(songService.getSongsByGenre(genre), HttpStatus.OK);
    }

    @GetMapping("/genres")
    public ResponseEntity<List<String>> getAllGenres(){
        return new ResponseEntity<>(songService.getAllGenreNames(), HttpStatus.OK);
    }

}
