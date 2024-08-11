-- liquibase formatted sql

-- changeset annaa:1

CREATE TABLE users(
    id BIGSERIAL,
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
