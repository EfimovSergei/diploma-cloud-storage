package ru.efimov.cloudstorage.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@Service
public class CloudStorageService {

    public byte[] downloadFile(String authToken, String filename) {
        return "hello".getBytes();
    }

    public void uploadFile(String authToken, String filename, MultipartFile file) {
    }

    public void deleteFile(String authToken, String filename) {
    }
}

