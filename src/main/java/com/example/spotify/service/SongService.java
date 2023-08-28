package com.example.spotify.service;

import com.example.spotify.dto.request.SongDto;
import com.example.spotify.dto.response.FileDto;
import com.example.spotify.dto.response.SongResponseDto;
import com.example.spotify.entity.Song;
import com.example.spotify.exception.SongNotFoundException;
import com.example.spotify.mapper.SongMapper;
import com.example.spotify.repository.SongRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import java.util.List;


@RequiredArgsConstructor
@Service
@Slf4j
public class SongService {

    private final SongRepository songRepository;
    private final SongMapper songMapper = Mappers.getMapper(SongMapper.class);

    private final FileService fileService;

    private final NotificationService notificationService;

    public SongResponseDto create(SongDto songDto) {
        FileDto fileDTO = fileService.uploadAudio(songDto.file());
        Song song = songMapper.songDtoToSong(songDto);
        song.setFileName(fileDTO.fileName());//url ubicacion en el bucket?
        song.setDuration(fileDTO.duration());
        //Obtener usuario logueado para enviar nombre en el subject..
        notificationService.publishMessage("Song: "+ songDto.nameSong() + "\n"+ "By:" + songDto.artistName() ,"Hi "+ "BreiSpotify has uploaded a new song <3" );
        log.info(song.toString());
        return songMapper.toSongResponseDto(songRepository.save(song));

    }

    public SongResponseDto editSong(Integer id, SongDto songDto) {
        return songMapper.toSongResponseDto(songRepository.findById(id)
                .map(song -> {
                    edit(songDto, song, fileService);
                    return songRepository.save(song);
                }).orElseThrow(()-> new SongNotFoundException("Song with id"+ id + "wasn't found")));
    }

    public static void edit(SongDto songDto, Song song, FileService fileService) {
        song.setNameSong(songDto.nameSong());
        song.setGenre(songDto.genre());
        song.setArtistName(songDto.artistName());
        if (!songDto.file().isEmpty()) {
            fileService.deleteAudio(song.getFileName());
            FileDto fileDTO = fileService.uploadAudio(songDto.file());

            song.setFileName(fileDTO.fileName());
            song.setDuration(fileDTO.duration());
        }
    }

    public List<SongResponseDto> getAll() {
        return songMapper.toSongsResponseDto(songRepository.findAll());
    }

    public SongResponseDto getById(Integer id) {
        return songRepository.findById(id)
                .map(songMapper::toSongResponseDto)
                .orElseThrow(()-> new SongNotFoundException("Song with id:" + id+ "wasn't found" ));
    }

    public String delete(Integer id) {
        return songRepository.findById(id)
                .map( song -> {
                    fileService.deleteAudio(song.getFileName());
                    songRepository.deleteById(id);
                    return "Song with id:" + id + "was deleted";
                })
                .orElseThrow(()-> new SongNotFoundException("Song with id:" + id+ "wasn't found" ));
    }

    public List<SongResponseDto> getSongsByGenre(String genre){
        return songMapper.toSongsResponseDto(songRepository.findAllByGenre(genre));
    }

    public List<String> getAllGenreNames() {
        return songRepository.getAllGenre();
    }
}
