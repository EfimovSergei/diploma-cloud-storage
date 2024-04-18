package ru.efimov.cloudstorage.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Limit;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.efimov.cloudstorage.entity.Storage;
import ru.efimov.cloudstorage.entity.User;
import ru.efimov.cloudstorage.exception.InputDataException;
import ru.efimov.cloudstorage.exception.InternalServerException;
import ru.efimov.cloudstorage.repository.CloudStorageRepository;
import ru.efimov.cloudstorage.repository.UserRepository;

import java.io.IOException;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CloudStorageService {
    private final CloudStorageRepository storageRepository;
    private final UserRepository userRepository;

    public byte[] downloadFile(String filename) {
        var file = getFileByFilename(filename);
        return file.getFileContent();
    }

    public void uploadFile(MultipartFile file) {

        try {
            String fileName = file.getOriginalFilename();
            byte[] fileBytes = file.getBytes();
            User user = getUserFromSecurityContext();
            Storage storage = new Storage(fileName, fileBytes, user);
            storageRepository.save(storage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteFile(String filename) {

        User user = getUserFromSecurityContext();
        storageRepository.deleteByUserAndFileName(user, filename);
    }

    public String getFileList(int limit) {

        try {
            User user = getUserFromSecurityContext();
            var storages = storageRepository.findAllByUser(user, Limit.of(limit));
            return storages.stream().map(Storage::getFileName).collect(Collectors.joining("\n"));
        } catch (Exception e) {
            throw new InternalServerException("Error getting file list", e.getMessage());
        }
    }

    public void editFileName(String filename, String editFileName) {
        try {
            var file = getFileByFilename(filename);
            file.setFileName(editFileName);
            storageRepository.save(file);
        } catch (InputDataException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalServerException("Error edit file", e.getMessage());
        }
    }

    private Storage getFileByFilename(String filename) {
        var user = getUserFromSecurityContext();
        var file = storageRepository.findByUserAndFileName(user, filename);
        if (file.isPresent()) {
            return file.get();
        } else {
            throw new InputDataException(String.format("File with filename %s is not found", filename));
        }
    }

    private User getUserFromSecurityContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var username = authentication.getName();
        var user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new InputDataException(String.format("User with username %s is not found", username));
        }
    }
}

