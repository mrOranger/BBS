package com.edoardo.bbs.repositories.customer;


import com.edoardo.bbs.entities.Customer;
import com.edoardo.bbs.repositories.CustomerRepository;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class FirstByBirthDateTest {

    private final Faker faker;
    private final int maxRandomElements;

    @Autowired
    private CustomerRepository customerRepository;

    public FirstByBirthDateTest() {
        this.maxRandomElements = (int)((Math.random() * 10) + 1);
        this.faker = new Faker();
    }

    @Test
    public void testGetAllCustomersReturnsNoneCustomer () {
        final Date birthDate = this.faker.date().birthday();

        final List<Customer> customers = this.customerRepository.findByBirthDate(birthDate).stream()
                .toList();

        assertThat(customers.size()).isEqualTo(0);
    }

    @Test
    public void testGetAllCustomersReturnsSingleCustomer () {
        final Date birthDate = this.faker.date().birthday();
        this.customerRepository.save(new Customer(
                this.faker.code().isbn10(), this.faker.name().firstName(), this.faker.name().lastName(),
                birthDate, this.faker.internet().emailAddress(), this.faker.date().birthday(),
                this.faker.internet().password(), this.faker.file().toString()
        ));

        final List<Customer> customers = this.customerRepository.findByBirthDate(birthDate).stream()
                .toList();

        assertThat(customers.size()).isEqualTo(1);
    }

    @Test
    public void testGetAllCustomersReturnsManyCustomers () {
        final Date birthDate = this.faker.date().birthday();
        for (int i = 0; i < this.maxRandomElements; i++) {
            this.customerRepository.save(new Customer(
                    this.faker.code().isbn10(), this.faker.name().lastName(), this.faker.name().firstName(),
                    birthDate, this.faker.internet().emailAddress(), this.faker.date().birthday(),
                    this.faker.internet().password(), this.faker.file().toString()
            ));
        }

        final List<Customer> customers = this.customerRepository.findByBirthDate(birthDate).stream()
                .toList();

        assertThat(customers.size()).isEqualTo(this.maxRandomElements);
    }
}
