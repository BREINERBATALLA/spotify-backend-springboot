package com.example.spotify.mapper;

import com.example.spotify.dto.request.SongDto;
import com.example.spotify.dto.response.SongResponseDto;
import com.example.spotify.entity.Song;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface SongMapper {


    Song songDtoToSong(SongDto songDTO);

    @Mapping(target = "file", ignore = true)
    SongDto songToSongDto(Song song);

    //List<SongDto> toSongsDto(List<Song> songList);

    SongResponseDto toSongResponseDto(Song song);

    List<SongResponseDto> toSongsResponseDto(List<Song> songList);
}
