-- We use a separate database schema for DART Accounts
CREATE SCHEMA IF NOT EXISTS dart_accounts;

-- Account types are used to determine the access to RabbitMQ
CREATE TABLE IF NOT EXISTS dart_accounts.account_types (
  id   BIGSERIAL    NOT NULL,
  name VARCHAR(255) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE (name)
);

-- Account type permissions are used to determine access to RabbitMQ resources (exchanges, topics, queues)
CREATE TABLE IF NOT EXISTS dart_accounts.account_type_permissions (
  type_id       BIGINT       NOT NULL,
  resource_type VARCHAR(255) NOT NULL,
  resource_name VARCHAR(255) NOT NULL,
  configure     BOOLEAN DEFAULT FALSE,
  write         BOOLEAN DEFAULT FALSE,
  read          BOOLEAN DEFAULT FALSE,
  PRIMARY KEY (type_id, resource_type, resource_name),
  FOREIGN KEY (type_id) REFERENCES dart_accounts.account_types (id)
);

-- Accounts represent users (either human users or system users)
CREATE TABLE IF NOT EXISTS dart_accounts.accounts (
  id       BIGSERIAL    NOT NULL,
  name     VARCHAR(255) NOT NULL,
  password VARCHAR(255)          DEFAULT NULL,
  type_id  BIGINT       NOT NULL,
  enabled  BOOLEAN      NOT NULL DEFAULT FALSE,
  PRIMARY KEY (id),
  UNIQUE (name),
  FOREIGN KEY (type_id) REFERENCES dart_accounts.account_types (id)
);
