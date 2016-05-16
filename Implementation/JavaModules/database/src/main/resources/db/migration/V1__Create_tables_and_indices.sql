--
-- Remember to run CREATE EXTENSION postgis; in the database and creating a baseline
-- before running the first migration.
--

CREATE TABLE municipalities (
    id bigserial not null,
    name_sv varchar(200) not null,
    name_fi varchar(200) not null,
    PRIMARY KEY (id)
);

CREATE TABLE stations (
    id bigserial not null,
    name_sv varchar(200) not null,
    name_fi varchar(200) not null,
    municipality_id bigint not null,
    location geometry(point,4326) default null,
    street_address varchar(200) default '',
    PRIMARY KEY (id),
    FOREIGN KEY (municipality_id) REFERENCES municipalities (id)
);

CREATE INDEX station_location_ix ON stations USING GIST (location);

CREATE TABLE ticket_types (
    id bigserial not null,
    code varchar(10) not null,
    name_sv varchar(200) not null,
    name_fi varchar(200) not null,
    PRIMARY KEY (id),
    UNIQUE (code)
);

CREATE TABLE resource_types (
    id bigserial not null,
    code varchar(10) not null,
    name_sv varchar(200) not null,
    name_fi varchar(200) not null,
    PRIMARY KEY (id),
    UNIQUE (code)
);

CREATE TYPE urgency AS ENUM ('A','B','C','D');

CREATE TABLE tickets (
    id bigserial not null,
    type_id bigint default null,
    parent_id bigint default null,
    municipality_id bigint default null,
    urgency urgency default null,
    location geometry(point,4326) default null,
    loc_text varchar(200) default '',
    details varchar(200) default '',
    PRIMARY KEY (id),
    FOREIGN KEY (type_id) REFERENCES ticket_types (id),
    FOREIGN KEY (parent_id) REFERENCES tickets (id),
    FOREIGN KEY (municipality_id) REFERENCES municipalities (id)
);

CREATE INDEX ticket_location_ix ON tickets USING GIST (location);

CREATE TYPE ticket_state AS ENUM (
    'CREATED',
    'DISPATCHED',
    'RESOURCES_EN_ROUTE',
    'RESOURCES_ON_SCENE',
    'ON_HOLD',
    'CLOSED'
);

CREATE TABLE ticket_states (
    id bigserial not null,
    ts timestamp with time zone not null,
    state ticket_state not null,
    ticket_id bigint default null,
    PRIMARY KEY (id),
    FOREIGN KEY (ticket_id) REFERENCES tickets (id)
);

CREATE INDEX ticket_state_ts_ix ON ticket_states (ts DESC);

CREATE TABLE resources (
    id bigserial not null,
    type_id bigint not null,
    call_sign varchar(20) not null,
    station_id bigint not null,
    PRIMARY KEY (id),
    FOREIGN KEY (type_id) REFERENCES resource_types (id),
    FOREIGN KEY (station_id) REFERENCES stations (id),
    UNIQUE (call_sign)
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
    'AVAILABLE_OVER_RADIO',
    'RESERVED_AT_STATION',
    'RESERVED_OVER_RADIO',
    'DISPATCHED_AT_STATION',
    'DISPATCHED_OVER_RADIO',
    'EN_ROUTE',
    'ON_SCENE'
);

CREATE TABLE resource_states (
    id bigserial not null,
    ts timestamp with time zone not null,
    state resource_state not null,
    resource_id bigint not null,
    ticket_id bigint default null,
    PRIMARY KEY (id),
    FOREIGN KEY (resource_id) REFERENCES resources (id),
    FOREIGN KEY (ticket_id) REFERENCES tickets (id)
);

CREATE INDEX resource_state_ts_ix ON resource_states (ts DESC);
