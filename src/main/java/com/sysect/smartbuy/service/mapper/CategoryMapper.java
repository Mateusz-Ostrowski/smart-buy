package com.sysect.smartbuy.service.mapper;

import com.sysect.smartbuy.domain.Category;
import com.sysect.smartbuy.service.dto.CategoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Category} and its DTO {@link CategoryDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CategoryMapper extends EntityMapper<CategoryDTO, Category> {
    @Mapping(target = "parentCategory", source = "parentCategory", qualifiedByName = "id")
    CategoryDTO toDto(Category s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CategoryDTO toDtoId(Category category);
}
