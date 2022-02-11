package com.sysect.smartbuy.repository;

import com.sysect.smartbuy.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data SQL repository for the Product entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p FROM Product p WHERE p.status = 'PUBLISHED' AND (:categoryId IS NULL OR :categoryId = p.category.id)")
    Page<Product> findAllByCategoryAndPublishedStatus(@Param("categoryId") Long id, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.status = 'PUBLISHED' AND :productId = p.id")
    Optional<Product> findOneByIdAndPublishedStatus(@Param("productId") Long id);
}
