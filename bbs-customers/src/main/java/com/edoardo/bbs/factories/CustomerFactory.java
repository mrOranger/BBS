package com.edoardo.bbs.factories;

import com.edoardo.bbs.entities.Customer;
import com.github.javafaker.Faker;

import java.time.ZoneId;

public class CustomerFactory implements Factory<Customer> {

    private Faker faker;

    public CustomerFactory () {
        this.faker = new Faker();
    }

    @Override
    public Customer create() {
        return Customer.builder().taxCode(faker.code().isbn10())
                .firstName(faker.name().firstName())
                .lastName(faker.name().lastName())
                .email(faker.internet().emailAddress())
                .birthDate(faker.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                .emailVerifiedAt(faker.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                .password(faker.internet().password())
                .idCard(faker.file().toString())
                .build();
    }
}
