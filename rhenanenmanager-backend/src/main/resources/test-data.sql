-- Test Data for RhenanenManager
-- This script creates a dummy user for testing the login functionality

-- Insert Admin Role
INSERT INTO role (id, name, description) VALUES 
(1, 'ROLE_ADMIN', 'Administrator with full access');

-- Insert Test User
-- Username: admin
-- Password: password (BCrypt hash)
-- Email: admin@rhenanenmanager.de
INSERT INTO user (
    id,
    username,
    password,
    email,
    firstname,
    lastname,
    activated,
    account_locked,
    failed_logins,
    role_id,
    created,
    updated
) VALUES (
    1,
    'admin',
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
    'admin@rhenanenmanager.de',
    'Max',
    'Mustermann',
    TRUE,
    FALSE,
    0,
    1,
    NOW(),
    NOW()
);

-- Insert Member Role
INSERT INTO role (id, name, description) VALUES 
(2, 'ROLE_MEMBER', 'Regular member with basic access');

-- Insert Test Member User
-- Username: member
-- Password: password (BCrypt hash)
-- Email: member@rhenanenmanager.de
INSERT INTO user (
    id,
    username,
    password,
    email,
    firstname,
    lastname,
    activated,
    account_locked,
    failed_logins,
    role_id,
    created,
    updated
) VALUES (
    2,
    'member',
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
    'member@rhenanenmanager.de',
    'Hans',
    'Schmidt',
    TRUE,
    FALSE,
    0,
    2,
    NOW(),
    NOW()
);
