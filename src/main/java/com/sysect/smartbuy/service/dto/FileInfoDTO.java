package com.sysect.smartbuy.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.sysect.smartbuy.domain.FileInfo} entity.
 */
public class FileInfoDTO implements Serializable {

    private Long id;

    private String fileName;

    private String originalFileName;

    private Long fileSize;

    private Instant createdAt;

    private Instant updatedAt;

    private ProductDTO imageOf;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public ProductDTO getImageOf() {
        return imageOf;
    }

    public void setImageOf(ProductDTO imageOf) {
        this.imageOf = imageOf;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FileInfoDTO)) {
            return false;
        }

        FileInfoDTO fileInfoDTO = (FileInfoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, fileInfoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FileInfoDTO{" +
            "id=" + getId() +
            ", fileName='" + getFileName() + "'" +
            ", originalFileName='" + getOriginalFileName() + "'" +
            ", fileSize=" + getFileSize() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", imageOf=" + getImageOf() +
            "}";
    }
}
