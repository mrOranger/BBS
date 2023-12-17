CREATE TABLE Addresses (
    id SERIAL PRIMARY KEY,
    country VARCHAR(30) NOT NULL,
    state VARCHAR(30) NOT NULL,
    city VARCHAR(30) NOT NULL,
    street VARCHAR(30) NOT NULL,
    number INT NOT NULL,
    postal_code VARCHAR(20) NOT NULL,
    customer VARCHAR(30) NOT NULL,
    CONSTRAINT fk_customer FOREIGN KEY (customer) REFERENCES Customers(tax_code)
);