-- liquibase formatted sql

-- changeset denis:1

CREATE TABLE users(
    id BIGSERIAL,
    chat_Id BIGINT,
    name TEXT,
    phone TEXT,
    login TEXT
);


-- changeset denis:2

CREATE TABLE pets(
    id BIGSERIAL PRIMARY KEY,
    name TEXT,
    BREED TEXT,
    AGE INTEGER,
    FOOD TEXT,
    SHELTER TEXT
);

-- changeset denis:3
create table condition_pets(
    id bigserial primary key,
    diet text,
    wellbeing text,
    behaviour text
);
-- changeset denis:4
create table photo_pets(
    id bigserial primary key,
    filePath text,
    fileSize bigserial,
    mediaType text,
    image bytea
);
