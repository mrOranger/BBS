package com.edoardo.bbs.services.mappers;


import com.edoardo.bbs.dtos.AddressDTO;
import com.edoardo.bbs.dtos.CustomerDTO;
import com.edoardo.bbs.entities.Address;
import com.edoardo.bbs.entities.Customer;
import com.edoardo.bbs.factories.AddressFactory;
import com.edoardo.bbs.factories.CustomerFactory;
import com.edoardo.bbs.mapper.AddressMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class AddressMapperTest {

    private Address address;
    private Customer customer;
    private AddressDTO addressDTO;
    private CustomerDTO customerDTO;
    private final AddressMapper addressMapper;
    private final AddressFactory addressFactory;
    private final CustomerFactory customerFactory;

    @Autowired
    public AddressMapperTest (
            AddressMapper addressMapper,
            AddressFactory addressFactory,
            CustomerFactory customerFactory
    ) {
        this.addressMapper = addressMapper;
        this.addressFactory = addressFactory;
        this.customerFactory = customerFactory;
    }

    @BeforeEach
    public void init () {
        this.address = this.addressFactory.create();
        this.customer = this.customerFactory.create();
        this.addressDTO = this.addressFactory.createDTO();
        this.customerDTO = this.customerFactory.createDTO();
    }

    @Test
    public void testMappingAddressToDTOWithCustomer () {
        this.address.setCustomer(this.customer);

        final AddressDTO addressMapped = this.addressMapper.convertToDTO(this.address);

        assertAll(
                () -> assertEquals(addressMapped.getId(), this.address.getId()),
                () -> assertEquals(addressMapped.getCountry(), this.address.getCountry()),
                () -> assertEquals(addressMapped.getState(), this.address.getState()),
                () -> assertEquals(addressMapped.getCity(), this.address.getCity()),
                () -> assertEquals(addressMapped.getStreet(), this.address.getStreet()),
                () -> assertEquals(addressMapped.getStreetNumber(), this.address.getStreetNumber()),
                () -> assertEquals(addressMapped.getPostalCode(), this.address.getPostalCode()),
                () -> assertAll(
                        () -> assertEquals(addressMapped.getCustomer().getTaxCode(), this.address.getCustomer().getTaxCode()),
                        () -> assertEquals(addressMapped.getCustomer().getFirstName(), this.address.getCustomer().getFirstName()),
                        () -> assertEquals(addressMapped.getCustomer().getLastName(), this.address.getCustomer().getLastName()),
                        () -> assertEquals(addressMapped.getCustomer().getBirthDate(), this.address.getCustomer().getBirthDate()),
                        () -> assertEquals(addressMapped.getCustomer().getEmailVerifiedAt(), this.address.getCustomer().getEmailVerifiedAt()),
                        () -> assertEquals(addressMapped.getCustomer().getEmail(), this.address.getCustomer().getEmail()),
                        () -> assertEquals(addressMapped.getCustomer().getPassword(), this.address.getCustomer().getPassword()),
                        () -> assertEquals(addressMapped.getCustomer().getIdCard(), this.address.getCustomer().getIdCard())
                )
        );
    }

    @Test
    public void testMappingAddressToDTOWithoutCustomer () {
        final AddressDTO addressMapped = this.addressMapper.convertToDTO(this.address);

        assertAll(
                () -> assertEquals(addressMapped.getId(), this.address.getId()),
                () -> assertEquals(addressMapped.getCountry(), this.address.getCountry()),
                () -> assertEquals(addressMapped.getState(), this.address.getState()),
                () -> assertEquals(addressMapped.getCity(), this.address.getCity()),
                () -> assertEquals(addressMapped.getStreet(), this.address.getStreet()),
                () -> assertEquals(addressMapped.getStreetNumber(), this.address.getStreetNumber()),
                () -> assertEquals(addressMapped.getPostalCode(), this.address.getPostalCode())
        );
    }

    @Test
    public void testMappingAddressToEntityWithCustomer () {
        this.addressDTO.setCustomer(this.customerDTO);

        final Address addressMapped = this.addressMapper.convertToEntity(this.addressDTO);

        assertAll(
                () -> assertEquals(addressMapped.getId(), this.addressDTO.getId()),
                () -> assertEquals(addressMapped.getCountry(), this.addressDTO.getCountry()),
                () -> assertEquals(addressMapped.getState(), this.addressDTO.getState()),
                () -> assertEquals(addressMapped.getCity(), this.addressDTO.getCity()),
                () -> assertEquals(addressMapped.getStreet(), this.addressDTO.getStreet()),
                () -> assertEquals(addressMapped.getStreetNumber(), this.addressDTO.getStreetNumber()),
                () -> assertEquals(addressMapped.getPostalCode(), this.addressDTO.getPostalCode()),
                () -> assertAll(
                        () -> assertEquals(addressMapped.getCustomer().getTaxCode(), this.addressDTO.getCustomer().getTaxCode()),
                        () -> assertEquals(addressMapped.getCustomer().getFirstName(), this.addressDTO.getCustomer().getFirstName()),
                        () -> assertEquals(addressMapped.getCustomer().getLastName(), this.addressDTO.getCustomer().getLastName()),
                        () -> assertEquals(addressMapped.getCustomer().getBirthDate(), this.addressDTO.getCustomer().getBirthDate()),
                        () -> assertEquals(addressMapped.getCustomer().getEmailVerifiedAt(), this.addressDTO.getCustomer().getEmailVerifiedAt()),
                        () -> assertEquals(addressMapped.getCustomer().getEmail(), this.addressDTO.getCustomer().getEmail()),
                        () -> assertEquals(addressMapped.getCustomer().getPassword(), this.addressDTO.getCustomer().getPassword()),
                        () -> assertEquals(addressMapped.getCustomer().getIdCard(), this.addressDTO.getCustomer().getIdCard())
                )
        );
    }

    @Test
    public void testMappingAddressToEntityWithoutCustomer () {
        final Address addressMapped = this.addressMapper.convertToEntity(this.addressDTO);

        assertAll(
                () -> assertEquals(addressMapped.getId(), this.addressDTO.getId()),
                () -> assertEquals(addressMapped.getCountry(), this.addressDTO.getCountry()),
                () -> assertEquals(addressMapped.getState(), this.addressDTO.getState()),
                () -> assertEquals(addressMapped.getCity(), this.addressDTO.getCity()),
                () -> assertEquals(addressMapped.getStreet(), this.addressDTO.getStreet()),
                () -> assertEquals(addressMapped.getStreetNumber(), this.addressDTO.getStreetNumber()),
                () -> assertEquals(addressMapped.getPostalCode(), this.addressDTO.getPostalCode())
        );
    }
}
