package com.edoardo.bbs.repositories.customer;


import com.edoardo.bbs.entities.Address;
import com.edoardo.bbs.entities.Customer;
import com.edoardo.bbs.repositories.CustomerRepository;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class SaveTest {
    private final Faker faker;

    @Autowired
    private CustomerRepository customerRepository;

    public SaveTest() {
        this.faker = new Faker();
    }

    @Test
    public void CustomerRepository_save_withoutAddress_ReturnsCustomer () {
        final Customer newCustomer = Customer.builder().taxCode(this.faker.code().isbn10())
                .firstName(this.faker.name().firstName())
                .lastName(this.faker.name().lastName())
                .birthDate(this.faker.date().birthday())
                .email(this.faker.internet().emailAddress())
                .emailVerifiedAt(this.faker.date().birthday())
                .password(this.faker.internet().password())
                .idCard(this.faker.file().toString())
                .addresses(new HashSet<>())
                .build();
        this.customerRepository.save(newCustomer);

        final Optional<Customer> customer = this.customerRepository.findById(newCustomer.getTaxCode());

        assertThat(customer.isPresent()).isEqualTo(true);
        assertThat(customer.get().getTaxCode()).isEqualTo(newCustomer.getTaxCode());
        assertThat(customer.get().getFirstName()).isEqualTo(newCustomer.getFirstName());
        assertThat(customer.get().getLastName()).isEqualTo(newCustomer.getLastName());
        assertThat(customer.get().getBirthDate()).isEqualTo(newCustomer.getBirthDate());
        assertThat(customer.get().getEmail()).isEqualTo(newCustomer.getEmail());
        assertThat(customer.get().getEmailVerifiedAt()).isEqualTo(newCustomer.getEmailVerifiedAt());
        assertThat(customer.get().getPassword()).isEqualTo(newCustomer.getPassword());
        assertThat(customer.get().getIdCard()).isEqualTo(newCustomer.getIdCard());
    }

    @Test
    public void CustomerRepository_save_withAddress_ReturnsCustomer () {
        final Address address = Address.builder().country(this.faker.address().country())
                .state(this.faker.address().state())
                .city(this.faker.address().city())
                .street(this.faker.address().city())
                .streetNumber(Integer.parseInt(this.faker.address().streetAddressNumber()))
                .postalCode(this.faker.address().zipCode())
                .build();
        final Customer newCustomer = Customer.builder().taxCode(this.faker.code().isbn10())
                .firstName(this.faker.name().firstName())
                .lastName(this.faker.name().lastName())
                .birthDate(this.faker.date().birthday())
                .email(this.faker.internet().emailAddress())
                .emailVerifiedAt(this.faker.date().birthday())
                .password(this.faker.internet().password())
                .idCard(this.faker.file().toString())
                .addresses(new HashSet<>())
                .build();
        address.setCustomer(newCustomer);
        newCustomer.getAddresses().add(address);
        this.customerRepository.save(newCustomer);

        final Optional<Customer> customer = this.customerRepository.findById(newCustomer.getTaxCode());

        assertThat(customer.isPresent()).isEqualTo(true);
        assertThat(customer.get().getTaxCode()).isEqualTo(newCustomer.getTaxCode());
        assertThat(customer.get().getFirstName()).isEqualTo(newCustomer.getFirstName());
        assertThat(customer.get().getLastName()).isEqualTo(newCustomer.getLastName());
        assertThat(customer.get().getBirthDate()).isEqualTo(newCustomer.getBirthDate());
        assertThat(customer.get().getEmail()).isEqualTo(newCustomer.getEmail());
        assertThat(customer.get().getEmailVerifiedAt()).isEqualTo(newCustomer.getEmailVerifiedAt());
        assertThat(customer.get().getPassword()).isEqualTo(newCustomer.getPassword());
        assertThat(customer.get().getIdCard()).isEqualTo(newCustomer.getIdCard());
        assertThat(customer.get().getAddresses().size()).isEqualTo(1);
    }
}
