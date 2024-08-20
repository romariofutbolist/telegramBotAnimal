-- liquibase formatted sql

-- changeset annaa:1

CREATE TABLE users(
    user_id BIGSERIAL PRIMARY KEY,
    chat_Id BIGINT,
    name TEXT,
    phone TEXT,
    login TEXT,
    pet_id INT
);


-- changeset annaa:2

CREATE TABLE pets(
    pet_id BIGSERIAL PRIMARY KEY,
    name TEXT,
    BREED TEXT,
    AGE INTEGER,
    FOOD TEXT,
    SHELTER TEXT,
    user_id INT
);


-- changeset annaa:3

CREATE TABLE user_pet (
  user_id INT,
  pet_id INT,
  PRIMARY KEY (user_id, pet_id),
  FOREIGN KEY (user_id) REFERENCES users(user_id),
  FOREIGN KEY (pet_id) REFERENCES pets(pet_id)
);

-- changeset annaa:4
CREATE TABLE report(
    chat_Id BIGINT,
    text TEXT,
    photo BYTEA
);


---- changeset annaa:3
--
--ALTER TABLE users
--ADD COLUMN pet_id VARCHAR(255);
--
---- changeset annaa:4
--
--ALTER TABLE pets
--ADD COLUMN user_id VARCHAR(255);

---- changeset annaa:4
--DROP TABLE users CASCADE;
--DROP TABLE pets CASCADE;

