package com.sysect.smartbuy.service.impl;

import com.sysect.smartbuy.domain.Cart;
import com.sysect.smartbuy.domain.CartItem;
import com.sysect.smartbuy.domain.Product;
import com.sysect.smartbuy.repository.CartItemRepository;
import com.sysect.smartbuy.repository.CartRepository;
import com.sysect.smartbuy.repository.ProductRepository;
import com.sysect.smartbuy.repository.UserRepository;
import com.sysect.smartbuy.security.SecurityUtils;
import com.sysect.smartbuy.service.CartService;
import com.sysect.smartbuy.service.dto.CartDTO;
import com.sysect.smartbuy.service.mapper.CartMapper;

import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.sysect.smartbuy.web.rest.vm.AddItemToCartVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

/**
 * Service Implementation for managing {@link Cart}.
 */
@Service
@Transactional
public class CartServiceImpl implements CartService {

    private final Logger log = LoggerFactory.getLogger(CartServiceImpl.class);

    private final CartRepository cartRepository;

    private final CartMapper cartMapper;

    private final CartItemRepository cartItemRepository;

    private final ProductRepository productRepository;

    public CartServiceImpl(CartRepository cartRepository, CartMapper cartMapper, CartItemRepository cartItemRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.cartMapper = cartMapper;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
    }

    @Override
    public CartDTO save(CartDTO cartDTO) {
        log.debug("Request to save Cart : {}", cartDTO);
        Cart cart = cartMapper.toEntity(cartDTO);
        cart = cartRepository.save(cart);
        return cartMapper.toDto(cart);
    }

    @Override
    public Optional<CartDTO> partialUpdate(CartDTO cartDTO) {
        log.debug("Request to partially update Cart : {}", cartDTO);

        return cartRepository
            .findById(cartDTO.getId())
            .map(existingCart -> {
                cartMapper.partialUpdate(existingCart, cartDTO);

                return existingCart;
            })
            .map(cartRepository::save)
            .map(cartMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CartDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Carts");
        return cartRepository.findAll(pageable).map(cartMapper::toDto);
    }

    /**
     *  Get all the carts where Customer is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CartDTO> findAllWhereCustomerIsNull() {
        log.debug("Request to get all carts where Customer is null");
        return StreamSupport
            .stream(cartRepository.findAll().spliterator(), false)
            .filter(cart -> cart.getCustomer() == null)
            .map(cartMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CartDTO> findOne(Long id) {
        log.debug("Request to get Cart : {}", id);
        return cartRepository.findById(id).map(cartMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Cart : {}", id);
        cartRepository.deleteById(id);
    }

    @Transactional
    @Override
    public void addItemToCart(AddItemToCartVM addItemToCartVM){
        Product product = productRepository.findById(addItemToCartVM.getProductId())
            .orElseThrow(() -> new EntityNotFoundException("Product with "+addItemToCartVM.getProductId()+" not found!"));

        Cart cart = cartRepository.findCartByCustomer_User_Login(SecurityUtils.getCurrentUserLogin()
            .orElseThrow(() -> new SecurityException("User with given login not found!")))
            .orElseThrow(() -> new EntityNotFoundException("Cart with given user not found!"));

        if(addItemToCartVM.getQuantity() > product.getQuantity()){
            throw new IllegalArgumentException("Quantity cannot be higher than product quantity");
        }

        Optional<CartItem> cartItemOptional = cartItemRepository.getOneByCartAndProduct(cart,product);
        CartItem cartItem;

        if(cartItemOptional.isEmpty()) {
            cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setPrice(product.getPrice());
            cartItem.setQuantity(addItemToCartVM.getQuantity());

            cartItem.setCreatedAt(Instant.now());
            cartItem.setUpdatedAt(Instant.now());

            cartItem.setCart(cart);
        }else{
            cartItem = cartItemOptional.get();
            cartItem.setQuantity(cartItem.getQuantity() + addItemToCartVM.getQuantity());
            if(cartItem.getQuantity() > product.getQuantity()){
                throw new IllegalArgumentException("Quantity cannot be higher than product quantity");
            }
        }

        cartItemRepository.saveAndFlush(cartItem);
    }


}
