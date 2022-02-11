package com.sysect.smartbuy.service.mapper;

import com.sysect.smartbuy.domain.Product;
import com.sysect.smartbuy.service.dto.ProductDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Product} and its DTO {@link ProductDTO}.
 */
@Mapper(componentModel = "spring", uses = { CategoryMapper.class })
public interface ProductMapper extends EntityMapper<ProductDTO, Product> {
    @Mapping(target = "category", source = "category", qualifiedByName = "id")
    ProductDTO toDto(Product s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "discountPrice", source = "discountPrice")

    ProductDTO toDtoId(Product product);
}
