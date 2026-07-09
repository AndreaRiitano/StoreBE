package org.esamepsw.store.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "purchase")
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;


    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "purchase_time", nullable = false)
    private Date purchaseTime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user_id;

    @OneToMany(mappedBy = "purchase",  cascade = CascadeType.MERGE)
    private List<ProductInPurchase> productInPurchase = new ArrayList<ProductInPurchase>();

    @Version
    private long version;


}
