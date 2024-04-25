package ru.efimov.cloudstorage.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.efimov.cloudstorage.CloudStorageApplicationTests;
import ru.efimov.cloudstorage.repository.UserRepository;
import ru.efimov.cloudstorage.service.CloudStorageService;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.efimov.cloudstorage.TestData.*;

public class CloudStorageControllerTest extends CloudStorageApplicationTests {
    private static final String FILE_ENDPOINT = "/file";
    private static final String LIST_FILE_ENDPOINT = "/list";

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserRepository userRepository;
    @Autowired
    CloudStorageService storageService;

    @Test
    @WithMockUser(username = USERNAME)
    void getAllFiles() throws Exception {

        var expectedArray = FILENAME.split("/n");
        var result = mockMvc.perform(get(LIST_FILE_ENDPOINT).param(LIMIT_PARAM, String.valueOf(2)))
                .andExpect(status().isOk())
                .andReturn();
        var responseBody = result.getResponse().getContentAsString().split("/n");

        assertEquals(responseBody.length, expectedArray.length);
        assertEquals(responseBody[0], expectedArray[0]);
    }

    @Test
    @WithMockUser(username = USERNAME)
    void downloadFile() throws Exception {

        byte[] bytes = FILE_CONTENT.getBytes();

        var result = mockMvc.perform(get(FILE_ENDPOINT).param(FILENAME_PARAM, FILENAME))
                .andExpect(status().isOk())
                .andReturn();
        var resultBytes = result.getResponse().getContentAsByteArray();

        assertArrayEquals(resultBytes, bytes);
    }

    @Test
    @WithMockUser(username = USERNAME)
    void deleteFile() throws Exception {

        mockMvc.perform(delete(FILE_ENDPOINT).param(FILENAME_PARAM, FILENAME))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = USERNAME)
    void editFileName() throws Exception {

        var body = objectMapper.writeValueAsString(FILENAME_2);

        mockMvc.perform(put(FILE_ENDPOINT)
                        .param(FILENAME_PARAM, FILENAME)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
