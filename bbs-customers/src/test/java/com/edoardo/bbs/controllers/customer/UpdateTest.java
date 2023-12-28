package com.edoardo.bbs.controllers.customer;

import com.edoardo.bbs.controllers.api.v1.CustomerController;
import com.edoardo.bbs.dtos.AddressDTO;
import com.edoardo.bbs.dtos.CustomerDTO;
import com.edoardo.bbs.exceptions.ResourceNotFoundException;
import com.edoardo.bbs.services.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.when;

@WebMvcTest(controllers = CustomerController.class) @AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class UpdateTest {
    @MockBean
    private CustomerService customerService;

    private CustomerDTO customer;
    private ObjectMapper mapper;

    private final MockMvc mockMvc;
    private final UriComponentsBuilder componentsBuilder;

    @Autowired
    public UpdateTest (MockMvc mockMvc, CustomerService customerService, ObjectMapper mapper, UriComponentsBuilder componentsBuilder) {
        this.componentsBuilder = componentsBuilder;
        this.customerService = customerService;
        this.mockMvc = mockMvc;
        this.mapper = mapper;
    }

    @BeforeEach
    public void init () {
        Faker faker = new Faker();
        Set<AddressDTO> addresses = new HashSet<>();
        final AddressDTO newAddress = AddressDTO.builder().country(faker.address().country())
                .state(faker.address().state())
                .city(faker.address().city())
                .street(faker.address().streetName())
                .streetNumber(Integer.parseInt(faker.address().streetAddressNumber()))
                .postalCode(faker.address().zipCode())
                .build();
        addresses.add(newAddress);
        this.customer = CustomerDTO.builder().taxCode(faker.code().isbn10())
                .firstName(faker.name().firstName())
                .lastName(faker.name().lastName())
                .email(faker.internet().emailAddress())
                .birthDate(faker.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                .emailVerifiedAt(faker.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                .password(faker.internet().password())
                .idCard(faker.file().toString())
                .addresses(addresses)
                .build();
    }

    @Test
    public void testUpdateCustomerReturnsNotFoundException () throws Exception {
        when(this.customerService.updateCustomer(this.customer)).thenThrow(new ResourceNotFoundException("Not found."));
        final URI customerURI = this.componentsBuilder.path("/customers/{id}").buildAndExpand(customer.getTaxCode()).toUri();

        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders.put(customerURI)
                .content(this.mapper.writeValueAsString(this.customer))
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Not found.")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testUpdateCustomerReturnsOkResponse () throws Exception {
        when(this.customerService.updateCustomer(this.customer)).thenReturn(this.customer);
        final URI customerURI = this.componentsBuilder.path("/customers/{id}").buildAndExpand(customer.getTaxCode()).toUri();

        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders.put(customerURI)
                .content(this.mapper.writeValueAsString(this.customer))
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.taxCode", CoreMatchers.is(this.customer.getTaxCode())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(this.customer.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is(this.customer.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.birthDate", CoreMatchers.is(this.customer.getBirthDate())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(this.customer.getEmail())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.emailVerifiedAt", CoreMatchers.is(this.customer.getEmailVerifiedAt())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password", CoreMatchers.is(this.customer.getPassword())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.idCard", CoreMatchers.is(this.customer.getIdCard())))
                .andDo(MockMvcResultHandlers.print());
    }
}
