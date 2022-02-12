package com.sysect.smartbuy.filehandling;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import org.springframework.web.multipart.MultipartFile;

public class LocalFilesystemRepository implements FileRepository {

    private final Path root;

    public LocalFilesystemRepository(Path root) {
        Objects.requireNonNull(root, "Path must be set in " + this.getClass().getName());
        this.root = root;
    }

    @Override
    public void saveNewFile(MultipartFile file, String fileName) throws IOException {
        Files.copy(file.getInputStream(), root.resolve(fileName));
    }

    @Override
    public File findFileByLocationAndName(String fileName) {
        final Path file = root.resolve(fileName);
        return file.toFile();
    }

    @Override
    public void removeFile(String fileName) throws IOException {
        Files.deleteIfExists(root.resolve(fileName));
    }
}
