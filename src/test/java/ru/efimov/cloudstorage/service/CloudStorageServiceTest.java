package ru.efimov.cloudstorage.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.efimov.cloudstorage.CloudStorageApplicationTests;
import ru.efimov.cloudstorage.entity.Storage;
import ru.efimov.cloudstorage.repository.CloudStorageRepository;
import ru.efimov.cloudstorage.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;
import static ru.efimov.cloudstorage.TestData.*;

class CloudStorageServiceTest extends CloudStorageApplicationTests {

    @Autowired
    CloudStorageService storageService;
    @Autowired
    CloudStorageRepository storageRepository;
    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        var user = userRepository.findByUsername(USERNAME).get();
        storageRepository.save(
                new Storage(FILENAME, FILENAME.getBytes(), user));
    }

    @Test
    void testDownloadFile() {
        var user = userRepository.findByUsername(USERNAME).get();
        byte[] bytesActual = FILENAME.getBytes();

        var bytesExpected = storageService.downloadFile(FILENAME, user);

        assertTrue(bytesExpected.length > 0);
        assertArrayEquals(bytesExpected, bytesActual);
    }

    @Test
    void testUploadFile() {
        var user = userRepository.findByUsername(USERNAME).get();
        storageService.uploadFile(FILENAME, FILE_CONTENT.getBytes(), user);
    }

    @Test
    void testDeleteFile() {
        var user = userRepository.findByUsername(USERNAME).get();
        storageService.deleteFile(FILENAME, user);

        var storage = storageRepository.findByUserAndFileName(user, FILENAME);

        assertFalse(storage.isPresent());
    }

    @Test
    void testGetFileList() {
        var user = userRepository.findByUsername(USERNAME).get();

        String fileList = storageService.getFileList(2, user);
        assertEquals("test.txt", fileList);
    }

    @Test
    void testEditFileName() {
        var user = userRepository.findByUsername(USERNAME).get();
        storageService.editFileName(FILENAME, "filename", user);

        var storage = storageRepository.findByUserAndFileName(user, "filename");

        assertTrue(storage.isPresent());
        assertEquals(storage.get().getFileName(), "filename");
        assertEquals(storage.get().getUser().getUsername(), user.getUsername());
    }
}
