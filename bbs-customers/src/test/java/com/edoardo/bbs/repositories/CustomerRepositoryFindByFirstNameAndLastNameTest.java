package com.edoardo.bbs.repositories;


import com.edoardo.bbs.entities.Customer;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CustomerRepositoryFindByFirstNameAndLastNameTest {
    private final Faker faker;
    private final int maxRandomElements;

    @Autowired
    private CustomerRepository customerRepository;

    public CustomerRepositoryFindByFirstNameAndLastNameTest() {
        this.maxRandomElements = (int)((Math.random() * 10) + 1);
        this.faker = new Faker();
    }

    @Test
    public void testGetAllCustomersReturnsNoneCustomer () {
        final String firstName = this.faker.name().firstName();
        final String lastname = this.faker.name().lastName();

        final List<Customer> customers = this.customerRepository.findByFirstNameAndLastName(firstName, lastname).stream()
                .toList();

        assertThat(customers.size()).isEqualTo(0);
    }

    @Test
    public void testGetAllCustomersReturnsSingleCustomer () {
        final String firstName = this.faker.name().firstName();
        final String lastname = this.faker.name().lastName();

        this.customerRepository.save(new Customer(
                this.faker.code().isbn10(), firstName, lastname,
                this.faker.date().birthday(), this.faker.internet().emailAddress(), this.faker.date().birthday(),
                this.faker.internet().password(), this.faker.file().toString()
        ));

        final List<Customer> customers = this.customerRepository.findByFirstNameAndLastName(firstName, lastname).stream()
                .toList();

        assertThat(customers.size()).isEqualTo(1);
    }

    @Test
    public void testGetAllCustomersReturnsManyCustomers () {
        final String firstName = this.faker.name().firstName();
        final String lastname = this.faker.name().lastName();
        for (int i = 0; i < this.maxRandomElements; i++) {
            this.customerRepository.save(new Customer(
                    this.faker.code().isbn10(), firstName, lastname,
                    this.faker.date().birthday(), this.faker.internet().emailAddress(), this.faker.date().birthday(),
                    this.faker.internet().password(), this.faker.file().toString()
            ));
        }

        final List<Customer> customers = this.customerRepository.findByFirstNameAndLastName(firstName, lastname).stream()
                .toList();

        assertThat(customers.size()).isEqualTo(this.maxRandomElements);
    }
}
