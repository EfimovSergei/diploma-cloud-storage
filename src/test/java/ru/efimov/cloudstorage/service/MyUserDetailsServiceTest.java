package ru.efimov.cloudstorage.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.efimov.cloudstorage.CloudStorageApplicationTests;
import ru.efimov.cloudstorage.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.efimov.cloudstorage.TestData.USERNAME;

@ExtendWith(MockitoExtension.class)
class MyUserDetailsServiceTest extends CloudStorageApplicationTests {

    @Autowired
    UserRepository userRepository;

    @Autowired
    MyUserDetailsService userDetailsService;

    @Test
    void loadUserByUsername_WhenUserExists_ShouldReturnUserDetails() {

        var user = userRepository.findByUsername(USERNAME);
        var userDetail = userDetailsService.loadUserByUsername(USERNAME);

        assertEquals(userDetail.getUsername(), user.get().getUsername());
        assertEquals(userDetail.getPassword(), user.get().getPassword());
    }

    @Test
    void loadUserByUsernameException() {
        Assertions.assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername("USERNAME_WRONG"));
    }
}
