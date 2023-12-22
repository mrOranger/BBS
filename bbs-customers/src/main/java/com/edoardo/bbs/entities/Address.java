package com.edoardo.bbs.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Entity @Table(name = "Addresses") @Data @NoArgsConstructor
public class Address implements Serializable {
    @Serial
    private static final long serialVersionUID = 2L;

    @Id @Column(name = "id")
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

    @ManyToOne @EqualsAndHashCode.Exclude @JoinColumn(name = "customer", referencedColumnName = "tax_code")
    @JsonManagedReference
    private Customer customer;

    public Address (Integer id, String country, String state, String city,
                    String street, Integer streetNumber, String postalCode) {
        this.id = id;
        this.country = country;
        this.state = state;
        this.city = city;
        this.street = street;
        this.streetNumber = streetNumber;
        this.postalCode = postalCode;
    }
}
