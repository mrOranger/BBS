package com.edoardo.bbs.repositories;

import com.edoardo.bbs.entities.Customer;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.Set;

public interface CustomerRepository extends CrudRepository<Customer, String> {

    Set<Customer> findByFirstNameAndLastName (String firstName, String lastName);
    Set<Customer> findByBirthDate (Date birthDate);

}
