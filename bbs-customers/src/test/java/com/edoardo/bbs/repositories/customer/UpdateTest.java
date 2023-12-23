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
public class UpdateTest {

    private final Faker faker;

    @Autowired
    private CustomerRepository customerRepository;

    public UpdateTest() {
        this.faker = new Faker();
    }

    @Test
    public void testUpdateCustomerWithoutAddress () {
        final Customer newCustomer = new Customer(
                this.faker.code().isbn10(), this.faker.name().firstName(), this.faker.name().lastName(),
                this.faker.date().birthday(), this.faker.internet().emailAddress(), this.faker.date().birthday(),
                this.faker.internet().password(), this.faker.file().toString()
        );
        this.customerRepository.save(newCustomer);
        newCustomer.setFirstName(this.faker.name().firstName());
        newCustomer.setLastName(this.faker.name().lastName());
        this.customerRepository.save(newCustomer);

        final Optional<Customer> customer = this.customerRepository.findById(newCustomer.getTaxCode());

        assertThat(customer.isPresent()).isEqualTo(true);
        assertThat(customer.get().getTaxCode()).isEqualTo(newCustomer.getTaxCode());
        assertThat(customer.get().getFirstName()).isEqualTo(newCustomer.getFirstName());
        assertThat(customer.get().getLastName()).isEqualTo(newCustomer.getLastName());
        assertThat(customer.get().getBirthDate()).isEqualTo(newCustomer.getBirthDate());
        assertThat(customer.get().getEmail()).isEqualTo(newCustomer.getEmail());
        assertThat(customer.get().getEmailVerifiedAt()).isEqualTo(newCustomer.getEmailVerifiedAt());
        assertThat(customer.get().getPassword()).isEqualTo(newCustomer.getPassword());
        assertThat(customer.get().getIdCard()).isEqualTo(newCustomer.getIdCard());
    }

    @Test
    public void testSaveCustomerWithAddress () {
        final Address address = new Address(this.faker.address().country(), this.faker.address().state(), this.faker.address().city(),
                this.faker.address().streetName(), Integer.parseInt(this.faker.address().streetAddressNumber()), this.faker.address().zipCode());
        final Customer newCustomer = new Customer(
                this.faker.code().isbn10(), this.faker.name().firstName(), this.faker.name().lastName(),
                this.faker.date().birthday(), this.faker.internet().emailAddress(), this.faker.date().birthday(),
                this.faker.internet().password(), this.faker.file().toString()
        );
        address.setCustomer(newCustomer);
        newCustomer.getAddresses().add(address);
        this.customerRepository.save(newCustomer);
        address.setCity(this.faker.address().city());
        address.setCountry(this.faker.address().country());
        this.customerRepository.save(newCustomer);

        final Optional<Customer> customer = this.customerRepository.findById(newCustomer.getTaxCode());

        assertThat(customer.isPresent()).isEqualTo(true);
        assertThat(customer.get().getTaxCode()).isEqualTo(newCustomer.getTaxCode());
        assertThat(customer.get().getFirstName()).isEqualTo(newCustomer.getFirstName());
        assertThat(customer.get().getLastName()).isEqualTo(newCustomer.getLastName());
        assertThat(customer.get().getBirthDate()).isEqualTo(newCustomer.getBirthDate());
        assertThat(customer.get().getEmail()).isEqualTo(newCustomer.getEmail());
        assertThat(customer.get().getEmailVerifiedAt()).isEqualTo(newCustomer.getEmailVerifiedAt());
        assertThat(customer.get().getPassword()).isEqualTo(newCustomer.getPassword());
        assertThat(customer.get().getIdCard()).isEqualTo(newCustomer.getIdCard());
        assertThat(customer.get().getAddresses().size()).isEqualTo(1);
    }
}
