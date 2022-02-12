package com.sysect.smartbuy.service.mapper;

import com.sysect.smartbuy.domain.CartItem;
import com.sysect.smartbuy.service.dto.CartItemDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CartItem} and its DTO {@link CartItemDTO}.
 */
@Mapper(componentModel = "spring", uses = { ProductMapper.class, CartMapper.class })
public interface CartItemMapper extends EntityMapper<CartItemDTO, CartItem> {
    @Mapping(target = "prod", source = "product", qualifiedByName = "images")
    @Mapping(target = "cart", source = "cart", qualifiedByName = "id")
    CartItemDTO toDto(CartItem s);
}
