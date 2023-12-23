package com.edoardo.bbs.repositories.customer;

import com.edoardo.bbs.entities.Customer;
import com.edoardo.bbs.repositories.CustomerRepository;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.assertj.core.api.Assertions;

import java.util.List;

@DataJpaTest @AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class FindAllTest {
    private final Faker faker;
    private int maxRandomElements;

    private final CustomerRepository customerRepository;

    @Autowired
    public FindAllTest(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
        this.faker = new Faker();
    }

    @BeforeEach
    public void init () {
        this.maxRandomElements = (int)((Math.random() * 10) + 1);
    }

    @Test
    public void CustomerRepository_findAll_ReturnsEmptyList () {
        final List<Customer> customers = this.customerRepository.findAll();

        Assertions.assertThat(customers.size()).isEqualTo(0);
    }

    @Test
    public void CustomerRepository_findAll_ReturnsOneCustomerList () {
        final Customer newCustomer = Customer.builder().taxCode(this.faker.code().isbn10())
                .firstName(this.faker.name().firstName())
                .lastName(this.faker.name().lastName())
                .birthDate(this.faker.date().birthday())
                .email(this.faker.internet().emailAddress())
                .emailVerifiedAt(this.faker.date().birthday())
                .password(this.faker.internet().password())
                .idCard(this.faker.file().toString())
                .build();
        this.customerRepository.save(newCustomer);

        final List<Customer> customers = this.customerRepository.findAll();

        Assertions.assertThat(customers.size()).isEqualTo(1);
    }

    @Test
    public void CustomerRepository_findAll_ReturnsManyCustomersList () {
        for (int i = 0; i < this.maxRandomElements; i++) {
            final Customer newCustomer = Customer.builder().taxCode(this.faker.code().isbn10())
                    .firstName(this.faker.name().firstName())
                    .lastName(this.faker.name().lastName())
                    .birthDate(this.faker.date().birthday())
                    .email(this.faker.internet().emailAddress())
                    .emailVerifiedAt(this.faker.date().birthday())
                    .password(this.faker.internet().password())
                    .idCard(this.faker.file().toString())
                    .build();
            this.customerRepository.save(newCustomer);
        }

        final List<Customer> customers = this.customerRepository.findAll();

        Assertions.assertThat(customers.size()).isEqualTo(this.maxRandomElements);
    }
}
