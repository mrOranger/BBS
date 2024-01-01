package com.edoardo.bbs.services.address;

import com.edoardo.bbs.dtos.AddressDTO;
import com.edoardo.bbs.entities.Address;
import com.edoardo.bbs.entities.Customer;
import com.edoardo.bbs.exceptions.MaximumAddressNumberException;
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
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZoneId;
import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class AddAddressTest {

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

    public AddAddressTest() {
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
                .country(this.faker.address().country())
                .state(this.faker.address().state())
                .city(this.faker.address().city())
                .street(this.faker.address().streetName())
                .streetNumber(Integer.parseInt(this.faker.address().streetAddressNumber()))
                .postalCode(this.faker.address().zipCode())
                .build();
    }

    @Test
    public void testAddAddressToNotExistingCustomerThrowsException () {
        when(this.customerRepository.findById(this.customer.getTaxCode())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> this.addressService.addAddress(this.customer.getTaxCode(), this.mapToDto(this.address)));
    }

    @Test
    public void testAddAddressToCustomerWithAlreadyThreeThrowsException () {
        for (int i = 0; i < 2; i++) {
            this.customer.getAddresses().add(Address.builder()
                    .country(this.faker.address().country())
                    .state(this.faker.address().state())
                    .city(this.faker.address().city())
                    .street(this.faker.address().streetName())
                    .streetNumber(Integer.parseInt(this.faker.address().streetAddressNumber()))
                    .postalCode(this.faker.address().zipCode())
                    .build());
        }

        when(this.addressRepository.save(this.address)).thenReturn(this.address);
        when(this.addressMapper.convertToDTO(this.address)).thenReturn(this.mapToDto(this.address));
        when(this.customerRepository.findById(this.customer.getTaxCode())).thenReturn(Optional.ofNullable(this.customer));

        assertThrows(MaximumAddressNumberException.class, () -> this.addressService.addAddress(this.customer.getTaxCode(), this.mapToDto(this.address)));
    }

    @Test
    public void testAddAddressToCustomerWithoutOneReturnsCustomer () throws MaximumAddressNumberException, ResourceNotFoundException {
        when(this.addressRepository.save(this.address)).thenReturn(this.address);
        when(this.addressMapper.convertToDTO(this.address)).thenReturn(this.mapToDto(this.address));
        when(this.customerRepository.findById(this.customer.getTaxCode())).thenReturn(Optional.ofNullable(this.customer));

        final AddressDTO address = this.addressService.addAddress(this.customer.getTaxCode(), this.mapToDto(this.address));

        assertThat(address).isNotNull();
        assertThat(address.getCustomer().getTaxCode()).isEqualTo(this.customer.getTaxCode());
        assertThat(this.customer.getAddresses().size()).isEqualTo(1);
    }

    @Test
    public void testAddAddressToCustomerWithOneReturnsCustomer () throws MaximumAddressNumberException, ResourceNotFoundException {
        this.customer.getAddresses().add(Address.builder()
                .country(this.faker.address().country())
                .state(this.faker.address().state())
                .city(this.faker.address().city())
                .street(this.faker.address().streetName())
                .streetNumber(Integer.parseInt(this.faker.address().streetAddressNumber()))
                .postalCode(this.faker.address().zipCode())
                .build());

        when(this.addressRepository.save(this.address)).thenReturn(this.address);
        when(this.addressMapper.convertToDTO(this.address)).thenReturn(this.mapToDto(this.address));
        when(this.customerRepository.findById(this.customer.getTaxCode())).thenReturn(Optional.ofNullable(this.customer));
        final AddressDTO address = this.addressService.addAddress(this.customer.getTaxCode(), this.mapToDto(this.address));

        assertThat(address).isNotNull();
        assertThat(address.getCustomer().getTaxCode()).isEqualTo(this.customer.getTaxCode());
        assertThat(this.customer.getAddresses().size()).isEqualTo(2);
    }

    private AddressDTO mapToDto(Address address) {
        if (address != null) {
            return AddressDTO.builder()
                    .country(address.getCountry())
                    .state(address.getState())
                    .city(address.getCity())
                    .street(address.getStreet())
                    .streetNumber(address.getStreetNumber())
                    .postalCode(address.getPostalCode())
                    .build();
        }
        return null;
    }
}
