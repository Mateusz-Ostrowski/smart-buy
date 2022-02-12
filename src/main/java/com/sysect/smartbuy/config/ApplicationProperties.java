package com.sysect.smartbuy.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Smart Buy.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 * See {@link tech.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private String localFileSystemRootFolder;

    public String getLocalFileSystemRootFolder() {
        return localFileSystemRootFolder;
    }

    public void setLocalFileSystemRootFolder(String localFileSystemRootFolder) {
        this.localFileSystemRootFolder = localFileSystemRootFolder;
    }
}
