package com.example.cloudfide_igor_barcikowski_task.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import com.example.cloudfide_igor_barcikowski_task.producer.Producer;
import com.example.cloudfide_igor_barcikowski_task.productattributes.ProductAttribute;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "description")
    private String description;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producer_id", foreignKey = @ForeignKey(name = "product_producer_id_fk"))
    private Producer producer;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<ProductAttribute>  productAttributes =  new HashSet<>();


    @PrePersist
    void prePersist() {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
    }

    public Product(String productName, String description, int quantity, Set<ProductAttribute> productAttributes, BigDecimal price) {
        this.productName = productName;
        this.description = description;
        this.quantity = quantity;
        this.productAttributes = productAttributes;
        this.price = price;
    }

    public Product() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Producer getProducer() {
        return producer;
    }

    public void setProducer(Producer producer) {
        this.producer = producer;
    }

    public Set<ProductAttribute> getProductAttributes() {
        return productAttributes;
    }

    public void setProductAttributes(Set<ProductAttribute> productAttributes) {
        this.productAttributes = productAttributes;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public void addProductAttribute(ProductAttribute attribute) {
        this.productAttributes.add(attribute);
        attribute.setProduct(this);
    }

    public void addProductAttributes(Set<ProductAttribute> productAttributes) {
        for (ProductAttribute attribute : productAttributes) {
            addProductAttribute(attribute);
            attribute.setProduct(this);
        }
    }

    public void removeProductAttribute(ProductAttribute attribute) {
        productAttributes.remove(attribute);
        attribute.setProduct(null);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
