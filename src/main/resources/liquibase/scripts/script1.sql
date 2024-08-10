-- liquibase formatted sql

-- changeset denis:1

CREATE TABLE users(
    id INTEGER PRIMARY KEY ,
    chat_Id BIGINT,
    name TEXT,
    phone TEXT,
    login TEXT
);


-- changeset denis:2
CREATE TABLE pets(
    id INTEGER PRIMARY KEY ,
    name TEXT,
    BREED TEXT,
    AGE INTEGER,
    FOOD TEXT,
    SHELTER TEXT
);
