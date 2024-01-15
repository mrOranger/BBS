package com.edoardo.bbs;


import com.edoardo.bbs.factories.AddressFactory;
import com.edoardo.bbs.factories.CustomerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
public class FactoryConfig {

    @Bean @Scope("singleton")
    public AddressFactory addressFactory () {
        return new AddressFactory();
    }

    @Bean @Scope("singleton")
    public CustomerFactory customerFactory () {
        return new CustomerFactory();
    }
}
