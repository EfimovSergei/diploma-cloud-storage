package ru.efimov.cloudstorage.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.efimov.cloudstorage.entity.User;
import ru.efimov.cloudstorage.exception.InputDataException;
import ru.efimov.cloudstorage.exception.InternalServerException;
import ru.efimov.cloudstorage.service.CloudStorageService;
import ru.efimov.cloudstorage.service.MyUserDetailsService;

@Slf4j
@RestController
@AllArgsConstructor
public class CloudStorageController {

    private final CloudStorageService storageService;
    private final MyUserDetailsService userDetailsService;

    @PostMapping("/file")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile multipartFile) {
        User user = getUserFromSecurityContext();
        try {
            byte[] file;
            String fileName = multipartFile.getOriginalFilename();
            file = multipartFile.getBytes();
            storageService.uploadFile(fileName, file, user);
            return ResponseEntity.ok(HttpStatus.OK);
        } catch (Exception e) {
            throw new InternalServerException("Error getting file bytes", e.getMessage());
        }
    }

    @DeleteMapping("/file")
    public ResponseEntity<?> deleteFile(@RequestParam("filename") String filename) {
        User user = getUserFromSecurityContext();
        storageService.deleteFile(filename, user);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PutMapping(value = "/file")
    public ResponseEntity<?> editFileName(@RequestParam("filename") String filename,
                                          @RequestBody String editFileName) {
        User user = getUserFromSecurityContext();
        storageService.editFileName(filename, editFileName, user);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/file")
    public ResponseEntity<Resource> downloadFile(@RequestParam("filename") String filename) {
        User user = getUserFromSecurityContext();
        byte[] file = storageService.downloadFile(filename, user);
        return ResponseEntity.ok().body(new ByteArrayResource(file));
    }

    @GetMapping("/list")
    public String getAllFiles(@RequestParam("limit") int limit) {
        User user = getUserFromSecurityContext();
        return storageService.getFileList(limit, user);
    }

    private User getUserFromSecurityContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var username = authentication.getName();
        var user = userDetailsService.loadUserByUsername(username);
        if (user != null) {
            User userResult = new User();
            userResult.setUsername(user.getUsername());
            userResult.setPassword(user.getPassword());
            return userResult;
        } else {
            log.error(String.format("User with username %s is not found", username));
            throw new InputDataException(String.format("User with username %s is not found", username));
        }
    }
}