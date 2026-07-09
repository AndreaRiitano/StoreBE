package org.esamepsw.store.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, length = 60)
    private String name;

    @Column(name = "category", nullable = false, length = 50)
    private String category;

    @Column(name = "description", nullable = false, length = 500)
    private String description;

    @Column(name = "price", nullable = false)
    private double price;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @OneToMany(targetEntity = ProductInPurchase.class, mappedBy = "product", cascade = CascadeType.MERGE)
    @JsonIgnore
    private List<ProductInPurchase> productInPurchase = new ArrayList<ProductInPurchase>();

    @Version
    private long version;


}
