package com.sysect.smartbuy.service;

import com.sysect.smartbuy.service.dto.CartDTO;
import java.util.List;
import java.util.Optional;

import com.sysect.smartbuy.service.dto.CartItemDTO;
import com.sysect.smartbuy.web.rest.vm.AddItemToCartVM;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.sysect.smartbuy.domain.Cart}.
 */
public interface CartService {
    /**
     * Save a cart.
     *
     * @param cartDTO the entity to save.
     * @return the persisted entity.
     */
    CartDTO save(CartDTO cartDTO);

    /**
     * Partially updates a cart.
     *
     * @param cartDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CartDTO> partialUpdate(CartDTO cartDTO);

    /**
     * Get all the carts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CartDTO> findAll(Pageable pageable);
    /**
     * Get all the CartDTO where Customer is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<CartDTO> findAllWhereCustomerIsNull();

    /**
     * Get the "id" cart.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CartDTO> findOne(Long id);

    /**
     * Delete the "id" cart.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    void addItemToCart(AddItemToCartVM addItemToCartVM);
}
