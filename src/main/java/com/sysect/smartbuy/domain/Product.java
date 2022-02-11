package com.sysect.smartbuy.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sysect.smartbuy.domain.enumeration.ProductStatus;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Product.
 */
@Entity
@Table(name = "product")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private Float price;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "discount_price")
    private Float discountPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ProductStatus status;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @ManyToOne
    @JsonIgnoreProperties(value = { "parentCategory", "products", "subcategories" }, allowSetters = true)
    private Category category;

    @OneToMany(mappedBy = "imageOf")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "imageOf" }, allowSetters = true)
    private Set<FileInfo> images = new HashSet<>();

    @OneToMany(mappedBy = "product")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "product", "customer" }, allowSetters = true)
    private Set<ProductReview> reviews = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Product id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Product name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getPrice() {
        return this.price;
    }

    public Product price(Float price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public Product quantity(Integer quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Float getDiscountPrice() {
        return this.discountPrice;
    }

    public Product discountPercent(Float discountPercent) {
        this.setDiscountPrice(discountPercent);
        return this;
    }

    public void setDiscountPrice(Float discountPercent) {
        this.discountPrice = discountPercent;
    }

    public ProductStatus getStatus() {
        return this.status;
    }

    public Product status(ProductStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Product createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public Product updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Category getCategory() {
        return this.category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Product category(Category category) {
        this.setCategory(category);
        return this;
    }

    public Set<FileInfo> getImages() {
        return this.images;
    }

    public void setImages(Set<FileInfo> fileInfos) {
        if (this.images != null) {
            this.images.forEach(i -> i.setImageOf(null));
        }
        if (fileInfos != null) {
            fileInfos.forEach(i -> i.setImageOf(this));
        }
        this.images = fileInfos;
    }

    public Product images(Set<FileInfo> fileInfos) {
        this.setImages(fileInfos);
        return this;
    }

    public Product addImages(FileInfo fileInfo) {
        this.images.add(fileInfo);
        fileInfo.setImageOf(this);
        return this;
    }

    public Product removeImages(FileInfo fileInfo) {
        this.images.remove(fileInfo);
        fileInfo.setImageOf(null);
        return this;
    }

    public Set<ProductReview> getReviews() {
        return this.reviews;
    }

    public void setReviews(Set<ProductReview> productReviews) {
        if (this.reviews != null) {
            this.reviews.forEach(i -> i.setProduct(null));
        }
        if (productReviews != null) {
            productReviews.forEach(i -> i.setProduct(this));
        }
        this.reviews = productReviews;
    }

    public Product reviews(Set<ProductReview> productReviews) {
        this.setReviews(productReviews);
        return this;
    }

    public Product addReviews(ProductReview productReview) {
        this.reviews.add(productReview);
        productReview.setProduct(this);
        return this;
    }

    public Product removeReviews(ProductReview productReview) {
        this.reviews.remove(productReview);
        productReview.setProduct(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product)) {
            return false;
        }
        return id != null && id.equals(((Product) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Product{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", price=" + getPrice() +
            ", quantity=" + getQuantity() +
            ", discountPercent=" + getDiscountPrice() +
            ", status='" + getStatus() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
