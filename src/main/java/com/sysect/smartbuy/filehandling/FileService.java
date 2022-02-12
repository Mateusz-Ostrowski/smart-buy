package com.sysect.smartbuy.filehandling;

import com.sysect.smartbuy.domain.FileInfo;
import com.sysect.smartbuy.repository.FileInfoRepository;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.testcontainers.shaded.org.apache.commons.io.FilenameUtils;

@Service
public class FileService {

    private final FileInfoRepository fileInfoRepository;
    private final FileRepository fileRepository;

    public FileService(FileInfoRepository fileInfoRepository, FileRepository fileRepository) {
        this.fileInfoRepository = fileInfoRepository;
        this.fileRepository = fileRepository;
    }

    public FileInfo uploadFile(MultipartFile file) {
        FileInfo fileInfo = new FileInfo();
        fileInfo.setCreatedAt(Instant.now());
        fileInfo.setOriginalFileName(file.getOriginalFilename());
        fileInfo.setFileName(UUID.randomUUID() + "." + FilenameUtils.getExtension(file.getOriginalFilename()));
        fileInfo.setFileSize(file.getSize());
        try {
            fileRepository.saveNewFile(file, fileInfo.getFileName());
            fileInfoRepository.saveAndFlush(fileInfo);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return fileInfo;
    }

    @Transactional
    public void markFileToRemove(FileInfo fileInfo) {
        fileInfo.setRemoveFlag(true);
        fileInfoRepository.saveAndFlush(fileInfo);
    }

    @Transactional
    public void markFileToRemove(Iterable<FileInfo> fileInfos) {
        for (FileInfo fileInfo : fileInfos) {
            markFileToRemove(fileInfo);
        }
    }

    //    @Scheduled(cron = "0 0 3 * * ?")
    @Transactional
    public void removeFilesToRemove() throws IOException {
        List<FileInfo> fileInfos = fileInfoRepository.findAllByRemoveFlagIsTrue();
        for (FileInfo fileInfo : fileInfos) {
            fileRepository.removeFile(fileInfo.getFileName());
            fileInfoRepository.deleteById(fileInfo.getId());
        }
    }
}
