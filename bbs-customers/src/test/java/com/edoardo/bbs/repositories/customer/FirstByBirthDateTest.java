package com.edoardo.bbs.repositories.customer;


import com.edoardo.bbs.entities.Customer;
import com.edoardo.bbs.repositories.CustomerRepository;
import com.github.javafaker.Faker;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Date;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class FirstByBirthDateTest {

    private final Faker faker;
    private int maxRandomElements;

    @Autowired
    private CustomerRepository customerRepository;

    public FirstByBirthDateTest() {
        this.faker = new Faker();
    }

    @BeforeEach
    public void init () {
        this.maxRandomElements = (int)((Math.random() * 10) + 1);
    }

    @Test
    public void CustomerRepository_findByBirthDate_ReturnsEmptyList () {
        final Date birthDate = this.faker.date().birthday();

        final List<Customer> customers = this.customerRepository.findByBirthDate(birthDate);

        Assertions.assertThat(customers.size()).isEqualTo(0);
    }

    @Test
    public void CustomerRepository_findByBirthDate_ReturnsOneCustomer () {
        final Date birthDate = this.faker.date().birthday();
        final Customer newCustomer = Customer.builder().taxCode(this.faker.code().isbn10())
                .firstName(this.faker.name().firstName())
                .lastName(this.faker.name().lastName())
                .birthDate(birthDate)
                .email(this.faker.internet().emailAddress())
                .emailVerifiedAt(this.faker.date().birthday())
                .password(this.faker.internet().password())
                .idCard(this.faker.file().toString())
                .build();
        this.customerRepository.save(newCustomer);

        final List<Customer> customers = this.customerRepository.findByBirthDate(birthDate);

        Assertions.assertThat(customers.size()).isEqualTo(1);
    }

    @Test
    public void CustomerRepository_findByBirthDate_ReturnsManyCustomer () {
        final Date birthDate = this.faker.date().birthday();
        for (int i = 0; i < this.maxRandomElements; i++) {
            final Customer newCustomer = Customer.builder().taxCode(this.faker.code().isbn10())
                    .firstName(this.faker.name().firstName())
                    .lastName(this.faker.name().lastName())
                    .birthDate(birthDate)
                    .email(this.faker.internet().emailAddress())
                    .emailVerifiedAt(this.faker.date().birthday())
                    .password(this.faker.internet().password())
                    .idCard(this.faker.file().toString())
                    .build();
            this.customerRepository.save(newCustomer);
        }

        final List<Customer> customers = this.customerRepository.findByBirthDate(birthDate);

        Assertions.assertThat(customers.size()).isEqualTo(this.maxRandomElements);
    }
}
