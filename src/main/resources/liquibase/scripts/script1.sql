-- liquibase formatted sql

-- changeset roman:1

CREATE TABLE notification_task(
    id BIGINT,
    chat_id BIGINT,
    text_msg TEXT,
    notification_time TIMESTAMP
);