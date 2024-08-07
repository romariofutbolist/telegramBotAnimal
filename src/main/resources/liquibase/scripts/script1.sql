-- liquibase formated sql

-- changeset denis:1

CREATE TABLE notification_task(
    id BIGINT,
    chat_id BIGINT,
    text_msg TEXT,
    phone text,
    login text
);
--changeset denis:2
create table pets(
    id bigint primary key,
    name text,
    breed text,
    age integer,
    food text,
    shelter text
);
