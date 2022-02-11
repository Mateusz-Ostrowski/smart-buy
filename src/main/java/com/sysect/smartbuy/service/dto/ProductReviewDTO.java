package com.sysect.smartbuy.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.sysect.smartbuy.domain.ProductReview} entity.
 */
public class ProductReviewDTO implements Serializable {

    private Long id;

    private String text;

    private Integer rating;

    private Instant createdAt;

    private Instant updatedAt;

    private ProductDTO product;

    private CustomerDTO customer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }

    public CustomerDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDTO customer) {
        this.customer = customer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductReviewDTO)) {
            return false;
        }

        ProductReviewDTO productReviewDTO = (ProductReviewDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, productReviewDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductReviewDTO{" +
            "id=" + getId() +
            ", text='" + getText() + "'" +
            ", rating=" + getRating() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", product=" + getProduct() +
            ", customer=" + getCustomer() +
            "}";
    }
}
