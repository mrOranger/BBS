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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;


@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class DeleteTest {
    private final Faker faker;
    private Customer customer;
    private Address address;
    private final AddressRepository addressRepository;
    private final CustomerRepository customerRepository;

    @Autowired
    public DeleteTest(AddressRepository addressRepository, CustomerRepository customerRepository) {
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
    public void testDeleteAddressSuccess () {
        this.address.setCustomer(this.customer);
        this.customerRepository.save(this.customer);
        this.addressRepository.save(this.address);
        this.addressRepository.delete(this.address);

        final Optional<Customer> updatedCustomer = this.customerRepository.findById(this.customer.getTaxCode());
        final Optional<Address> updatedAddress = this.addressRepository.findById(this.address.getId().toString());

        assertAll(
                () -> Assertions.assertTrue(updatedAddress.isEmpty()),
                () -> Assertions.assertEquals(updatedCustomer.get().getTaxCode(), this.customer.getTaxCode()),
                () -> Assertions.assertEquals(updatedCustomer.get().getFirstName(), this.customer.getFirstName()),
                () -> Assertions.assertEquals(updatedCustomer.get().getLastName(), this.customer.getLastName()),
                () -> Assertions.assertEquals(updatedCustomer.get().getBirthDate(), this.customer.getBirthDate()),
                () -> Assertions.assertEquals(updatedCustomer.get().getEmail(), this.customer.getEmail()),
                () -> Assertions.assertEquals(updatedCustomer.get().getEmailVerifiedAt(), this.customer.getEmailVerifiedAt()),
                () -> Assertions.assertEquals(updatedCustomer.get().getPassword(), this.customer.getPassword()),
                () -> Assertions.assertEquals(updatedCustomer.get().getIdCard(), this.customer.getIdCard()),
                () -> Assertions.assertNull(updatedCustomer.get().getAddresses())
        );
    }
}
