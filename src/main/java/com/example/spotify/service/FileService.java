package com.example.spotify.service;

import com.example.spotify.dto.response.FileDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

@RequiredArgsConstructor
@Service
@Slf4j
public class FileService {

    private final S3Client s3Client;
    @Value("${aws.bucket-name}")
    private String bucketName;

    public FileDto uploadAudio(MultipartFile file) {
        String nameFile = LocalDate.now().toString()+file.getOriginalFilename();

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(nameFile)
                .build();
        try {
            s3Client.putObject(request, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        File tempFile;
        try {
            tempFile = File.createTempFile("temp", ".mp3");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Guarda el contenido del MultipartFile en el archivo temporal
        try {
            file.transferTo(tempFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Long duration = 0L;

        try {
            AudioFile audioFile = AudioFileIO.read(tempFile);
            duration = Integer.toUnsignedLong(audioFile.getAudioHeader().getTrackLength());
            tempFile.delete();
            System.out.println("Duration (seconds): " + duration);
        } catch (CannotReadException | IOException | TagException | ReadOnlyFileException | InvalidAudioFrameException e) {
            e.printStackTrace();
        }

        log.info("Pruebaa na ma");
        return new FileDto(nameFile, duration);
    }

    public void deleteAudio(String fileName) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

        DeleteObjectResponse deleteObjectResponse = s3Client.deleteObject(deleteObjectRequest);

        /** Verificar si la eliminación fue exitosa
        if (deleteObjectResponse.sdkHttpResponse().isSuccessful()) {
            return "El archivo de audio se eliminó correctamente";
        } else {
            return "No se pudo eliminar el archivo de audio";
        }
         */

    }


}
