package com.sysect.smartbuy.repository;

import com.sysect.smartbuy.domain.ProductReview;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ProductReview entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductReviewRepository extends JpaRepository<ProductReview, Long> {}
