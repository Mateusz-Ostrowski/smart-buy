package com.sysect.smartbuy.filehandling;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

/**
 * This is class that manages files on filesystem.
 */
public interface FileRepository {
    void saveNewFile(MultipartFile file, String fileName) throws IOException;

    File findFileByLocationAndName(String fileName);

    void removeFile(String fileName) throws IOException;
}
