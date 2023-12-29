package com.edoardo.bbs.services.address;

import com.edoardo.bbs.dtos.AddressDTO;
import com.edoardo.bbs.dtos.CustomerDTO;
import com.edoardo.bbs.entities.Address;
import com.edoardo.bbs.entities.Customer;
import com.edoardo.bbs.exceptions.ResourceNotFoundException;
import com.edoardo.bbs.mapper.CustomerMapper;
import com.edoardo.bbs.repositories.CustomerRepository;
import com.edoardo.bbs.services.implementation.CustomerServiceImpl;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.time.ZoneId;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class AddAddressTest {

    @Mock
    private CustomerMapper customerMapper;
    @Mock
    private CustomerRepository customerRepository;
    @InjectMocks
    private CustomerServiceImpl customerService;

    private Faker faker;
    private Customer customer;

    @BeforeEach
    public void init () {
        this.faker = new Faker();
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
    }

    @Test
    public void testAddAddressToNotExistingCustomerThrowsException () {
        final AddressDTO address = AddressDTO.builder()
                .country(this.faker.address().country())
                .state(this.faker.address().state())
                .city(this.faker.address().city())
                .street(this.faker.address().streetName())
                .streetNumber(Integer.parseInt(this.faker.address().streetAddressNumber()))
                .postalCode(this.faker.address().zipCode())
                .build();

        when(this.customerRepository.findById(customer.getTaxCode())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> this.customerService.addAddress(customer.getTaxCode(), address));
    }

    @Test
    public void testAddAddressToCustomerWithoutOneReturnsCustomer () {
        final AddressDTO address = AddressDTO.builder()
                .country(this.faker.address().country())
                .state(this.faker.address().state())
                .city(this.faker.address().city())
                .street(this.faker.address().streetName())
                .streetNumber(Integer.parseInt(this.faker.address().streetAddressNumber()))
                .postalCode(this.faker.address().zipCode())
                .build();

        when(this.customerMapper.convertToDTO(customer)).thenReturn(this.mapToDto(customer));
        when(this.customerRepository.findById(customer.getTaxCode())).thenReturn(Optional.of(customer));
        when(this.customerRepository.save(Mockito.any(Customer.class))).thenReturn(customer);

        final CustomerDTO updatedCustomer = this.customerService.addAddress(customer.getTaxCode(), address);

        assertThat(updatedCustomer).isNotNull();
        assertThat(updatedCustomer.getTaxCode()).isEqualTo(customer.getTaxCode());
        assertThat(updatedCustomer.getFirstName()).isEqualTo(customer.getFirstName());
        assertThat(updatedCustomer.getLastName()).isEqualTo(customer.getLastName());
        assertThat(updatedCustomer.getBirthDate()).isEqualTo(customer.getBirthDate());
        assertThat(updatedCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(updatedCustomer.getEmailVerifiedAt()).isEqualTo(customer.getEmailVerifiedAt());
        assertThat(updatedCustomer.getPassword()).isEqualTo(customer.getPassword());
        assertThat(updatedCustomer.getIdCard()).isEqualTo(customer.getIdCard());
        assertThat(customer.getAddresses().size()).isEqualTo(1);
    }

    @Test
    public void testAddAddressToCustomerWithOneReturnsCustomer () {
        this.customer.setAddresses(Set.of(this.randomAddress()));
        final AddressDTO address = AddressDTO.builder()
                .country(this.faker.address().country())
                .state(this.faker.address().state())
                .city(this.faker.address().city())
                .street(this.faker.address().streetName())
                .streetNumber(Integer.parseInt(this.faker.address().streetAddressNumber()))
                .postalCode(this.faker.address().zipCode())
                .build();

        when(this.customerMapper.convertToDTO(customer)).thenReturn(this.mapToDto(customer));
        when(this.customerRepository.findById(customer.getTaxCode())).thenReturn(Optional.of(customer));
        when(this.customerRepository.save(Mockito.any(Customer.class))).thenReturn(customer);

        final CustomerDTO updatedCustomer = this.customerService.addAddress(customer.getTaxCode(), address);

        assertThat(updatedCustomer).isNotNull();
        assertThat(updatedCustomer.getTaxCode()).isEqualTo(customer.getTaxCode());
        assertThat(updatedCustomer.getFirstName()).isEqualTo(customer.getFirstName());
        assertThat(updatedCustomer.getLastName()).isEqualTo(customer.getLastName());
        assertThat(updatedCustomer.getBirthDate()).isEqualTo(customer.getBirthDate());
        assertThat(updatedCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(updatedCustomer.getEmailVerifiedAt()).isEqualTo(customer.getEmailVerifiedAt());
        assertThat(updatedCustomer.getPassword()).isEqualTo(customer.getPassword());
        assertThat(updatedCustomer.getIdCard()).isEqualTo(customer.getIdCard());
        assertThat(customer.getAddresses().size()).isEqualTo(2);
    }

    @Test
    public void testAddAddressToCustomerWithAlreadyThreeThrowsException () {
        this.customer.setAddresses(Set.of(this.randomAddress(), this.randomAddress(), this.randomAddress()));
        final AddressDTO address = AddressDTO.builder()
                .country(this.faker.address().country())
                .state(this.faker.address().state())
                .city(this.faker.address().city())
                .street(this.faker.address().streetName())
                .streetNumber(Integer.parseInt(this.faker.address().streetAddressNumber()))
                .postalCode(this.faker.address().zipCode())
                .build();

        when(this.customerMapper.convertToDTO(customer)).thenReturn(this.mapToDto(customer));
        when(this.customerRepository.findById(customer.getTaxCode())).thenReturn(Optional.of(customer));
        when(this.customerRepository.save(Mockito.any(Customer.class))).thenThrow(new MaximumAddressNumberException());

        assertThrows(MaximumAddressNumberException.class, () -> this.customerService.addAddress(customer.getTaxCode(), address));
    }

    private CustomerDTO mapToDto(Customer customer) {
        if (customer != null) {
            final Set<AddressDTO> addresses = new HashSet<>();
            customer.getAddresses().forEach((entity) -> {
                final AddressDTO address = AddressDTO.builder()
                        .country(entity.getCountry())
                        .state(entity.getState())
                        .city(entity.getCity())
                        .street(entity.getStreet())
                        .streetNumber(entity.getStreetNumber())
                        .postalCode(entity.getPostalCode())
                        .build();
                addresses.add(address);
            });
            return CustomerDTO.builder()
                    .taxCode(customer.getTaxCode())
                    .firstName(customer.getFirstName())
                    .lastName(customer.getLastName())
                    .birthDate(customer.getBirthDate())
                    .email(customer.getEmail())
                    .emailVerifiedAt(customer.getEmailVerifiedAt())
                    .password(customer.getPassword())
                    .idCard(customer.getIdCard())
                    .addresses(addresses)
                    .build();
        }
        return null;
    }

    private Address randomAddress () {
        return Address.builder()
                .country(this.faker.address().country())
                .state(this.faker.address().state())
                .city(this.faker.address().city())
                .street(this.faker.address().streetName())
                .streetNumber(Integer.parseInt(this.faker.address().streetAddressNumber()))
                .postalCode(this.faker.address().zipCode())
                .build();
    }
}
