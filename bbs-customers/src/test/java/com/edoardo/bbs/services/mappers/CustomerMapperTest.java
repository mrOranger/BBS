package com.edoardo.bbs.services.mappers;

import com.edoardo.bbs.dtos.AddressDTO;
import com.edoardo.bbs.dtos.CustomerDTO;
import com.edoardo.bbs.entities.Address;
import com.edoardo.bbs.entities.Customer;
import com.edoardo.bbs.mapper.CustomerMapper;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.ZoneId;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class CustomerMapperTest {

    private final Faker faker;
    private Address address;
    private Customer customer;
    private AddressDTO addressDTO;
    private CustomerDTO customerDTO;
    private final CustomerMapper customerMapper;

    @Autowired
    public CustomerMapperTest (CustomerMapper customerMapper) {
        this.faker = new Faker();
        this.customerMapper = customerMapper;
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

        this.customerDTO = CustomerDTO.builder().taxCode(this.faker.code().isbn10())
                .firstName(this.faker.name().firstName())
                .lastName(this.faker.name().lastName())
                .birthDate(this.faker.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                .email(this.faker.internet().emailAddress())
                .emailVerifiedAt(this.faker.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                .password(this.faker.internet().password())
                .idCard(this.faker.file().toString())
                .addresses(new HashSet<>())
                .build();

        this.address = Address.builder()
                .id(this.faker.number().randomDigit())
                .country(this.faker.address().country())
                .state(this.faker.address().state())
                .city(this.faker.address().city())
                .street(this.faker.address().streetName())
                .streetNumber(Integer.parseInt(this.faker.address().streetAddressNumber()))
                .postalCode(this.faker.address().zipCode())
                .build();

        this.addressDTO = AddressDTO.builder()
                .id(this.faker.number().randomDigit())
                .country(this.faker.address().country())
                .state(this.faker.address().state())
                .city(this.faker.address().city())
                .street(this.faker.address().streetName())
                .streetNumber(Integer.parseInt(this.faker.address().streetAddressNumber()))
                .postalCode(this.faker.address().zipCode())
                .build();
    }

    @Test
    public void testMappingCustomerToDTOWithAddress () {
        this.customer.getAddresses().add(this.address);

        final CustomerDTO customerMapped = this.customerMapper.convertToDTO(this.customer);

        assertAll(
                () -> assertEquals(this.customer.getTaxCode(), customerMapped.getTaxCode()),
                () -> assertEquals(this.customer.getFirstName(), customerMapped.getFirstName()),
                () -> assertEquals(this.customer.getLastName(), customerMapped.getLastName()),
                () -> assertEquals(this.customer.getBirthDate(), customerMapped.getBirthDate()),
                () -> assertEquals(this.customer.getEmail(), customerMapped.getEmail()),
                () -> assertEquals(this.customer.getEmailVerifiedAt(), customerMapped.getEmailVerifiedAt()),
                () -> assertEquals(this.customer.getPassword(), customerMapped.getPassword()),
                () -> assertEquals(this.customer.getIdCard(), customerMapped.getIdCard()),
                () -> this.customer.getAddresses().forEach((address -> assertAll(
                        () -> assertEquals(address.getId(), this.address.getId()),
                        () -> assertEquals(address.getCountry(), this.address.getCountry()),
                        () -> assertEquals(address.getState(), this.address.getState()),
                        () -> assertEquals(address.getCity(), this.address.getCity()),
                        () -> assertEquals(address.getStreet(), this.address.getStreet()),
                        () -> assertEquals(address.getStreetNumber(), this.address.getStreetNumber()),
                        () -> assertEquals(address.getPostalCode(), this.address.getPostalCode())
                )))
        );
    }

    @Test
    public void testMappingCustomerToDTOWithoutAddress () {
        final CustomerDTO customerMapped = this.customerMapper.convertToDTO(this.customer);

        assertAll(
                () -> assertEquals(this.customer.getTaxCode(), customerMapped.getTaxCode()),
                () -> assertEquals(this.customer.getFirstName(), customerMapped.getFirstName()),
                () -> assertEquals(this.customer.getLastName(), customerMapped.getLastName()),
                () -> assertEquals(this.customer.getBirthDate(), customerMapped.getBirthDate()),
                () -> assertEquals(this.customer.getEmail(), customerMapped.getEmail()),
                () -> assertEquals(this.customer.getEmailVerifiedAt(), customerMapped.getEmailVerifiedAt()),
                () -> assertEquals(this.customer.getPassword(), customerMapped.getPassword()),
                () -> assertEquals(this.customer.getIdCard(), customerMapped.getIdCard())
        );
    }

    @Test
    public void testMappingCustomerToEntityWithAddress () {
        this.customerDTO.getAddresses().add(this.addressDTO);

        final Customer customerMapped = this.customerMapper.convertToEntity(this.customerDTO);

        assertAll(
                () -> assertEquals(this.customerDTO.getTaxCode(), customerMapped.getTaxCode()),
                () -> assertEquals(this.customerDTO.getFirstName(), customerMapped.getFirstName()),
                () -> assertEquals(this.customerDTO.getLastName(), customerMapped.getLastName()),
                () -> assertEquals(this.customerDTO.getBirthDate(), customerMapped.getBirthDate()),
                () -> assertEquals(this.customerDTO.getEmail(), customerMapped.getEmail()),
                () -> assertEquals(this.customerDTO.getEmailVerifiedAt(), customerMapped.getEmailVerifiedAt()),
                () -> assertEquals(this.customerDTO.getPassword(), customerMapped.getPassword()),
                () -> assertEquals(this.customerDTO.getIdCard(), customerMapped.getIdCard()),
                () -> this.customerDTO.getAddresses().forEach((address -> assertAll(
                        () -> assertEquals(address.getId(), this.addressDTO.getId()),
                        () -> assertEquals(address.getCountry(), this.addressDTO.getCountry()),
                        () -> assertEquals(address.getState(), this.addressDTO.getState()),
                        () -> assertEquals(address.getCity(), this.addressDTO.getCity()),
                        () -> assertEquals(address.getStreet(), this.addressDTO.getStreet()),
                        () -> assertEquals(address.getStreetNumber(), this.addressDTO.getStreetNumber()),
                        () -> assertEquals(address.getPostalCode(), this.addressDTO.getPostalCode())
                )))
        );
    }

    @Test
    public void testMappingCustomerToEntityWithoutAddress () {
        final Customer customerMapped = this.customerMapper.convertToEntity(this.customerDTO);

        assertAll(
                () -> assertEquals(this.customerDTO.getTaxCode(), customerMapped.getTaxCode()),
                () -> assertEquals(this.customerDTO.getFirstName(), customerMapped.getFirstName()),
                () -> assertEquals(this.customerDTO.getLastName(), customerMapped.getLastName()),
                () -> assertEquals(this.customerDTO.getBirthDate(), customerMapped.getBirthDate()),
                () -> assertEquals(this.customerDTO.getEmail(), customerMapped.getEmail()),
                () -> assertEquals(this.customerDTO.getEmailVerifiedAt(), customerMapped.getEmailVerifiedAt()),
                () -> assertEquals(this.customerDTO.getPassword(), customerMapped.getPassword()),
                () -> assertEquals(this.customerDTO.getIdCard(), customerMapped.getIdCard())
        );
    }
}
