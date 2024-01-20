package com.edoardo.bbs;

import com.edoardo.bbs.entities.Address;
import com.edoardo.bbs.entities.Customer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

public class RedisConfig {

    @Bean
    public RedisTemplate<String, Customer> redisCustomerTemplate (RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Customer> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        return template;
    }

    @Bean
    public RedisTemplate<Integer, Address> redisAddressTemplate (RedisConnectionFactory connectionFactory) {
        RedisTemplate<Integer, Address> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        return template;
    }
}
