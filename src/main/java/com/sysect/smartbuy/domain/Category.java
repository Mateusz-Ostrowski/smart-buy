package com.sysect.smartbuy.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Category.
 */
@Entity
@Table(name = "category")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "jhi_order")
    private Integer order;

    @ManyToOne
    @JsonIgnoreProperties(value = { "parentCategory", "products", "subcategories" }, allowSetters = true)
    private Category parentCategory;

    @OneToMany(mappedBy = "category")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "category", "images", "reviews" }, allowSetters = true)
    private Set<Product> products = new HashSet<>();

    @OneToMany(mappedBy = "parentCategory")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "parentCategory", "products", "subcategories" }, allowSetters = true)
    private Set<Category> subcategories = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Category id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Category name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOrder() {
        return this.order;
    }

    public Category order(Integer order) {
        this.setOrder(order);
        return this;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Category getParentCategory() {
        return this.parentCategory;
    }

    public void setParentCategory(Category category) {
        this.parentCategory = category;
    }

    public Category parentCategory(Category category) {
        this.setParentCategory(category);
        return this;
    }

    public Set<Product> getProducts() {
        return this.products;
    }

    public void setProducts(Set<Product> products) {
        if (this.products != null) {
            this.products.forEach(i -> i.setCategory(null));
        }
        if (products != null) {
            products.forEach(i -> i.setCategory(this));
        }
        this.products = products;
    }

    public Category products(Set<Product> products) {
        this.setProducts(products);
        return this;
    }

    public Category addProducts(Product product) {
        this.products.add(product);
        product.setCategory(this);
        return this;
    }

    public Category removeProducts(Product product) {
        this.products.remove(product);
        product.setCategory(null);
        return this;
    }

    public Set<Category> getSubcategories() {
        return this.subcategories;
    }

    public void setSubcategories(Set<Category> categories) {
        if (this.subcategories != null) {
            this.subcategories.forEach(i -> i.setParentCategory(null));
        }
        if (categories != null) {
            categories.forEach(i -> i.setParentCategory(this));
        }
        this.subcategories = categories;
    }

    public Category subcategories(Set<Category> categories) {
        this.setSubcategories(categories);
        return this;
    }

    public Category addSubcategories(Category category) {
        this.subcategories.add(category);
        category.setParentCategory(this);
        return this;
    }

    public Category removeSubcategories(Category category) {
        this.subcategories.remove(category);
        category.setParentCategory(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Category)) {
            return false;
        }
        return id != null && id.equals(((Category) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Category{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", order=" + getOrder() +
            "}";
    }
}
