-- We use a separate database schema for DART Accounts
CREATE SCHEMA IF NOT EXISTS dart_accounts;

-- Account types are used to determine the access to RabbitMQ
CREATE TABLE IF NOT EXISTS dart_accounts.account_types (
  id   BIGSERIAL    NOT NULL,
  name VARCHAR(255) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS dart_accounts.account_type_permissions (
  type_id       BIGINT       NOT NULL,
  resource_type VARCHAR(255) NOT NULL,
  configure     VARCHAR(255) NOT NULL DEFAULT '',
  write         VARCHAR(255) NOT NULL DEFAULT '',
  read          VARCHAR(255) NOT NULL DEFAULT '',
  PRIMARY KEY (type_id, resource_type),
  UNIQUE (type_id, resource_type),
  FOREIGN KEY (type_id) REFERENCES dart_accounts.account_types (id)
);

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
