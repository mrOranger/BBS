package com.edoardo.bbs.repositories.address;

import com.edoardo.bbs.entities.Address;
import com.edoardo.bbs.entities.Customer;
import com.edoardo.bbs.repositories.AddressRepository;
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
import java.util.Optional;


@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class FindByIdTest {
    private final Faker faker;
    private Address address;
    private Customer customer;
    private final AddressRepository addressRepository;
    private final CustomerRepository customerRepository;

    @Autowired
    public FindByIdTest(AddressRepository addressRepository, CustomerRepository customerRepository) {
        this.addressRepository = addressRepository;
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
                .build();
    }

    @Test
    public void findByIdReturnsNoneAddress () {
        final Optional<Address> customer = this.addressRepository.findById(this.faker.code().isbn10());

        Assertions.assertThat(customer.isEmpty()).isEqualTo(true);
    }

    @Test
    public void findByIdReturnsOneAddress () {
        this.address = Address.builder()
                .country(this.faker.address().country())
                .state(this.faker.address().state())
                .city(this.faker.address().city())
                .street(this.faker.address().streetAddress())
                .streetNumber(Integer.parseInt(this.faker.address().streetAddressNumber()))
                .postalCode(this.faker.address().zipCode())
                .build();

        this.address.setCustomer(this.customer);
        this.customerRepository.save(this.customer);
        this.addressRepository.save(this.address);

        final Optional<Address> customer = this.addressRepository.findById(this.address.getId().toString());

        Assertions.assertThat(customer.isEmpty()).isEqualTo(false);
    }
}
