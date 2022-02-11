package com.sysect.smartbuy.service.mapper;

import com.sysect.smartbuy.domain.Customer;
import com.sysect.smartbuy.service.dto.CustomerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Customer} and its DTO {@link CustomerDTO}.
 */
@Mapper(componentModel = "spring", uses = { CartMapper.class })
public interface CustomerMapper extends EntityMapper<CustomerDTO, Customer> {
    @Mapping(target = "shoppingCart", source = "shoppingCart", qualifiedByName = "id")
    CustomerDTO toDto(Customer s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CustomerDTO toDtoId(Customer customer);
}
