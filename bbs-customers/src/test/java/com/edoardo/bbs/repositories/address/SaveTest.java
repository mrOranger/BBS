package com.edoardo.bbs.repositories.address;


import com.edoardo.bbs.entities.Address;
import com.edoardo.bbs.entities.Customer;
import com.edoardo.bbs.repositories.AddressRepository;
import com.edoardo.bbs.repositories.CustomerRepository;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class SaveTest {
    private final Faker faker;
    private Customer customer;
    private Address address;
    private final AddressRepository addressRepository;
    private final CustomerRepository customerRepository;

    @Autowired
    public SaveTest(AddressRepository addressRepository, CustomerRepository customerRepository) {
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

        this.address = Address.builder()
                .country(this.faker.address().country())
                .state(this.faker.address().state())
                .city(this.faker.address().city())
                .street(this.faker.address().streetAddress())
                .streetNumber(Integer.parseInt(this.faker.address().streetAddressNumber()))
                .postalCode(this.faker.address().zipCode())
                .build();
    }

    @Test
    public void testSaveAddressSuccess () {
        this.address.setCustomer(this.customer);

        this.customerRepository.save(this.customer);
        final Address updatedAddress = this.addressRepository.save(this.address);

        assertAll(
                () -> Assertions.assertEquals(updatedAddress.getCountry(), this.address.getCountry()),
                () -> Assertions.assertEquals(updatedAddress.getState(), this.address.getState()),
                () -> Assertions.assertEquals(updatedAddress.getCity(), this.address.getCity()),
                () -> Assertions.assertEquals(updatedAddress.getStreet(), this.address.getStreet()),
                () -> Assertions.assertEquals(updatedAddress.getStreetNumber(), this.address.getStreetNumber()),
                () -> Assertions.assertEquals(updatedAddress.getCustomer().getTaxCode(), this.customer.getTaxCode()),
                () -> Assertions.assertEquals(updatedAddress.getCustomer().getFirstName(), this.customer.getFirstName()),
                () -> Assertions.assertEquals(updatedAddress.getCustomer().getLastName(), this.customer.getLastName()),
                () -> Assertions.assertEquals(updatedAddress.getCustomer().getBirthDate(), this.customer.getBirthDate()),
                () -> Assertions.assertEquals(updatedAddress.getCustomer().getEmail(), this.customer.getEmail())
        );
    }
}
