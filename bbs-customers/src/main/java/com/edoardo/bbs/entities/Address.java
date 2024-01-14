package com.edoardo.bbs.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Entity @Table(name = "Addresses") @Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Address implements Serializable {
    @Serial
    private static final long serialVersionUID = 2L;

    @Id @Column(name = "id") @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "country", nullable = false, length = 30)
    private String country;

    @Column(name = "state", nullable = false, length = 30)
    private String state;

    @Column(name = "city", nullable = false, length = 30)
    private String city;

    @Column(name = "street", nullable = false, length = 30)
    private String street;

    @Column(name = "number", nullable = false)
    private Integer streetNumber;

    @Column(name = "postal_code", nullable = false, length = 20)
    private String postalCode;

    @ManyToOne @JoinColumn(name = "customer", referencedColumnName = "tax_code")
    @JsonBackReference @EqualsAndHashCode.Exclude
    private Customer customer;
}
