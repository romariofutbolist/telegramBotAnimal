-- liquibase formatted sql

-- changeset denis:1

CREATE TABLE notification_task(
    id BIGINT,
    chat_id BIGINT,
    text_msg TEXT,
    phone TEXT
);


-- changeset denis:2
CREATE TABLE pets(
    id BIGINT PRIMARY KEY,
    name TEXT,
    BREED TEXT,
    AGE INTEGER,
    FOOD TEXT,
    SHELTER TEXT
);