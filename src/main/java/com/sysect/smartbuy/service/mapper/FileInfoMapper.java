package com.sysect.smartbuy.service.mapper;

import com.sysect.smartbuy.domain.FileInfo;
import com.sysect.smartbuy.service.dto.FileInfoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FileInfo} and its DTO {@link FileInfoDTO}.
 */
@Mapper(componentModel = "spring", uses = { ProductMapper.class })
public interface FileInfoMapper extends EntityMapper<FileInfoDTO, FileInfo> {
    @Mapping(target = "imageOf", source = "imageOf", qualifiedByName = "id")
    FileInfoDTO toDto(FileInfo s);
}
