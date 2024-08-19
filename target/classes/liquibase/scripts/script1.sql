-- liquibase formatted sql

-- changeset roman:1

CREATE TABLE users(
    id BIGSERIAL PRIMARY KEY,
    chat_Id BIGINT,
    name TEXT,
    phone TEXT,
    login TEXT
);


-- changeset roman:2

CREATE TABLE pets(
    id BIGSERIAL PRIMARY KEY,
    name TEXT,
    BREED TEXT,
    AGE INTEGER,
    FOOD TEXT,
    SHELTER TEXT
);

    -- changeset roman:3

CREATE TABLE pet_report(
    id BIGSERIAL PRIMARY KEY,
    animals_Diet TEXT,
    animal_Health TEXT,
    animal_Habits TEXT,
    data TIMESTAMP without time zone
);

