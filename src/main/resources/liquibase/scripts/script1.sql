-- liquibase formatted sql

-- changeset annaa:1

CREATE TABLE notification_task(
    id BIGINT,
    chat_id BIGINT,
    userName TEXT,
    firstName TEXT,
    lastName TEXT,
    userID BIGINT,
    phone TEXT,
    pet BOOLEAN
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