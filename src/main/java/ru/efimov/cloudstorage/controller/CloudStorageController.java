package ru.efimov.cloudstorage.controller;

import lombok.AllArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.efimov.cloudstorage.service.CloudStorageService;

import java.io.IOException;

@RestController
@AllArgsConstructor
public class CloudStorageController {
    private CloudStorageService storageService;

        @PostMapping("/file")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        storageService.uploadFile(file);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/file")
    public ResponseEntity<?> deleteFile(@RequestParam("filename") String filename) {
        storageService.deleteFile(filename);
        return ResponseEntity.ok(HttpStatus.OK);
    }

         @PutMapping(value = "/file")
    public ResponseEntity<?> editFileName(@RequestParam("filename") String filename,
                                          @RequestBody String editFileName) {
        storageService.editFileName(filename, editFileName);
        return ResponseEntity.ok(HttpStatus.OK);
    }
    @GetMapping("/file")
    public ResponseEntity<Resource> downloadFile(@RequestParam("filename") String filename) {
        byte[] file = storageService.downloadFile(filename);
        return ResponseEntity.ok().body(new ByteArrayResource(file));
    }

    @GetMapping("/list")
    public String getAllFiles(@RequestParam("limit") int limit) {
        return storageService.getFileList(limit);
    }

}