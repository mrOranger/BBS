package com.edoardo.bbs.factories;

import com.edoardo.bbs.dtos.AddressDTO;
import com.edoardo.bbs.entities.Address;
import com.edoardo.bbs.mapper.AddressMapper;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;

public class AddressFactory implements Factory<Address, AddressDTO> {

    @Autowired
    private Faker faker;

    @Autowired
    private AddressMapper addressMapper;

    @Override
    public Address create() {
        return Address.builder()
                .state(this.faker.address().state())
                .city(this.faker.address().city())
                .country(this.faker.address().country())
                .street(this.faker.address().streetName())
                .streetNumber(this.faker.number().numberBetween(1, 100))
                .postalCode(this.faker.address().zipCode())
                .build();
    }

    @Override
    public AddressDTO createDTO() {
        return this.addressMapper.convertToDTO(this.create());
    }
}
