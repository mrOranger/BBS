package com.edoardo.bbs;


import com.edoardo.bbs.factories.AddressFactory;
import com.edoardo.bbs.factories.CustomerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class FactoryConfig {

    @Bean
    public AddressFactory addressFactory () {
        return new AddressFactory();
    }

    @Bean
    public CustomerFactory customerFactory () {
        return new CustomerFactory();
    }
}
