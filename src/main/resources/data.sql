INSERT INTO customers (email, name)
VALUES ('customer1@example.com', 'customer1'),
       ('customer2@example.com', 'customer2'),
       ('customer3@example.com', 'customer3'),
       ('customer4@example.com', 'customer4'),
       ('customer5@example.com', 'customer5')
        ON CONFLICT (email) DO NOTHING;

INSERT INTO lenders (email, name)
VALUES ('lender1@example.com', 'lender1'),
       ('lender2@example.com', 'lender2'),
       ('lender3@example.com', 'lender3'),
       ('lender4@example.com', 'lender4'),
       ('lender5@example.com', 'lender5')
        ON CONFLICT (email) DO NOTHING;
