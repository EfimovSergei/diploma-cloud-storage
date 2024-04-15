package ru.efimov.cloudstorage.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.efimov.cloudstorage.entity.User;
import ru.efimov.cloudstorage.service.CloudStorageService;
import ru.efimov.cloudstorage.service.MyUserDetailsService;

@RestController
@AllArgsConstructor
public class CloudStorageController {
    private CloudStorageService storageService;
    private MyUserDetailsService userService;

    @PostMapping("/save-user")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        userService.saveUser(user);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/welcome")
    public String cs() {
       return "welcome";
    }

}



//    @GetMapping("/list")
//    public List<FileListResponse> getAllFiles(@RequestHeader("auth-token") String authToken,
//                                              @RequestParam("limit") Integer limit) {
//        return service.getFileList(authToken, limit);
//    }
//
//    @GetMapping("/file")
//    public ResponseEntity<Resource> downloadFile(@RequestHeader("auth-token") String authToken,
//                                                 @RequestParam("filename") String filename) {
//        byte[] file = service.downloadFile(authToken, filename);
//        return ResponseEntity.ok().body(new ByteArrayResource(file));
//    }
//
//    @PostMapping("/file")
//    public ResponseEntity<?> uploadFile(@RequestHeader("auth-token") String authToken,
//                                        @RequestParam("filename") String filename,
//                                        MultipartFile file) throws IOException {
//        service.uploadFile(authToken, filename, file);
//        return ResponseEntity.ok(HttpStatus.OK);
//    }
//
//    @DeleteMapping("/file")
//    public ResponseEntity<?> deleteFile(@RequestHeader("auth-token") String authToken,
//                                        @RequestParam("filename") String filename) {
//        service.deleteFile(authToken, filename);
//        return ResponseEntity.ok(HttpStatus.OK);
//    }

//    @PutMapping(value = "/file")
//    public ResponseEntity<?> editFileName(@RequestHeader("auth-token") String authToken,
//                                          @RequestParam("filename") String filename,
//                                          @RequestBody EditFileNameRequest editFileNameRequest) {
//        service.editFileName(authToken, filename, editFileNameRequest);
//        return ResponseEntity.ok(HttpStatus.OK);
//    }

//}
