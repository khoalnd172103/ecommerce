package com.ecommerce.library.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "image"}))
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "cost_price")
    private double costPrice;

    @Column(name = "sale_price")
    private double salePrice;

    @Column(name = "current_quantity")
    private int currentQuantity;

    @Lob
    @Column(columnDefinition = "VARBINARY(MAX)")
    private byte[] image;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id", referencedColumnName = "category_id")
    private Category category;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    @Column(name = "is_activated")
    private boolean isActivated;
}
