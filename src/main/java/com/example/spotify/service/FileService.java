package com.example.spotify.service;

import com.example.spotify.dto.response.FileResponseDTO;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import lombok.RequiredArgsConstructor;
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

@RequiredArgsConstructor
@Service
public class FileService {

    private final S3Client s3Client;
    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    public FileResponseDTO uploadAudio(MultipartFile file) throws IOException, InvalidDataException, UnsupportedTagException {
        String keyS3 = file.getOriginalFilename();

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(keyS3)
                .build();

        s3Client.putObject(request, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

        File tempFile = File.createTempFile("temp", ".mp3");

        // Guarda el contenido del MultipartFile en el archivo temporal
        file.transferTo(tempFile);

        Mp3File mp3File = new Mp3File(tempFile);
        Long duration;

        if(mp3File.hasId3v1Tag()) {
           duration = mp3File.getLengthInSeconds();
        } else{
            duration = 0L;
        }

        return new FileResponseDTO(file.getOriginalFilename(), duration);
    }

    public String deleteAudio(String fileName) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

        DeleteObjectResponse deleteObjectResponse = s3Client.deleteObject(deleteObjectRequest);

        // Verificar si la eliminación fue exitosa
        if (deleteObjectResponse.sdkHttpResponse().isSuccessful()) {
            return "El archivo de audio se eliminó correctamente";
        } else {
            return "No se pudo eliminar el archivo de audio";
        }
    }


}
