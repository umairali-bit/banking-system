INSERT INTO roles (role_name)
VALUES ('CUSTOMER'),
       ('EMPLOYEE'),
       ('MANAGER'),
       ('ADMIN');


-- Development admin
-- Password: Admin123!

INSERT INTO users (
    username,
    email,
    password,
    is_enabled,
    account_locked,
    created_at
)
VALUES (
           'admin',
           'admin@bank.com',
           '$2a$10$hLsMaE09UnBXw5eT3WtFlu/BZWg9ymU75EnbBuy5Lj635jDd2W0t2',
           true,
           false,
           CURRENT_TIMESTAMP
       );


INSERT INTO user_roles (
    user_id,
    role_id
)
SELECT
    u.id,
    r.id
FROM users u
         JOIN roles r
              ON r.role_name = 'ADMIN'
WHERE u.username = 'admin';