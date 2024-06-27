-- liquibase formatted sql

-- changeset denis:1

CREATE TABLE notification_task(
    id BIGINT,
    chat_id BIGINT,
    text_msg TEXT,
    notification_time TIMESTAMP
);
