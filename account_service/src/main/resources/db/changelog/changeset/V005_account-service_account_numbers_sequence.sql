CREATE TABLE IF NOT EXISTS account_numbers_sequence
(
    id             BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE NOT NULL,
    count          BIGINT DEFAULT 0                                      NOT NULL,
    account_type   VARCHAR(32)                                            NOT NULL,
    version        BIGINT DEFAULT 1                                       NOT NULL
);

CREATE UNIQUE INDEX idx_account_numbers_sequence_account_type ON account_numbers_sequence (account_type);