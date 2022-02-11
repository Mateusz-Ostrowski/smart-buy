package com.sysect.smartbuy.repository;

import com.sysect.smartbuy.domain.Cart;
import com.sysect.smartbuy.domain.CartItem;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data SQL repository for the Cart entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findCartByCustomer_User_Login(String login);
}
