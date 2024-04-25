package ru.efimov.cloudstorage.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.efimov.cloudstorage.CloudStorageApplicationTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.efimov.cloudstorage.TestData.USERNAME;

class UserRepositoryTest extends CloudStorageApplicationTests {

    @Autowired
    UserRepository userRepository;

    @Test
    void findByUsername() {
        var user = userRepository.findByUsername(USERNAME);
        assertTrue(user.isPresent());
        assertEquals(user.get().getUsername(), USERNAME);
    }
}