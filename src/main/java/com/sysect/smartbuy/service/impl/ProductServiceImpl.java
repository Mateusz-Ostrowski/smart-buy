package com.sysect.smartbuy.service.impl;

import com.sysect.smartbuy.domain.FileInfo;
import com.sysect.smartbuy.domain.Product;
import com.sysect.smartbuy.repository.FileInfoRepository;
import com.sysect.smartbuy.repository.ProductRepository;
import com.sysect.smartbuy.service.ProductService;
import com.sysect.smartbuy.service.dto.FileInfoDTO;
import com.sysect.smartbuy.service.dto.ProductDTO;
import com.sysect.smartbuy.service.mapper.FileInfoMapper;
import com.sysect.smartbuy.service.mapper.ProductMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Product}.
 */
@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository productRepository;
    private final FileInfoRepository fileInfoRepository;

    private final ProductMapper productMapper;
    private final FileInfoMapper fileInfoMapper;

    public ProductServiceImpl(
        ProductRepository productRepository,
        FileInfoRepository fileInfoRepository,
        ProductMapper productMapper,
        FileInfoMapper fileInfoMapper
    ) {
        this.productRepository = productRepository;
        this.fileInfoRepository = fileInfoRepository;
        this.productMapper = productMapper;
        this.fileInfoMapper = fileInfoMapper;
    }

    @Transactional
    @Override
    public ProductDTO save(ProductDTO productDTO) {
        log.debug("Request to save Product : {}", productDTO);
        Product product = productMapper.toEntity(productDTO);
        for (Long id : productDTO.getFileIds()) {
            product.addImages(fileInfoRepository.findById(id).get());
        }
        product = productRepository.save(product);
        return productMapper.toDto(product);
    }

    @Transactional
    @Override
    public Optional<ProductDTO> partialUpdate(ProductDTO productDTO) {
        log.debug("Request to partially update Product : {}", productDTO);

        return productRepository
            .findById(productDTO.getId())
            .map(existingProduct -> {
                productMapper.partialUpdate(existingProduct, productDTO);
                for (Long id : productDTO.getFileIds()) {
                    existingProduct.addImages(fileInfoRepository.findById(id).get());
                }
                return existingProduct;
            })
            .map(productRepository::save)
            .map(productMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Products");
        return productRepository.findAll(pageable).map(productMapper::toDto);
    }

    @Override
    public Page<ProductDTO> findAllByCategoryIdAndStatusPublished(Long categoryId, Pageable pageable) {
        return productRepository.findAllByCategoryAndPublishedStatus(categoryId, pageable).map(productMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductDTO> findOne(Long id) {
        log.debug("Request to get Product : {}", id);
        return productRepository.findById(id).map(productMapper::toDto);
    }

    @Override
    public Optional<ProductDTO> findOneByIdAndStatusPublished(Long id) {
        return productRepository.findOneByIdAndPublishedStatus(id).map(productMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Product : {}", id);
        productRepository.deleteById(id);
    }
}
