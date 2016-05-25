--
-- Remember to run CREATE EXTENSION postgis; in the database and creating a baseline
-- before running the first migration.
--

CREATE TABLE capabilities (
    id bigserial not null,
    capability varchar(200) not null,
    description_sv varchar(200) not null default '',
    description_fi varchar(200) not null default '',
    description_en varchar(200) not null default '',
    PRIMARY KEY (id),
    UNIQUE (capability)
);

CREATE TABLE resources (
    id bigserial not null,
    name varchar(20) not null,
    disabled boolean not null default false,
    PRIMARY KEY (id),
    UNIQUE (name)
);

CREATE TABLE resource_capabilities (
    resource_id bigint not null,
    capability_id bigint not null,
    PRIMARY KEY (resource_id, capability_id),
    FOREIGN KEY (resource_id) REFERENCES resources (id),
    FOREIGN KEY (capability_id) REFERENCES capabilities (id)
);

CREATE TABLE resource_locations (
    id bigserial not null,
    ts timestamp with time zone not null,
    location geometry(point,4326) default null,
    resource_id bigint not null,
    PRIMARY KEY (id),
    FOREIGN KEY (resource_id) REFERENCES resources (id)
);

CREATE INDEX resource_location_ts_ix ON resource_locations (ts DESC);
CREATE INDEX resource_location_location_ix ON resource_locations USING GIST (location);

CREATE TYPE resource_state AS ENUM (
    'UNAVAILABLE',
    'AVAILABLE_AT_STATION',
    'AVAILABLE',
    'RESERVED_AT_STATION',
    'RESERVED',
    'DISPATCHED_AT_STATION',
    'DISPATCHED',
    'EN_ROUTE',
    'ON_SCENE'
);

CREATE TABLE resource_state_descriptors (
    state resource_state not null,
    description_sv varchar(200) not null default '',
    description_fi varchar(200) not null default '',
    description_en varchar(200) not null default '',
    location_tracking_enabled boolean not null default true,
    color varchar(6) not null default 'ffffff',
    PRIMARY KEY (state)
);

CREATE TABLE resource_states (
    id bigserial not null,
    ts timestamp with time zone not null,
    state resource_state not null,
    resource_id bigint not null,
    PRIMARY KEY (id),
    FOREIGN KEY (resource_id) REFERENCES resources (id)
);

CREATE INDEX resource_state_ts_ix ON resource_states (ts DESC);
