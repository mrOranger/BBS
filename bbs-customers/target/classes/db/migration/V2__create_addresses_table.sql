CREATE TABLE Addresses (
    id SERIAL PRIMARY KEY,
    country VARCHAR(50) NOT NULL,
    state VARCHAR(50) NOT NULL,
    city VARCHAR(50) NOT NULL,
    street VARCHAR(50) NOT NULL,
    number INT NOT NULL,
    postal_code VARCHAR(50) NOT NULL,
    customer VARCHAR(30) NOT NULL,
    CONSTRAINT fk_customer FOREIGN KEY (customer) REFERENCES Customers(tax_code)
);