package com.sysect.smartbuy.repository;

import com.sysect.smartbuy.domain.CartItem;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data SQL repository for the CartItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @Query("SELECT ci FROM CartItem ci WHERE ci.cart.customer.user.login = :login")
    List<CartItem> getAllByLogin(@Param("login") String login);
}
