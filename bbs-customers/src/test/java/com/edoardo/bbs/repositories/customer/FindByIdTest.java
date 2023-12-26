package com.edoardo.bbs.repositories.customer;

import com.edoardo.bbs.entities.Customer;
import com.edoardo.bbs.repositories.CustomerRepository;
import com.github.javafaker.Faker;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class FindByIdTest {
    private final Faker faker;

    private final CustomerRepository customerRepository;

    @Autowired
    public FindByIdTest(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
        this.faker = new Faker();
    }

    @Test
    public void findByIdReturnsNoneCustomer () {
        final Optional<Customer> customer = this.customerRepository.findById(this.faker.code().isbn10());

        Assertions.assertThat(customer.isEmpty()).isEqualTo(true);
    }

    @Test
    public void findByIdReturnsOneCustomer () {
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

        final Optional<Customer> customer = this.customerRepository.findById(newCustomer.getTaxCode());

        Assertions.assertThat(customer.isEmpty()).isEqualTo(false);
    }
}
