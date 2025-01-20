INSERT INTO owner (account_id, owner_type)
VALUES (1, 'USER'),
       (2, 'USER'),
       (3, 'USER'),
       (4, 'USER'),
       (5, 'USER'),
       (6, 'USER'),
       (7, 'USER'),
       (8, 'USER'),
       (9, 'USER'),
       (10, 'USER');

INSERT INTO account (number, owner_id, account_type, currency, account_status, closed_at, version)
VALUES ('123456789012', 1, 'INDIVIDUAL', 'USD', 'ACTIVE', null, 1),
       ('123456789013', 2, 'SAVINGS', 'USD', 'ACTIVE', null, 1),
       ('123456789014', 3, 'CORPORATE', 'RUB', 'CLOSED', CURRENT_TIMESTAMP, 2),
       ('123456789015', 4, 'INDIVIDUAL', 'JPY', 'ACTIVE', null, 1),
       ('123456789016', 5, 'SAVINGS', 'USD', 'FROZEN', null, 2),
       ('123456789017', 6, 'CORPORATE', 'EUR', 'ACTIVE', null, 1),
       ('123456789018', 7, 'INDIVIDUAL', 'USD', 'FROZEN', null, 2),
       ('123456789019', 8, 'INDIVIDUAL', 'RUB', 'ACTIVE', null, 1),
       ('123456789020', 9, 'SAVINGS', 'USD', 'ACTIVE', null, 1),
       ('123456789021', 10, 'INVESTMENT', 'USD', 'ACTIVE', null, 1);

INSERT INTO balance (account_id, authorization_Balance, actual_Balance, version)
VALUES (1, 1000.00, 1200.00, 1),
       (2, 1500.00, 1500.00, 2),
       (3, 2000.00, 2200.00, 1),
       (4, 2500.00, 2500.00, 1),
       (5, 3000.00, 3200.00, 3),
       (6, 3500.00, 3500.00, 7),
       (7, 4000.00, 4200.00, 4),
       (8, 4500.00, 4500.00, 6),
       (9, 5000.00, 5200.00, 6),
       (10, 5500.00, 5500.00, 20);