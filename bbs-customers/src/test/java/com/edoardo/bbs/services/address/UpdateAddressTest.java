package com.edoardo.bbs.services.address;

import com.edoardo.bbs.dtos.AddressDTO;
import com.edoardo.bbs.dtos.CustomerDTO;
import com.edoardo.bbs.entities.Address;
import com.edoardo.bbs.entities.Customer;
import com.edoardo.bbs.exceptions.ResourceNotFoundException;
import com.edoardo.bbs.mapper.AddressMapper;
import com.edoardo.bbs.repositories.AddressRepository;
import com.edoardo.bbs.repositories.CustomerRepository;
import com.edoardo.bbs.services.implementation.AddressServiceImpl;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZoneId;
import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class UpdateAddressTest {

    @Mock
    private AddressMapper addressMapper;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private AddressRepository addressRepository;
    @InjectMocks
    private AddressServiceImpl addressService;

    private final Faker faker;
    private Customer customer;
    private Address address;

    public UpdateAddressTest() {
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

        this.address = Address.builder()
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
    public void testUpdateAddressToNotExistingCustomerThrowsResourceNotFoundException () {
        when(this.customerRepository.findById(this.customer.getTaxCode())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                this.addressService.updateAddress(
                        this.customer.getTaxCode(),
                        this.address.getId().toString(),
                        this.mapToDto(this.address)
                ));
    }

    @Test
    public void testUpdateAddressToCustomerWithoutOneThrowsResourceNotFoundException () {
        when(this.customerRepository.findById(this.customer.getTaxCode())).thenReturn(Optional.ofNullable(this.customer));

        assertThrows(ResourceNotFoundException.class, () ->
                this.addressService.updateAddress(
                        this.customer.getTaxCode(),
                        this.address.getId().toString(),
                        this.mapToDto(this.address)
                ));
    }

    @Test
    public void testUpdateAddressToCustomerWithOneReturnsAddress () throws ResourceNotFoundException {
        this.customer.getAddresses().add(this.address);

        when(this.customerRepository.findById(this.customer.getTaxCode())).thenReturn(Optional.ofNullable(this.customer));
        when(this.addressRepository.findById(address.getId().toString())).thenReturn(Optional.ofNullable(this.address));
        when(this.addressMapper.convertToDTO(Mockito.any(Address.class))).thenReturn(this.mapToDto(address));
        when(this.addressMapper.convertToEntity(Mockito.any(AddressDTO.class))).thenReturn(address);
        when(this.addressRepository.save(address)).thenReturn(this.address);

        final AddressDTO address = this.addressService.updateAddress(
                this.customer.getTaxCode(),
                this.address.getId().toString(),
                this.mapToDto(this.address)
        );

        assertThat(address).isNotNull();
        assertThat(address.getStreet()).isEqualTo(this.address.getStreet());
        assertThat(address.getPostalCode()).isEqualTo(this.address.getPostalCode());
        assertThat(address.getState()).isEqualTo(this.address.getState());
        assertThat(address.getCountry()).isEqualTo(this.address.getCountry());
        assertThat(address.getStreetNumber()).isEqualTo(this.address.getStreetNumber());
    }

    private AddressDTO mapToDto(Address address) {
        if (address != null) {
            return AddressDTO.builder()
                    .id(address.getId())
                    .country(address.getCountry())
                    .state(address.getState())
                    .city(address.getCity())
                    .street(address.getStreet())
                    .streetNumber(address.getStreetNumber())
                    .postalCode(address.getPostalCode())
                    .customer(this.mapCustomerToDto(address.getCustomer()))
                    .build();
        }
        return null;
    }

    private CustomerDTO mapCustomerToDto (Customer customer) {
        if (customer != null) {
            return CustomerDTO.builder()
                    .taxCode(customer.getTaxCode())
                    .firstName(customer.getFirstName())
                    .lastName(customer.getLastName())
                    .birthDate(customer.getBirthDate())
                    .email(customer.getEmail())
                    .emailVerifiedAt(customer.getEmailVerifiedAt())
                    .password(customer.getPassword())
                    .idCard(customer.getIdCard())
                    .build();
        }
        return null;
    }
}
