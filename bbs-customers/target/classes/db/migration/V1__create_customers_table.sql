CREATE TABLE Customers (
    tax_code VARCHAR(30) PRIMARY KEY,
    last_name VARCHAR(50) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    birth_date DATE NOT NULL,
    email VARCHAR(50) NOT NULL UNIQUE,
    email_verified_at DATE,
    password VARCHAR(50) NOT NULL,
    id_card TEXT NOT NULL
);