package com.edoardo.bbs.services.address;

import com.edoardo.bbs.dtos.AddressDTO;
import com.edoardo.bbs.dtos.CustomerDTO;
import com.edoardo.bbs.entities.Address;
import com.edoardo.bbs.entities.Customer;
import com.edoardo.bbs.exceptions.MaximumAddressNumberException;
import com.edoardo.bbs.exceptions.ResourceNotFoundException;
import com.edoardo.bbs.mapper.CustomerMapper;
import com.edoardo.bbs.repositories.CustomerRepository;
import com.edoardo.bbs.services.implementation.CustomerServiceImpl;
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
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
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
    }

    @Test
    public void testAddAddressToNotExistingCustomerThrowsException () {
    }

    @Test
    public void testAddAddressToCustomerWithAlreadyThreeThrowsException () {
    }

    @Test
    public void testAddAddressToCustomerWithoutOneReturnsCustomer () throws MaximumAddressNumberException, ResourceNotFoundException {
    }

    @Test
    public void testAddAddressToCustomerWithOneReturnsCustomer () throws MaximumAddressNumberException, ResourceNotFoundException {
    }
}
