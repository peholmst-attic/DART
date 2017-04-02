--- We use a separate database schema for DART Accounts
CREATE SCHEMA IF NOT EXISTS accounts;

-- Account types are used to determine the access to RabbitMQ
CREATE TABLE IF NOT EXISTS accounts.account_types (
  id                  BIGSERIAL    NOT NULL,
  name                VARCHAR(255) NOT NULL,
  configure_resources VARCHAR(255) NOT NULL,
  write_resources     VARCHAR(255) NOT NULL,
  read_resources      VARCHAR(255) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS accounts.accounts (
  id       BIGSERIAL    NOT NULL,
  name     VARCHAR(255) NOT NULL,
  password VARCHAR(255)          DEFAULT NULL,
  type_id  BIGINT       NOT NULL,
  enabled  BOOLEAN      NOT NULL DEFAULT FALSE,
  PRIMARY KEY (id),
  UNIQUE (name),
  FOREIGN KEY (type_id) REFERENCES accounts.account_types (id)
);
