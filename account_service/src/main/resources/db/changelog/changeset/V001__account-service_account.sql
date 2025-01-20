CREATE TABLE IF NOT EXISTS owner
(
    id         BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
    account_id BIGINT      NOT NULL,
    owner_type VARCHAR(64) NOT NULL
);

CREATE TABLE IF NOT EXISTS account
(
    id             BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
    number         VARCHAR(20) NOT NULL UNIQUE CHECK (LENGTH(number) BETWEEN 12 AND 20),
    owner_id       BIGINT      NOT NULL,
    account_type   VARCHAR(32) NOT NULL,
    currency       VARCHAR(32) NOT NULL,
    account_status VARCHAR(32) NOT NULL,
    created_at     timestamptz DEFAULT CURRENT_TIMESTAMP,
    updated_at     timestamptz DEFAULT CURRENT_TIMESTAMP,
    closed_at      timestamptz,
    version        BIGINT      NOT NULL,
    FOREIGN KEY (owner_id) REFERENCES owner (id)
);

CREATE INDEX IF NOT EXISTS idx_owner_id ON account (owner_id);