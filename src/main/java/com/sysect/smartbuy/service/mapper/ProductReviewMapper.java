package com.sysect.smartbuy.service.mapper;

import com.sysect.smartbuy.domain.ProductReview;
import com.sysect.smartbuy.service.dto.ProductReviewDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProductReview} and its DTO {@link ProductReviewDTO}.
 */
@Mapper(componentModel = "spring", uses = { ProductMapper.class, CustomerMapper.class })
public interface ProductReviewMapper extends EntityMapper<ProductReviewDTO, ProductReview> {
    @Mapping(target = "product", source = "product", qualifiedByName = "id")
    @Mapping(target = "customer", source = "customer", qualifiedByName = "id")
    ProductReviewDTO toDto(ProductReview s);
}
