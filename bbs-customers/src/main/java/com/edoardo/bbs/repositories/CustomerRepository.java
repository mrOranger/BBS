package com.edoardo.bbs.repositories;

import com.edoardo.bbs.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface CustomerRepository extends JpaRepository<Customer, String> {

    List<Customer> findByFirstNameAndLastName (String firstName, String lastName);
    List<Customer> findByBirthDate (Date birthDate);

}
