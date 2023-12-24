package com.edoardo.bbs.services.customer;

import com.edoardo.bbs.dtos.CustomerDTO;
import com.edoardo.bbs.dtos.CustomerResponse;
import com.edoardo.bbs.entities.Customer;
import com.edoardo.bbs.repositories.CustomerRepository;
import com.edoardo.bbs.services.implementation.CustomerServiceImpl;
import com.github.javafaker.Faker;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FindAllTest {

    private final Faker faker;
    private final int maxRandomElements;
    private final Pageable pageable;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    public FindAllTest() {
        this.faker = new Faker();
        this.pageable = Pageable.ofSize(10);
        this.maxRandomElements = (int)((Math.random() * 10) + 1);
    }

    @Test
    public void CustomerService_findAll_returnsEmptyCustomerList () {
        final Page<Customer> customers = Mockito.mock(Page.class);

        when(this.customerRepository.findAll(Mockito.any(Pageable.class))).thenReturn(customers);
        final CustomerResponse customerResponse = this.customerService.getAllCustomers(this.pageable);

        Assertions.assertThat(customerResponse).isNotNull();
        Assertions.assertThat(customerResponse.getTotalElements()).isEqualTo(0);
    }
}
