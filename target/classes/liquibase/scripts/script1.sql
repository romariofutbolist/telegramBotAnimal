-- liquibase formatted sql

-- changeset roman:1

CREATE TABLE public.users(
    id BIGINT,
    chat_Id BIGINT,
    name TEXT,
    phone TEXT
);


-- changeset roman:2

CREATE TABLE public.pets(
    id BIGINT PRIMARY KEY,
    name TEXT,
    BREED TEXT,
    AGE INTEGER,
    FOOD TEXT,
    SHELTER TEXT
);
