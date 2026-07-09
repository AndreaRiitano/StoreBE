package org.esamepsw.store.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.AnyDiscriminatorImplicitValues;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "first_name", nullable = false, length = 40)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 40)
    private String lastName;

    @Column(name = "email", nullable = false, length = 60)
    private String email;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "address", length = 100)
    private String address;

    @OneToMany(targetEntity = Purchase.class , mappedBy = "user_id")
    @JsonIgnore
    private List<Purchase> purchases= new ArrayList<Purchase>();

}
