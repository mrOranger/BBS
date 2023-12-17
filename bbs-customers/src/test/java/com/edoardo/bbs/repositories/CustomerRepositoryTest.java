package com.edoardo.bbs.repositories;

import com.edoardo.bbs.entities.Customer;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest()
public class CustomerRepositoryTest {

    private final CustomerRepository customerRepository;
    private final Faker faker;

    @Autowired
    public CustomerRepositoryTest(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
        this.faker = new Faker();
    }

    @Test
    public void testGetAllCustomersReturnsEmptySet () {
        final Set<Customer> customers = this.customerRepository.findAll();

        assertThat(customers.size()).isEqualTo(0);
    }

    @Test
    public void testGetAllCustomersReturnsOneCustomer () {
        final Customer newCustomer = new Customer(
                this.faker.code().isbn10(),
                this.faker.name().firstName(), this.faker.name().lastName(),
                this.faker.date().birthday(),
                this.faker.internet().emailAddress(), null,
                this.faker.internet().password(), this.faker.file().toString()
        );

        this.customerRepository.save(newCustomer);
        final Set<Customer> customers = this.customerRepository.findAll();

        assertThat(customers.size()).isEqualTo(1);
    }

    @Test
    public void testGetAllCustomersReturnsManyCustomers () {
        for (int i = 0; i < 10; i++) {
            final Customer newCustomer = new Customer(
                    this.faker.code().isbn10(),
                    this.faker.name().firstName(), this.faker.name().lastName(),
                    this.faker.date().birthday(),
                    this.faker.internet().emailAddress(), null,
                    this.faker.internet().password(), this.faker.file().toString()
            );
            this.customerRepository.save(newCustomer);
        }
        final Set<Customer> customers = this.customerRepository.findAll();

        assertThat(customers.size()).isEqualTo(10);
    }
}