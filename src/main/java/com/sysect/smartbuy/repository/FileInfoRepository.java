package com.sysect.smartbuy.repository;

import com.sysect.smartbuy.domain.FileInfo;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the FileInfo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FileInfoRepository extends JpaRepository<FileInfo, Long> {
    List<FileInfo> findAllByRemoveFlagIsTrue();
}
