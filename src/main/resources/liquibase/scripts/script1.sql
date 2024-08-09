-- liquibase formatted sql

-- changeset annaa:1

CREATE TABLE public.users(
    id BIGINT,
    chat_id BIGINT,
    text_msg TEXT,
    phone text,
    login text
);


-- changeset annaa:2
CREATE TABLE pets(
    id BIGINT PRIMARY KEY,
    name TEXT,
    BREED TEXT,
    AGE INTEGER,
    FOOD TEXT,
    SHELTER TEXT
);