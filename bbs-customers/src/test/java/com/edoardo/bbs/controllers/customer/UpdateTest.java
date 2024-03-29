package com.edoardo.bbs.controllers.customer;

import com.edoardo.bbs.controllers.api.v1.CustomerController;
import com.edoardo.bbs.dtos.AddressDTO;
import com.edoardo.bbs.dtos.CustomerDTO;
import com.edoardo.bbs.exceptions.ResourceNotFoundException;
import com.edoardo.bbs.services.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import lombok.SneakyThrows;
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

import java.time.ZoneId;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.when;

@WebMvcTest(controllers = CustomerController.class) @AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class UpdateTest {
    @MockBean
    private final CustomerService customerService;

    private final MockMvc mockMvc;
    private CustomerDTO customer;
    private final ObjectMapper mapper;
    private final Faker faker;
    private final String baseUrl;


    @Autowired
    public UpdateTest (MockMvc mockMvc, CustomerService customerService, ObjectMapper mapper) {
        this.baseUrl = "/api/v1/customers/";
        this.mapper = mapper;
        this.mockMvc = mockMvc;
        this.customerService = customerService;
        this.faker = new Faker();
    }

    @BeforeEach
    public void init () {
        Set<AddressDTO> addresses = new HashSet<>();
        addresses.add(AddressDTO.builder().country(this.faker.address().country())
                .state(this.faker.address().state())
                .city(this.faker.address().city())
                .street(this.faker.address().streetName())
                .streetNumber(Integer.parseInt(this.faker.address().streetAddressNumber()))
                .postalCode(this.faker.address().zipCode())
                .build());
        this.customer = CustomerDTO.builder().taxCode(this.faker.code().isbn10())
                .firstName(this.faker.name().firstName())
                .lastName(this.faker.name().lastName())
                .email(this.faker.internet().emailAddress())
                .birthDate(this.faker.date().birthday(20, 80).toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                .emailVerifiedAt(this.faker.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                .password(this.faker.internet().password())
                .idCard(this.faker.file().toString())
                .addresses(addresses)
                .build();
    }

    @Test @SneakyThrows
    public void testUpdateCustomerReturnsNotFoundException () {
        when(this.customerService.updateCustomer(this.customer.getTaxCode(), this.customer)).thenThrow(new ResourceNotFoundException("Not found."));

        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders
                .put(this.baseUrl + customer.getTaxCode())
                .content(this.mapper.writeValueAsString(this.customer))
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Not found.")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test @SneakyThrows
    public void testUpdateCustomerWithoutFirstNameReturnsValidationException () {
        this.customer.setFirstName(null);

        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders
                .put(this.baseUrl + customer.getTaxCode())
                .content(this.mapper.writeValueAsString(this.customer))
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Customer first name must not be null.")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test @SneakyThrows
    public void testUpdateCustomerWithoutLastNameReturnsValidationException () {
        this.customer.setLastName(null);

        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders
                .put(this.baseUrl + customer.getTaxCode())
                .content(this.mapper.writeValueAsString(this.customer))
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Customer last name must not be null.")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test @SneakyThrows
    public void testUpdateCustomerWithoutEmailReturnsValidationException () {
        this.customer.setEmail(null);

        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders
                .put(this.baseUrl + customer.getTaxCode())
                .content(this.mapper.writeValueAsString(this.customer))
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Customer email must not be null.")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test @SneakyThrows
    public void testUpdateCustomerWithoutPasswordReturnsValidationException () {
        this.customer.setPassword(null);

        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders
                .put(this.baseUrl + customer.getTaxCode())
                .content(this.mapper.writeValueAsString(this.customer))
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Customer password must not be null.")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test @SneakyThrows
    public void testUpdateCustomerWithoutIdCardReturnsValidationException () {
        this.customer.setIdCard(null);

        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders
                .put(this.baseUrl + customer.getTaxCode())
                .content(this.mapper.writeValueAsString(this.customer))
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Customer id card must not be null.")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test @SneakyThrows
    public void testUpdateCustomerWithBlankFirstNameReturnsValidationException () {
        this.customer.setFirstName("");

        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders
                .put(this.baseUrl + customer.getTaxCode())
                .content(this.mapper.writeValueAsString(this.customer))
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Customer first name length must be between 1 and 50 chars.")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test @SneakyThrows
    public void testUpdateCustomerWithBlankLastNameReturnsValidationException () {
        this.customer.setLastName("");

        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders
                .put(this.baseUrl + customer.getTaxCode())
                .content(this.mapper.writeValueAsString(this.customer))
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Customer last name length must be between 1 and 50 chars.")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test @SneakyThrows
    public void testUpdateCustomerWithBlankEmailReturnsValidationException () {
        this.customer.setEmail("");

        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders
                .put(this.baseUrl + customer.getTaxCode())
                .content(this.mapper.writeValueAsString(this.customer))
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Customer email length must be between 1 and 50 chars.")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test @SneakyThrows
    public void testUpdateCustomerWithBlankPasswordReturnsValidationException () {
        this.customer.setPassword("");

        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders
                .put(this.baseUrl + customer.getTaxCode())
                .content(this.mapper.writeValueAsString(this.customer))
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Customer password length must be between 1 and 50 chars.")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test @SneakyThrows
    public void testUpdateCustomerWithBlankIdCardReturnsValidationException () {
        this.customer.setIdCard("");

        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders
                .put(this.baseUrl + customer.getTaxCode())
                .content(this.mapper.writeValueAsString(this.customer))
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Customer id card length must not be empty.")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test @SneakyThrows
    public void testUpdateCustomerWithInvalidFirstNameLengthReturnsValidationException () {
        this.customer.setFirstName(faker.lorem().characters(51));

        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders
                .put(this.baseUrl + customer.getTaxCode())
                .content(this.mapper.writeValueAsString(this.customer))
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Customer first name length must be between 1 and 50 chars.")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test @SneakyThrows
    public void testUpdateCustomerWithInvalidLastNameLengthReturnsValidationException () {
        this.customer.setLastName(faker.lorem().characters(51));

        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders
                .put(this.baseUrl + customer.getTaxCode())
                .content(this.mapper.writeValueAsString(this.customer))
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Customer last name length must be between 1 and 50 chars.")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test @SneakyThrows
    public void testUpdateCustomerWithInvalidEmailLengthReturnsValidationException () {
        this.customer.setEmail("notValidEmailAddress.notValidDomainAddress@NotValidEmail.com");

        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders
                .put(this.baseUrl + customer.getTaxCode())
                .content(this.mapper.writeValueAsString(this.customer))
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Customer email length must be between 1 and 50 chars.")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test @SneakyThrows
    public void testUpdateCustomerWithInvalidPasswordLengthReturnsValidationException () {
        this.customer.setPassword(this.faker.lorem().characters(51));

        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders
                .put(this.baseUrl + customer.getTaxCode())
                .content(this.mapper.writeValueAsString(this.customer))
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Customer password length must be between 1 and 50 chars.")))
                .andDo(MockMvcResultHandlers.print());
    }
    @Test @SneakyThrows
    public void testUpdateCustomerWithInvalidEmailReturnsValidationException () {
        this.customer.setEmail(this.faker.lorem().characters(20));

        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders
                .put(this.baseUrl + customer.getTaxCode())
                .content(this.mapper.writeValueAsString(this.customer))
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Customer email must be valid.")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test @SneakyThrows
    public void testUpdateCustomerNotAdultReturnsValidationException () {
        this.customer.setBirthDate(this.faker.date().birthday(10, 17).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());

        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders
                .put(this.baseUrl + customer.getTaxCode())
                .content(this.mapper.writeValueAsString(this.customer))
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Customer must be adult.")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test @SneakyThrows
    public void testUpdateCustomerReturnsOkResponse () {
        when(this.customerService.updateCustomer(this.customer.getTaxCode(), this.customer)).thenReturn(this.customer);

        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders
                .put(this.baseUrl + customer.getTaxCode())
                .content(this.mapper.writeValueAsString(this.customer))
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.taxCode", CoreMatchers.is(this.customer.getTaxCode())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(this.customer.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is(this.customer.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(this.customer.getEmail())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password", CoreMatchers.is(this.customer.getPassword())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.idCard", CoreMatchers.is(this.customer.getIdCard())))
                .andDo(MockMvcResultHandlers.print());
    }
}
