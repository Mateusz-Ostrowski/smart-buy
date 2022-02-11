package com.sysect.smartbuy.service.mapper;

import com.sysect.smartbuy.domain.Address;
import com.sysect.smartbuy.service.dto.AddressDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Address} and its DTO {@link AddressDTO}.
 */
@Mapper(componentModel = "spring", uses = { CustomerMapper.class })
public interface AddressMapper extends EntityMapper<AddressDTO, Address> {
    @Mapping(target = "customer", source = "customer", qualifiedByName = "id")
    AddressDTO toDto(Address s);
}
