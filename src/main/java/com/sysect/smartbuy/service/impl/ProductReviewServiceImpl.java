package com.sysect.smartbuy.service.impl;

import com.sysect.smartbuy.domain.ProductReview;
import com.sysect.smartbuy.repository.ProductReviewRepository;
import com.sysect.smartbuy.service.ProductReviewService;
import com.sysect.smartbuy.service.dto.ProductReviewDTO;
import com.sysect.smartbuy.service.mapper.ProductReviewMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ProductReview}.
 */
@Service
@Transactional
public class ProductReviewServiceImpl implements ProductReviewService {

    private final Logger log = LoggerFactory.getLogger(ProductReviewServiceImpl.class);

    private final ProductReviewRepository productReviewRepository;

    private final ProductReviewMapper productReviewMapper;

    public ProductReviewServiceImpl(ProductReviewRepository productReviewRepository, ProductReviewMapper productReviewMapper) {
        this.productReviewRepository = productReviewRepository;
        this.productReviewMapper = productReviewMapper;
    }

    @Override
    public ProductReviewDTO save(ProductReviewDTO productReviewDTO) {
        log.debug("Request to save ProductReview : {}", productReviewDTO);
        ProductReview productReview = productReviewMapper.toEntity(productReviewDTO);
        productReview = productReviewRepository.save(productReview);
        return productReviewMapper.toDto(productReview);
    }

    @Override
    public Optional<ProductReviewDTO> partialUpdate(ProductReviewDTO productReviewDTO) {
        log.debug("Request to partially update ProductReview : {}", productReviewDTO);

        return productReviewRepository
            .findById(productReviewDTO.getId())
            .map(existingProductReview -> {
                productReviewMapper.partialUpdate(existingProductReview, productReviewDTO);

                return existingProductReview;
            })
            .map(productReviewRepository::save)
            .map(productReviewMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductReviewDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ProductReviews");
        return productReviewRepository.findAll(pageable).map(productReviewMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductReviewDTO> findOne(Long id) {
        log.debug("Request to get ProductReview : {}", id);
        return productReviewRepository.findById(id).map(productReviewMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ProductReview : {}", id);
        productReviewRepository.deleteById(id);
    }
}
