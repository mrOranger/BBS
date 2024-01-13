package com.edoardo.bbs.controllers.address;


import com.edoardo.bbs.controllers.api.v1.AddressController;
import com.edoardo.bbs.dtos.AddressDTO;
import com.edoardo.bbs.exceptions.MaximumAddressNumberException;
import com.edoardo.bbs.exceptions.ResourceNotFoundException;
import com.edoardo.bbs.services.AddressService;
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

import static org.mockito.Mockito.when;

@WebMvcTest(controllers = AddressController.class) @AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class AddAddressTest {
    @MockBean
    private final AddressService addressService;

    private final ObjectMapper mapper;
    private AddressDTO addressDTO;
    private final MockMvc mockMvc;
    private final Faker faker;

    @Autowired
    public AddAddressTest (ObjectMapper objectMapper, MockMvc mockMvc, AddressService addressService) {
        this.addressService = addressService;
        this.mapper = objectMapper;
        this.faker = new Faker();
        this.mockMvc = mockMvc;
    }

    @BeforeEach
    public void init () {
        this.addressDTO = AddressDTO.builder()
                .state(this.faker.address().state())
                .city(this.faker.address().city())
                .country(this.faker.address().country())
                .street(this.faker.address().streetName())
                .streetNumber(Integer.parseInt(this.faker.address().streetAddressNumber()))
                .postalCode(this.faker.address().zipCode())
                .build();
    }

    @Test
    public void testAddAddressToNotExistingCustomerReturnsNotFound () throws Exception {
        final String taxCode = this.faker.code().isbn10();
        when(this.addressService.addAddress(taxCode, this.addressDTO)).thenThrow(new ResourceNotFoundException("Customer " + taxCode + " not found."));


        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/addresses/customer/{taxCode}", taxCode)
                .content(this.mapper.writeValueAsString(this.addressDTO))
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Customer " + taxCode + " not found.")));
    }

    @Test
    public void testAddAddressToCustomerWithAlreadyThreeAddressReturnsBadRequest () throws Exception{
        final String taxCode = this.faker.code().isbn10();
        when(this.addressService.addAddress(taxCode, this.addressDTO)).thenThrow(new MaximumAddressNumberException("Maximum number of addresses."));


        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/addresses/customer/{taxCode}", taxCode)
                .content(this.mapper.writeValueAsString(this.addressDTO))
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Maximum number of addresses.")));
    }

    @Test
    public void testAddAddressToCustomerWithoutCountryReturnsBadRequest () throws Exception {
        final String taxCode = this.faker.code().isbn10();
        this.addressDTO.setCountry(null);
        AddressDTO savedAddress = this.addressDTO;
        savedAddress.setId(this.faker.number().randomDigit());
        when(this.addressService.addAddress(taxCode, this.addressDTO)).thenReturn(savedAddress);

        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/addresses/customer/{taxCode}", taxCode)
                .content(this.mapper.writeValueAsString(this.addressDTO))
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Address country must be not empty.")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testAddAddressToCustomerWithInvalidCountryReturnsBadRequest () throws Exception {
        final String taxCode = this.faker.code().isbn10();
        this.addressDTO.setCountry(this.faker.lorem().sentence(200));
        AddressDTO savedAddress = this.addressDTO;
        savedAddress.setId(this.faker.number().randomDigit());
        when(this.addressService.addAddress(taxCode, this.addressDTO)).thenReturn(savedAddress);

        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/addresses/customer/{taxCode}", taxCode)
                .content(this.mapper.writeValueAsString(this.addressDTO))
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Address country must be at most 100 character long.")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testAddAddressToCustomerWithoutStateReturnsBadRequest () throws Exception {
        final String taxCode = this.faker.code().isbn10();
        this.addressDTO.setState(null);
        AddressDTO savedAddress = this.addressDTO;
        savedAddress.setId(this.faker.number().randomDigit());
        when(this.addressService.addAddress(taxCode, this.addressDTO)).thenReturn(savedAddress);

        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/addresses/customer/{taxCode}", taxCode)
                .content(this.mapper.writeValueAsString(this.addressDTO))
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Address state must be not empty.")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testAddAddressToCustomerWithInvalidStateReturnsBadRequest () throws Exception {
        final String taxCode = this.faker.code().isbn10();
        this.addressDTO.setState(this.faker.lorem().sentence(200));
        AddressDTO savedAddress = this.addressDTO;
        savedAddress.setId(this.faker.number().randomDigit());
        when(this.addressService.addAddress(taxCode, this.addressDTO)).thenReturn(savedAddress);

        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/addresses/customer/{taxCode}", taxCode)
                .content(this.mapper.writeValueAsString(this.addressDTO))
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Address state must be at most 100 character long.")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testAddAddressToCustomerWithoutCityReturnsBadRequest () throws Exception {
        final String taxCode = this.faker.code().isbn10();
        this.addressDTO.setCity(null);
        AddressDTO savedAddress = this.addressDTO;
        savedAddress.setId(this.faker.number().randomDigit());
        when(this.addressService.addAddress(taxCode, this.addressDTO)).thenReturn(savedAddress);

        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/addresses/customer/{taxCode}", taxCode)
                .content(this.mapper.writeValueAsString(this.addressDTO))
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Address city must be not empty.")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testAddAddressToCustomerWithInvalidCityReturnsBadRequest () throws Exception {
        final String taxCode = this.faker.code().isbn10();
        this.addressDTO.setCity(this.faker.lorem().sentence(200));
        AddressDTO savedAddress = this.addressDTO;
        savedAddress.setId(this.faker.number().randomDigit());
        when(this.addressService.addAddress(taxCode, this.addressDTO)).thenReturn(savedAddress);

        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/addresses/customer/{taxCode}", taxCode)
                .content(this.mapper.writeValueAsString(this.addressDTO))
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Address city must be at most 100 character long.")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testAddAddressToCustomerWithoutStreetReturnsBadRequest () throws Exception {
        final String taxCode = this.faker.code().isbn10();
        this.addressDTO.setStreet(null);
        AddressDTO savedAddress = this.addressDTO;
        savedAddress.setId(this.faker.number().randomDigit());
        when(this.addressService.addAddress(taxCode, this.addressDTO)).thenReturn(savedAddress);

        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/addresses/customer/{taxCode}", taxCode)
                .content(this.mapper.writeValueAsString(this.addressDTO))
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Address street must be not empty.")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testAddAddressToCustomerWithInvalidStreetReturnsBadRequest () throws Exception {
        final String taxCode = this.faker.code().isbn10();
        this.addressDTO.setStreet(this.faker.lorem().sentence(200));
        AddressDTO savedAddress = this.addressDTO;
        savedAddress.setId(this.faker.number().randomDigit());
        when(this.addressService.addAddress(taxCode, this.addressDTO)).thenReturn(savedAddress);

        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/addresses/customer/{taxCode}", taxCode)
                .content(this.mapper.writeValueAsString(this.addressDTO))
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Address street must be at most 100 character long.")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testAddAddressToCustomerWithoutStreetNumberReturnsBadRequest () throws Exception {
        final String taxCode = this.faker.code().isbn10();
        this.addressDTO.setStreetNumber(null);
        AddressDTO savedAddress = this.addressDTO;
        savedAddress.setId(this.faker.number().randomDigit());
        when(this.addressService.addAddress(taxCode, this.addressDTO)).thenReturn(savedAddress);

        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/addresses/customer/{taxCode}", taxCode)
                .content(this.mapper.writeValueAsString(this.addressDTO))
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Address street number must be not empty.")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testAddAddressToCustomerWithInvalidStreetNumberReturnsBadRequest () throws Exception {
        final String taxCode = this.faker.code().isbn10();
        this.addressDTO.setStreetNumber(-1);
        AddressDTO savedAddress = this.addressDTO;
        savedAddress.setId(this.faker.number().randomDigit());
        when(this.addressService.addAddress(taxCode, this.addressDTO)).thenReturn(savedAddress);

        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/addresses/customer/{taxCode}", taxCode)
                .content(this.mapper.writeValueAsString(this.addressDTO))
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Address street number must be greater than or equal to 1.")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testAddAddressToCustomerWithoutPostalCodeReturnsBadRequest () throws Exception {
        final String taxCode = this.faker.code().isbn10();
        this.addressDTO.setPostalCode(null);
        AddressDTO savedAddress = this.addressDTO;
        savedAddress.setId(this.faker.number().randomDigit());
        when(this.addressService.addAddress(taxCode, this.addressDTO)).thenReturn(savedAddress);

        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/addresses/customer/{taxCode}", taxCode)
                .content(this.mapper.writeValueAsString(this.addressDTO))
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Address postal code must be not empty.")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testAddAddressToCustomerWithInvalidPostalCodeNumberReturnsBadRequest () throws Exception {
        final String taxCode = this.faker.code().isbn10();
        this.addressDTO.setPostalCode(this.faker.lorem().sentence(200));
        AddressDTO savedAddress = this.addressDTO;
        savedAddress.setId(this.faker.number().randomDigit());
        when(this.addressService.addAddress(taxCode, this.addressDTO)).thenReturn(savedAddress);

        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/addresses/customer/{taxCode}", taxCode)
                .content(this.mapper.writeValueAsString(this.addressDTO))
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Address postal code must be at most 50 character long.")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testAddAddressToCustomerReturnsOkay () throws Exception {
        final String taxCode = this.faker.code().isbn10();
        AddressDTO savedAddress = this.addressDTO;
        savedAddress.setId(this.faker.number().randomDigit());
        when(this.addressService.addAddress(taxCode, this.addressDTO)).thenReturn(savedAddress);

        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/addresses/customer/{taxCode}", taxCode)
                .content(this.mapper.writeValueAsString(this.addressDTO))
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.country", CoreMatchers.is(this.addressDTO.getCountry())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.state", CoreMatchers.is(this.addressDTO.getState())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.city", CoreMatchers.is(this.addressDTO.getCity())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.street", CoreMatchers.is(this.addressDTO.getStreet())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.streetNumber", CoreMatchers.is(this.addressDTO.getStreetNumber())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.postalCode", CoreMatchers.is(this.addressDTO.getPostalCode())))
                .andDo(MockMvcResultHandlers.print());
    }
}
