package com.edoardo.bbs.repositories;

import com.edoardo.bbs.entities.Customer;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest()
public class CustomerRepositoryFindAllTest {
    private final CustomerRepository customerRepository;
    private final Faker faker;
    private final int maxRandomElements;

    @Autowired
    public CustomerRepositoryFindAllTest(CustomerRepository customerRepository) {
        this.maxRandomElements = Integer.parseInt(Double.toString((Math.random() * 10) + 1));
        this.customerRepository = customerRepository;
        this.faker = new Faker();
    }

    @Test
    public void testGetAllCustomersReturnsEmptySet () {
        final List<Customer> customers = StreamSupport.stream(
                        this.customerRepository.findAll().spliterator(), false)
                .toList();

        assertThat(customers.size()).isEqualTo(this.maxRandomElements);
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
