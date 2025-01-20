CREATE TABLE IF NOT EXISTS balance
(
    id                    BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    account_id            BIGINT NOT NULL,
    authorization_Balance MONEY,
    actual_Balance        MONEY,
    create_at             timestamptz DEFAULT CURRENT_TIMESTAMP,
    update_at             timestamptz DEFAULT CURRENT_TIMESTAMP,
    version               BIGINT NOT NULL,
    CONSTRAINT fk_account_balance FOREIGN KEY (account_id) REFERENCES account (id)
);
