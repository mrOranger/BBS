package com.edoardo.bbs.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity @Table(name = "Customers") @Data @NoArgsConstructor
public class Customer implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id @Column(name = "tax_code", length = 30)
    private String taxCode;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(name = "birth_date", nullable = false)
    private Date birthDate;

    @Column(name = "email", nullable = false, unique = true, length = 50)
    private String email;

    @Column(name = "email_verified_at")
    private Date emailVerifiedAt;

    @Column(name = "password", nullable = false, length = 50)
    private String password;

    @Column(name = "id_card", nullable = false)
    private String idCard;

    @OneToMany(fetch = FetchType.LAZY, cascade =  CascadeType.ALL, mappedBy = "customer", orphanRemoval = true)
    @JsonManagedReference
    private Set<Address> addresses;

    public Customer(String taxCode, String firstName, String lastName, Date birthDate,
                    String email, Date emailVerifiedAt, String password, String idCard) {
        this.taxCode = taxCode;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.email = email;
        this.emailVerifiedAt = emailVerifiedAt;
        this.password = password;
        this.idCard = idCard;
        this.addresses = new HashSet<>();
    }
}
