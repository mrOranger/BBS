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

import java.time.ZoneId;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class FindByFirstNameAndLastNameTest {
    private final Faker faker;
    private int maxRandomElements;

    private final CustomerRepository customerRepository;

    @Autowired
    public FindByFirstNameAndLastNameTest(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
        this.faker = new Faker();
    }

    @BeforeEach
    public void init () {
        this.maxRandomElements = (int)((Math.random() * 10) + 1);
    }

    @Test
    public void findByFirstNameAndLastNameReturnsEmptySet () {
        final String firstName = this.faker.name().firstName();
        final String lastname = this.faker.name().lastName();

        final List<Customer> customers = this.customerRepository.findByFirstNameAndLastName(firstName, lastname);

        Assertions.assertThat(customers.size()).isEqualTo(0);
    }

    @Test
    public void findByFirstNameAndLastNameReturnsOneCustomer () {
        final String firstName = this.faker.name().firstName();
        final String lastname = this.faker.name().lastName();
        final Customer newCustomer = Customer.builder().taxCode(this.faker.code().isbn10())
                .firstName(firstName)
                .lastName(lastname)
                .birthDate(this.faker.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                .email(this.faker.internet().emailAddress())
                .emailVerifiedAt(this.faker.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                .password(this.faker.internet().password())
                .idCard(this.faker.file().toString())
                .build();

        this.customerRepository.save(newCustomer);

        final List<Customer> customers = this.customerRepository.findByFirstNameAndLastName(firstName, lastname);

        Assertions.assertThat(customers.size()).isEqualTo(1);
    }

    @Test
    public void findByFirstNameAndLastNameReturnsManyCustomers () {
        final String firstName = this.faker.name().firstName();
        final String lastname = this.faker.name().lastName();
        for (int i = 0; i < this.maxRandomElements; i++) {
            final Customer newCustomer = Customer.builder().taxCode(this.faker.code().isbn10())
                    .firstName(firstName)
                    .lastName(lastname)
                    .birthDate(this.faker.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                    .email(this.faker.internet().emailAddress())
                    .emailVerifiedAt(this.faker.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                    .password(this.faker.internet().password())
                    .idCard(this.faker.file().toString())
                    .build();

            this.customerRepository.save(newCustomer);
        }

        final List<Customer> customers = this.customerRepository.findByFirstNameAndLastName(firstName, lastname);

        Assertions.assertThat(customers.size()).isEqualTo(this.maxRandomElements);
    }
}
