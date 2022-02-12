package com.sysect.smartbuy.filehandling;

import com.sysect.smartbuy.config.ApplicationProperties;
import java.nio.file.Paths;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FileConfiguration {

    @Bean
    FileRepository fileRepository(ApplicationProperties properties) {
        return new LocalFilesystemRepository(Paths.get(properties.getLocalFileSystemRootFolder()));
    }
}
