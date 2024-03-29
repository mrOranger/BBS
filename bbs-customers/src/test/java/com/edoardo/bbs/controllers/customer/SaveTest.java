package com.edoardo.bbs.controllers.customer;

import com.edoardo.bbs.controllers.api.v1.CustomerController;
import com.edoardo.bbs.dtos.AddressDTO;
import com.edoardo.bbs.dtos.CustomerDTO;
import com.edoardo.bbs.exceptions.ResourceConflictException;
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
public class SaveTest {

    @MockBean
    private final CustomerService customerService;

    private final MockMvc mockMvc;
    private CustomerDTO customer;
    private final ObjectMapper mapper;
    private final Faker faker;

    @Autowired
    public SaveTest (MockMvc mockMvc, CustomerService customerService, ObjectMapper mapper) {
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
    public void testSaveCustomerReturnsConflictException () {
        when(this.customerService.createCustomer(this.customer)).thenThrow(new ResourceConflictException("Conflict."));

        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/customers")
                .content(this.mapper.writeValueAsString(this.customer))
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.message", CoreMatchers.is("Conflict.")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test @SneakyThrows
    public void testSaveCustomerWithoutFirstNameReturnsValidationException () {
        this.customer.setFirstName(null);

        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/customers")
                .content(this.mapper.writeValueAsString(this.customer))
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.message", CoreMatchers.is("Customer first name must not be null.")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test @SneakyThrows
    public void testSaveCustomerWithoutLastNameReturnsValidationException () {
        this.customer.setLastName(null);

        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/customers")
                .content(this.mapper.writeValueAsString(this.customer))
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.message", CoreMatchers.is("Customer last name must not be null.")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test @SneakyThrows
    public void testSaveCustomerWithoutEmailReturnsValidationException () {
        this.customer.setEmail(null);

        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/customers")
                .content(this.mapper.writeValueAsString(this.customer))
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.message", CoreMatchers.is("Customer email must not be null.")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test @SneakyThrows
    public void testSaveCustomerWithoutPasswordReturnsValidationException () {
        this.customer.setPassword(null);

        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/customers")
                .content(this.mapper.writeValueAsString(this.customer))
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.message", CoreMatchers.is("Customer password must not be null.")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test @SneakyThrows
    public void testSaveCustomerWithoutIdCardReturnsValidationException () {
        this.customer.setIdCard(null);

        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/customers")
                .content(this.mapper.writeValueAsString(this.customer))
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.message", CoreMatchers.is("Customer id card must not be null.")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test @SneakyThrows
    public void testSaveCustomerWithBlankFirstNameReturnsValidationException () {
        this.customer.setFirstName("");

        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/customers")
                .content(this.mapper.writeValueAsString(this.customer))
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.message", CoreMatchers.is("Customer first name length must be between 1 and 50 chars.")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test @SneakyThrows
    public void testSaveCustomerWithBlankLastNameReturnsValidationException () {
        this.customer.setLastName("");

        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/customers")
                .content(this.mapper.writeValueAsString(this.customer))
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.message", CoreMatchers.is("Customer last name length must be between 1 and 50 chars.")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test @SneakyThrows
    public void testSaveCustomerWithBlankEmailReturnsValidationException () {
        this.customer.setEmail("");

        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/customers")
                .content(this.mapper.writeValueAsString(this.customer))
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.message", CoreMatchers.is("Customer email length must be between 1 and 50 chars.")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test @SneakyThrows
    public void testSaveCustomerWithBlankPasswordReturnsValidationException () {
        this.customer.setPassword("");

        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/customers")
                .content(this.mapper.writeValueAsString(this.customer))
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.
                        jsonPath("$.message", CoreMatchers.is("Customer password length must be between 1 and 50 chars.")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test @SneakyThrows
    public void testSaveCustomerWithBlankIdCardReturnsValidationException () {
        this.customer.setIdCard("");

        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/customers")
                .content(this.mapper.writeValueAsString(this.customer))
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.message", CoreMatchers.is("Customer id card length must not be empty.")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test @SneakyThrows
    public void testSaveCustomerWithInvalidFirstNameLengthReturnsValidationException () {
        this.customer.setFirstName(faker.lorem().characters(51));

        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/customers")
                .content(this.mapper.writeValueAsString(this.customer))
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.message", CoreMatchers.is("Customer first name length must be between 1 and 50 chars.")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test @SneakyThrows
    public void testSaveCustomerWithInvalidLastNameLengthReturnsValidationException () {
        this.customer.setLastName(faker.lorem().characters(51));

        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/customers")
                .content(this.mapper.writeValueAsString(this.customer))
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.message", CoreMatchers.is("Customer last name length must be between 1 and 50 chars.")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test @SneakyThrows
    public void testSaveCustomerWithInvalidEmailLengthReturnsValidationException () {
        this.customer.setEmail("notValidEmailAddress.notValidDomainAddress@NotValidEmail.com");

        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/customers")
                .content(this.mapper.writeValueAsString(this.customer))
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.message", CoreMatchers.is("Customer email length must be between 1 and 50 chars.")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test @SneakyThrows
    public void testSaveCustomerWithInvalidPasswordLengthReturnsValidationException () {
        this.customer.setPassword(this.faker.lorem().characters(51));

        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/customers")
                .content(this.mapper.writeValueAsString(this.customer))
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Customer password length must be between 1 and 50 chars.")))
                .andDo(MockMvcResultHandlers.print());
    }
    @Test @SneakyThrows
    public void testSaveCustomerWithInvalidEmailReturnsValidationException () {
        this.customer.setEmail(this.faker.lorem().characters(20));

        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/customers")
                .content(this.mapper.writeValueAsString(this.customer))
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.message", CoreMatchers.is("Customer email must be valid.")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test @SneakyThrows
    public void testSaveCustomerNotAdultReturnsValidationException () {
        this.customer.setBirthDate(this.faker.date().birthday(10, 17).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());

        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/customers")
                .content(this.mapper.writeValueAsString(this.customer))
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.message", CoreMatchers.is("Customer must be adult.")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test @SneakyThrows
    public void testSaveCustomerReturnsCreatedResponse () {
        when(this.customerService.createCustomer(this.customer)).thenReturn(this.customer);

        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/customers")
                .content(this.mapper.writeValueAsString(this.customer))
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().exists("Location"))
                .andDo(MockMvcResultHandlers.print());
    }
}
