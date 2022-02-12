package com.sysect.smartbuy.service.mapper;

import com.sysect.smartbuy.domain.FileInfo;
import com.sysect.smartbuy.domain.Product;
import com.sysect.smartbuy.service.dto.CategoryDTO;
import com.sysect.smartbuy.service.dto.FileInfoDTO;
import com.sysect.smartbuy.service.dto.ProductDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Product} and its DTO {@link ProductDTO}.
 */
@Mapper(componentModel = "spring", uses = { CategoryMapper.class })
public interface ProductMapper extends EntityMapper<ProductDTO, Product> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "discountPrice", source = "discountPrice")
    ProductDTO toDtoId(Product product);

    @Named("images")
    default ProductDTO toDto(Product product) {
        ProductDTO productDTO = new ProductDTO();
        if (product != null) {
            productDTO.setId(product.getId());
            productDTO.setName(product.getName());
            productDTO.setQuantity(product.getQuantity());
            productDTO.setPrice(product.getPrice());
            productDTO.setDiscountPrice(product.getDiscountPrice());
            productDTO.setStatus(product.getStatus());
            productDTO.setCreatedAt(product.getCreatedAt());
            productDTO.setUpdatedAt(product.getUpdatedAt());

            CategoryDTO categoryDTO = new CategoryDTO();
            categoryDTO.setId(product.getCategory().getId());
            categoryDTO.setName(product.getCategory().getName());
            categoryDTO.setOrder(product.getCategory().getOrder());
            productDTO.setCategory(categoryDTO);
            for (FileInfo fileInfo : product.getImages()) {
                FileInfoDTO fileInfoDTO = new FileInfoDTO();
                fileInfoDTO.setId(fileInfo.getId());
                fileInfoDTO.setFileName(fileInfo.getFileName());
                productDTO.getImages().add(fileInfoDTO);
            }
        }
        return productDTO;
    }
}
