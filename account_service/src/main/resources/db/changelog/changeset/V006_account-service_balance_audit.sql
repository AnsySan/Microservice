ALTER TABLE balance
    RENAME COLUMN create_at TO created_at;

ALTER TABLE balance
    RENAME COLUMN update_at TO updated_at;

CREATE TABLE IF NOT EXISTS balance_audit
(
    id                      BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY NOT NULL,
    account_id              BIGINT      NOT NULL,
    payment_number          BIGINT      NOT NULL,
    authorization_balance   NUMERIC     NOT NULL,
    actual_balance          NUMERIC     NOT NULL,
    version                 BIGINT      NOT NULL,
    created_at              timestamptz DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_account_balance FOREIGN KEY (account_id) REFERENCES account (id)
);

CREATE INDEX idx_balance_audit_account_id ON balance_audit (account_id);