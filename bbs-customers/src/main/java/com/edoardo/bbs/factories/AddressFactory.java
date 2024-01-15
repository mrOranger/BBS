package com.edoardo.bbs.factories;

import com.edoardo.bbs.entities.Address;
import com.github.javafaker.Faker;

public class AddressFactory implements Factory<Address> {

    private Faker faker;

    public AddressFactory () {
        this.faker = new Faker();
    }

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
}
