CREATE SCHEMA IF NOT EXISTS person;

CREATE TABLE IF NOT EXISTS person.tb_person (
    person_id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    document_number VARCHAR(100) NOT NULL,
    birthdate DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS person.tb_address (
    person_id INTEGER PRIMARY KEY,
    postal_code INTEGER,
    street VARCHAR(255),
    number INTEGER,
    city VARCHAR(100),
    state VARCHAR(100),
    CONSTRAINT fk_person_address
        FOREIGN KEY (person_id)
        REFERENCES person.person(person_id)
        ON DELETE CASCADE
);