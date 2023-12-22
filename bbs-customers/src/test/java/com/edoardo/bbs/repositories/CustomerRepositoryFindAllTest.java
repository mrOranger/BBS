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

@DataJpaTest @AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CustomerRepositoryFindAllTest {
    private final Faker faker;
    private final int maxRandomElements;

    @Autowired private CustomerRepository customerRepository;

    public CustomerRepositoryFindAllTest() {
        this.maxRandomElements = (int)((Math.random() * 10) + 1);
        this.faker = new Faker();
    }

    @Test
    public void testGetAllCustomersReturnsNoneCustomer () {
        final List<Customer> customers = StreamSupport.stream(
                        this.customerRepository.findAll().spliterator(), false)
                .toList();

        assertThat(customers.size()).isEqualTo(0);
    }

    @Test
    public void testGetAllCustomersReturnsSingleCustomer () {
        this.customerRepository.save(new Customer(
                this.faker.code().isbn10(), this.faker.name().firstName(), this.faker.name().lastName(),
                this.faker.date().birthday(), this.faker.internet().emailAddress(), this.faker.date().birthday(),
                this.faker.internet().password(), this.faker.file().toString()
        ));

        final List<Customer> customers = StreamSupport.stream(
                        this.customerRepository.findAll().spliterator(), false)
                .toList();

        assertThat(customers.size()).isEqualTo(1);
    }

    @Test
    public void testGetAllCustomersReturnsManyCustomers () {
        for (int i = 0; i < this.maxRandomElements; i++) {
            this.customerRepository.save(new Customer(
                    this.faker.code().isbn10(), this.faker.name().firstName(), this.faker.name().lastName(),
                    this.faker.date().birthday(), this.faker.internet().emailAddress(), this.faker.date().birthday(),
                    this.faker.internet().password(), this.faker.file().toString()
            ));
        }

        final List<Customer> customers = StreamSupport.stream(
                        this.customerRepository.findAll().spliterator(), false)
                .toList();

        assertThat(customers.size()).isEqualTo(this.maxRandomElements);
    }
}
