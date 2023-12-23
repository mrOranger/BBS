package com.edoardo.bbs.repositories.customer;

import com.edoardo.bbs.entities.Address;
import com.edoardo.bbs.entities.Customer;
import com.edoardo.bbs.repositories.CustomerRepository;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class DeleteTest {

    private final Faker faker;

    @Autowired
    private CustomerRepository customerRepository;

    public DeleteTest() {
        this.faker = new Faker();
    }

    @Test
    public void testDeleteCustomerWithoutAddress () {
        final Customer newCustomer = new Customer(
                this.faker.code().isbn10(), this.faker.name().firstName(), this.faker.name().lastName(),
                this.faker.date().birthday(), this.faker.internet().emailAddress(), this.faker.date().birthday(),
                this.faker.internet().password(), this.faker.file().toString()
        );
        this.customerRepository.save(newCustomer);
        this.customerRepository.delete(newCustomer);

        final Optional<Customer> customer = this.customerRepository.findById(newCustomer.getTaxCode());

        assertThat(customer.isPresent()).isEqualTo(false);
    }

    @Test
    public void testDeleteCustomerWithAddress () {
        final Address address = new Address(this.faker.address().country(), this.faker.address().state(), this.faker.address().city(),
                this.faker.address().streetName(), Integer.parseInt(this.faker.address().streetAddressNumber()), this.faker.address().zipCode());
        final Customer newCustomer = new Customer(
                this.faker.code().isbn10(), this.faker.name().firstName(), this.faker.name().lastName(),
                this.faker.date().birthday(), this.faker.internet().emailAddress(), this.faker.date().birthday(),
                this.faker.internet().password(), this.faker.file().toString()
        );
        this.customerRepository.save(newCustomer);
        this.customerRepository.delete(newCustomer);

        final Optional<Customer> customer = this.customerRepository.findById(newCustomer.getTaxCode());

        assertThat(customer.isPresent()).isEqualTo(false);
    }
}
