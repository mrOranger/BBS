package com.edoardo.bbs.repositories.customer;

import com.edoardo.bbs.entities.Address;
import com.edoardo.bbs.entities.Customer;
import com.edoardo.bbs.repositories.CustomerRepository;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.ZoneId;
import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class DeleteTest {

    private Address address;
    private final Faker faker;
    private Customer customer;
    private final CustomerRepository customerRepository;


    @Autowired
    public DeleteTest(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
        this.faker = new Faker();
    }

    @BeforeEach
    public void init () {
        this.customer = Customer.builder().taxCode(this.faker.code().isbn10())
                .firstName(this.faker.name().firstName())
                .lastName(this.faker.name().lastName())
                .birthDate(this.faker.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                .email(this.faker.internet().emailAddress())
                .emailVerifiedAt(this.faker.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                .password(this.faker.internet().password())
                .idCard(this.faker.file().toString())
                .addresses(new HashSet<>())
                .build();

        this.address = Address.builder().country(this.faker.address().country())
                .state(this.faker.address().state())
                .city(this.faker.address().city())
                .street(this.faker.address().city())
                .streetNumber(Integer.parseInt(this.faker.address().streetAddressNumber()))
                .postalCode(this.faker.address().zipCode())
                .build();
    }

    @Test
    public void deleteCustomerWithoutAddress () {
        this.customerRepository.save(this.customer);
        this.customerRepository.delete(this.customer);

        final Optional<Customer> customer = this.customerRepository.findById(this.customer.getTaxCode());

        assertThat(customer.isPresent()).isEqualTo(false);
    }

    @Test
    public void deleteCustomerWithAddress () {
        address.setCustomer(this.customer);
        this.customer.getAddresses().add(address);
        this.customerRepository.save(this.customer);
        this.customerRepository.delete(this.customer);

        final Optional<Customer> customer = this.customerRepository.findById(this.customer.getTaxCode());

        assertThat(customer.isPresent()).isEqualTo(false);
    }
}
