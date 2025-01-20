CREATE TABLE free_account_number
(
    account_type VARCHAR(32)      NOT NULL,
    number       VARCHAR(20)      NOT NULL,
    CONSTRAINT pk_free_account_number PRIMARY KEY (account_type, number)
);

CREATE UNIQUE INDEX idx_free_account_number_account_type ON free_account_number (account_type);