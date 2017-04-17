--
-- Remember to run CREATE EXTENSION postgis; in the database before running the first migration.
--

-- We use a separate database schema for DART Resources
CREATE SCHEMA IF NOT EXISTS dart_resources;

CREATE TABLE IF NOT EXISTS dart_resources.resource_types (
  id          BIGSERIAL    NOT NULL,
  name        VARCHAR(255) NOT NULL,
  active      BOOLEAN      NOT NULL DEFAULT TRUE,
  description VARCHAR(255) NOT NULL DEFAULT '',
  PRIMARY KEY (id)
);

CREATE UNIQUE INDEX IF NOT EXISTS resource_type_name
  ON dart_resources.resource_types (name)
  WHERE active IS TRUE;

CREATE TABLE IF NOT EXISTS dart_resources.stations (
  id       BIGSERIAL    NOT NULL,
  name     VARCHAR(255) NOT NULL,
  active   BOOLEAN      NOT NULL DEFAULT TRUE,
  location GEOMETRY(point, 4326) DEFAULT NULL,
  note     VARCHAR(255) NOT NULL DEFAULT '',
  PRIMARY KEY (id)
);

CREATE INDEX IF NOT EXISTS station_location
  ON dart_resources.stations USING GIST (location);

CREATE TABLE IF NOT EXISTS dart_resources.resources (
  id         BIGSERIAL    NOT NULL,
  name       VARCHAR(255) NOT NULL,
  type_id    BIGINT       NOT NULL,
  active     BOOLEAN               DEFAULT TRUE,
  station_id BIGINT       NOT NULL,
  note       VARCHAR(255) NOT NULL DEFAULT '',
  PRIMARY KEY (id),
  FOREIGN KEY (type_id) REFERENCES dart_resources.resource_types (id),
  FOREIGN KEY (station_id) REFERENCES dart_resources.stations (id)
);

CREATE UNIQUE INDEX IF NOT EXISTS resource_name
  ON dart_resources.resources (name)
  WHERE active IS TRUE;

CREATE TABLE IF NOT EXISTS dart_resources.status_descriptors (
  id               BIGSERIAL    NOT NULL,
  name             VARCHAR(255) NOT NULL,
  color            INTEGER      NOT NULL DEFAULT 4095,
  user_selectable  BOOLEAN      NOT NULL DEFAULT TRUE,
  station_location BOOLEAN      NOT NULL DEFAULT TRUE,
  live_location    BOOLEAN      NOT NULL DEFAULT FALSE,
  active           BOOLEAN      NOT NULL DEFAULT TRUE,
  note             VARCHAR(255) NOT NULL DEFAULT '',
  PRIMARY KEY (id),
  CHECK (live_location <> station_location OR live_location IS FALSE)
);

CREATE UNIQUE INDEX IF NOT EXISTS status_descriptor_name
  ON dart_resources.status_descriptors (name)
  WHERE active IS TRUE;

CREATE TABLE IF NOT EXISTS dart_resources.status_descriptor_transitions (
  from_id BIGSERIAL NOT NULL,
  to_id   BIGSERIAL NOT NULL,
  PRIMARY KEY (from_id, to_id),
  FOREIGN KEY (from_id) REFERENCES dart_resources.status_descriptors (id),
  FOREIGN KEY (to_id) REFERENCES dart_resources.status_descriptors (id)
);

CREATE TABLE IF NOT EXISTS dart_resources.resource_location_log (
  id          BIGSERIAL                NOT NULL,
  ts          TIMESTAMP WITH TIME ZONE NOT NULL,
  location    GEOMETRY(point, 4326)    NOT NULL,
  resource_id BIGINT                   NOT NULL,
  user_name   VARCHAR(255)             NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (resource_id) REFERENCES dart_resources.resources (id)
);

CREATE INDEX IF NOT EXISTS resource_location_log_ts
  ON dart_resources.resource_location_log (ts DESC);

CREATE INDEX IF NOT EXISTS resource_location_log_location
  ON dart_resources.resource_location_log USING GIST (location);

CREATE TABLE IF NOT EXISTS dart_resources.resource_status_log (
  id                   BIGSERIAL                NOT NULL,
  ts                   TIMESTAMP WITH TIME ZONE NOT NULL,
  status_descriptor_id BIGINT                   NOT NULL,
  resource_id          BIGINT                   NOT NULL,
  user_name            VARCHAR(255)             NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (resource_id) REFERENCES dart_resources.resources (id)
);

CREATE INDEX IF NOT EXISTS resource_status_log_ts
  ON dart_resources.resource_status_log (ts DESC);