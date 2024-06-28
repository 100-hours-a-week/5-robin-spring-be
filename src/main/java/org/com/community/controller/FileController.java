package org.com.community.controller;

import org.com.community.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

@RestController
public class FileController {

    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping("/webapp/{encodedFileName}")
    public ResponseEntity<Resource> serveFile(@PathVariable String encodedFileName) {
        try {
            // 파일명을 디코딩하여 실제 파일명으로 변환
            String decodedFileName = URLDecoder.decode(encodedFileName, StandardCharsets.UTF_8.toString());
            Path filePath = fileStorageService.loadFileAsResource(decodedFileName);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
