package com.sysect.smartbuy.service.mapper;

import com.sysect.smartbuy.domain.Cart;
import com.sysect.smartbuy.service.dto.CartDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Cart} and its DTO {@link CartDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CartMapper extends EntityMapper<CartDTO, Cart> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CartDTO toDtoId(Cart cart);
}
