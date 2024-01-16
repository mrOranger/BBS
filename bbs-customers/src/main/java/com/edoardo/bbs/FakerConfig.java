package com.edoardo.bbs;

import com.github.javafaker.Faker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
public class FakerConfig {

    @Bean @Scope("singleton")
    public Faker fake () {
        return new Faker();
    }
}
