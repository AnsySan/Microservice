CREATE TABLE IF NOT EXISTS payment
(
    id                      bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    idempotency_key         UUID           NOT NULL,
    sender_account_number   VARCHAR(255)   NOT NULL,
    receiver_account_number VARCHAR(255)   NOT NULL,
    currency                VARCHAR(10)    NOT NULL,
    amount                  NUMERIC(15, 2) NOT NULL,
    payment_status          VARCHAR(255)   NOT NULL,
    scheduled_at            timestamptz    NOT NULL,
    created_at              timestamptz    NOT NULL
);