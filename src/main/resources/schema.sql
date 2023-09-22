CREATE TABLE IF NOT EXISTS customers
(
    id
    BIGSERIAL
    PRIMARY
    KEY,
    name
    VARCHAR
(
    255
) NOT NULL,
    email VARCHAR
(
    255
) NOT NULL UNIQUE
    );

CREATE TABLE IF NOT EXISTS lenders
(
    id
    BIGSERIAL
    PRIMARY
    KEY,
    name
    VARCHAR
(
    255
) NOT NULL,
    email VARCHAR
(
    255
) NOT NULL UNIQUE
    );

CREATE TABLE IF NOT EXISTS loans
(
    id
    bigserial
    PRIMARY
    KEY,
    customer_id
    bigint
    NOT
    NULL,
    lender_id
    bigint
    NOT
    NULL,
    amount
    numeric
(
    19,
    2
) NOT NULL,
    remaining_amount numeric
(
    19,
    2
) NOT NULL,
    payment_date date NOT NULL,
    interest_rate_per_day real NOT NULL,
    due_date date NOT NULL,
    penalty_per_day real NOT NULL,
    cancel_status boolean NOT NULL DEFAULT false,
    FOREIGN KEY
(
    customer_id
) REFERENCES customers
(
    id
),
    FOREIGN KEY
(
    lender_id
) REFERENCES lenders
(
    id
)
    );


ALTER TABLE loans
ALTER
COLUMN interest_rate_per_day TYPE numeric(3, 2),
ALTER
COLUMN penalty_per_day TYPE numeric(3, 2);
