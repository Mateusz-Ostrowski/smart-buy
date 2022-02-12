package com.sysect.smartbuy.web.rest;

import com.github.dockerjava.api.exception.NotFoundException;
import com.sysect.smartbuy.domain.FileInfo;
import com.sysect.smartbuy.filehandling.FileRepository;
import com.sysect.smartbuy.filehandling.FileService;
import com.sysect.smartbuy.service.FileInfoService;
import com.sysect.smartbuy.service.dto.FileInfoDTO;
import java.io.IOException;
import java.io.InputStream;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.compress.utils.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/api")
public class FileResource {

    private final Logger log = LoggerFactory.getLogger(FileResource.class);

    private static final String ENTITY_NAME = "fileInfo";

    private final FileInfoService fileInfoService;
    private final FileRepository fileRepository;
    private final FileService fileService;

    public FileResource(FileInfoService fileInfoService, FileRepository fileRepository, FileService fileService) {
        this.fileInfoService = fileInfoService;
        this.fileRepository = fileRepository;
        this.fileService = fileService;
    }

    @PostMapping("/upload")
    public ResponseEntity<FileInfo> upload(@RequestParam("file") MultipartFile file) {
        log.debug("REST request to upload file : {}", file.getOriginalFilename());
        FileInfo info = fileService.uploadFile(file);
        return new ResponseEntity<>(info, HttpStatus.ACCEPTED);
    }
}
