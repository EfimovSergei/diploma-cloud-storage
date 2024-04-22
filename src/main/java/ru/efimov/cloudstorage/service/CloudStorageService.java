package ru.efimov.cloudstorage.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;
import ru.efimov.cloudstorage.entity.Storage;
import ru.efimov.cloudstorage.entity.User;
import ru.efimov.cloudstorage.exception.InputDataException;
import ru.efimov.cloudstorage.exception.InternalServerException;
import ru.efimov.cloudstorage.repository.CloudStorageRepository;

import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CloudStorageService {

    private final CloudStorageRepository storageRepository;

    public byte[] downloadFile(String filename, User user) {
        var file = getFileByFilename(filename, user);
        log.info(String.format("File %s downloaded", filename));
        return file.getFileContent();
    }

    public void uploadFile(String filename, byte[] file, User user) {
        Storage storage = new Storage(filename, file, user);
        storageRepository.save(storage);
        log.info(String.format("File %s uploaded", filename));
    }

    public void deleteFile(String filename, User user) {
        storageRepository.deleteByUserAndFileName(user, filename);
        log.info(String.format("File %s deleted", filename));
    }

    public String getFileList(int limit, User user) {
        try {
            var storages = storageRepository.findAllByUser(user, Limit.of(limit));
            log.info("Files showed");
            return storages.stream().map(Storage::getFileName).collect(Collectors.joining("\n"));
        } catch (Exception e) {
            log.error("Error getting file list");
            throw new InternalServerException("Error getting file list", e.getMessage());
        }
    }

    public void editFileName(String filename, String editFileName, User user) {
        try {
            var file = getFileByFilename(filename, user);
            file.setFileName(editFileName);
            storageRepository.save(file);
            log.info("File renamed");
        } catch (Exception e) {
            log.error(String.format("Error edit file %s", filename));
            throw new InternalServerException("Error edit file", e.getMessage());
        }
    }

    private Storage getFileByFilename(String filename, User user) {

        var file = storageRepository.findByUserAndFileName(user, filename);
        if (file.isPresent()) {
            return file.get();
        } else {
            log.error(String.format("File with filename %s is not found", filename));
            throw new InputDataException(String.format("File with filename %s is not found", filename));
        }
    }
}

