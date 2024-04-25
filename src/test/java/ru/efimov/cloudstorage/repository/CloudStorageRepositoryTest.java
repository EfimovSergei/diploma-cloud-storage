package ru.efimov.cloudstorage.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Limit;
import ru.efimov.cloudstorage.CloudStorageApplicationTests;
import ru.efimov.cloudstorage.entity.Storage;

import static org.junit.jupiter.api.Assertions.*;
import static ru.efimov.cloudstorage.TestData.*;

class CloudStorageRepositoryTest extends CloudStorageApplicationTests {

    @Autowired
    CloudStorageRepository storageRepository;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        var user = userRepository.findByUsername(USERNAME).get();
        storageRepository.save(
                new Storage(FILENAME, FILE_CONTENT.getBytes(), user));
    }


    @AfterEach
    void tearDown() {
        storageRepository.deleteAll();
    }

    @Test
    void findAllByUser() {
        var user = userRepository.findByUsername(USERNAME).get();

        var storage = storageRepository.findAllByUser(user, Limit.of(3));
        assertFalse(storage.isEmpty());
        assertEquals(storage.size(), 1);
    }

    @Test
    void findByUserAndFileName() {
        var user = userRepository.findByUsername(USERNAME).get();

        var storage = storageRepository.findByUserAndFileName(user, FILENAME);

        assertTrue(storage.isPresent());
        assertEquals(storage.get().getFileName(), FILENAME);
        assertEquals(storage.get().getUser().getUsername(), user.getUsername());
    }

    @Test
    void deleteByUserAndFileName() {
        var user = userRepository.findByUsername(USERNAME).get();

        storageRepository.deleteByUserAndFileName(user, FILENAME);
        var storage = storageRepository.findByUserAndFileName(user, FILENAME);

        assertFalse(storage.isPresent());
    }
}