--
-- Remember to run CREATE EXTENSION postgis;CREATE EXTENSION postgis_topology; in the database and creating a baseline
-- before running the first migration.
--

CREATE TABLE nls_municipality (
    id bigint not null,
    name_fi varchar(200) not null,
    name_sv varchar(200) not null,
    PRIMARY KEY (id)
);

CREATE INDEX nls_municipality_name_fi ON nls_municipality (name_fi);
CREATE INDEX nls_municipality_name_sv ON nls_municipality (name_sv);

CREATE TYPE nls_road_readiness AS ENUM (
    'in_use', -- 0
    'under_construction', -- 1
    'in_planning' -- 3
);

CREATE TABLE nls_road_class (
    id bigint not null,
    name_fi varchar(200) not null,
    name_sv varchar(200) not null,
    PRIMARY KEY (id)
);

CREATE TYPE nls_road_vertical_level AS ENUM (
    'tunnel', -- -11
    'below_surface', -- -1
    'on_surface', -- 0
    'above_surface_level_1', -- 1
    'above_surface_level_2', -- 2
    'above_surface_level_3', -- 3
    'above_surface_level_4', -- 4
    'above_surface_level_5', -- 5
    'undefined' -- 10
);

CREATE TYPE nls_road_surface AS ENUM (
    'unknown', -- 0
    'none', -- 1
    'durable' -- 2
);

CREATE TYPE nls_road_direction AS ENUM (
    'two_way', -- 0
    'one_way', -- 1
    'one_way_reversed' --2
);

CREATE TABLE nls_road (
    id bigserial not null,
    gid bigint not null,
    location_accuracy int not null,
    altitude_accuracy int not null,
    start_date date,
    end_date date,
    road_class_id bigint not null,
    vertical_location nls_road_vertical_level not null,
    readiness nls_road_readiness not null,
    surface nls_road_surface not null,
    direction nls_road_direction not null,
    number int,
    part_number int,
    min_address_number_left int,
    max_address_number_left int,
    min_address_number_right int,
    max_address_number_right int,
    name_sv varchar (200) not null default '',
    name_fi varchar (200) not null default '',
    location geometry(linestring,4326) not null,
    municipality_id bigint not null,
    PRIMARY KEY (id),
    FOREIGN KEY (road_class_id) REFERENCES nls_road_class (id),
    FOREIGN KEY (municipality_id) REFERENCES nls_municipality (id)
);

CREATE INDEX nls_road_gid ON nls_road (gid);
CREATE INDEX nls_road_name_sv ON nls_road (name_sv);
CREATE INDEX nls_road_name_fi ON nls_road (name_fi);
CREATE INDEX nls_road_location ON nls_road USING gist(location);

CREATE TYPE nls_address_point_class AS ENUM (
    'address', -- 96001
    'entry_point' -- 96002
);

CREATE TABLE nls_address_point (
    id bigserial not null,
    gid bigint not null,
    location_accuracy int not null,
    location geometry(point,4326) not null,
    start_date date,
    end_date date,
    number varchar (20) not null default '',
    name_sv varchar (200) not null default '',
    name_fi varchar (200) not null default '',
    municipality_id bigint not null,
    point_class nls_address_point_class not null,
    PRIMARY KEY (id),
    FOREIGN KEY (municipality_id) REFERENCES nls_municipality (id)
);

CREATE INDEX nls_address_point_gid ON nls_address_point (gid);
CREATE INDEX nls_address_point_name_sv ON nls_address_point (name_sv);
CREATE INDEX nls_address_point_name_fi ON nls_address_point (name_fi);
CREATE INDEX nls_address_point_location ON nls_address_point USING gist(location);

CREATE TABLE nls_map_1_5000 (
  id bigserial not null,
  rast raster,
  filename varchar (200) not null,
  tile_x int not null,
  tile_y int not null,
  PRIMARY KEY (id),
  CONSTRAINT enforce_height_rast CHECK (st_height(rast) = 100),
  CONSTRAINT enforce_nodata_values_rast CHECK (_raster_constraint_nodata_values(rast) = '{NULL,NULL,NULL}'::numeric[]),
  CONSTRAINT enforce_num_bands_rast CHECK (st_numbands(rast) = 3),
  CONSTRAINT enforce_out_db_rast CHECK (_raster_constraint_out_db(rast) = '{f,f,f}'::boolean[]),
  CONSTRAINT enforce_pixel_types_rast CHECK (_raster_constraint_pixel_types(rast) = '{16BUI,16BUI,16BUI}'::text[]),
  CONSTRAINT enforce_srid_rast CHECK (st_srid(rast) = 4326),
  CONSTRAINT enforce_width_rast CHECK (st_width(rast) = 100)
);

CREATE INDEX nls_map_1_5000_st_convexhull ON nls_map_1_5000 USING gist(st_convexhull(rast));
CREATE INDEX nls_map_1_5000_filename_tile_x_y ON nls_map_1_5000 (filename, tile_x, tile_y);

CREATE TABLE nls_map_1_10000 (
  id bigserial not null,
  rast raster,
  filename varchar (200) not null,
  tile_x int not null,
  tile_y int not null,
  PRIMARY KEY (id),
  CONSTRAINT enforce_height_rast CHECK (st_height(rast) = 100),
  CONSTRAINT enforce_nodata_values_rast CHECK (_raster_constraint_nodata_values(rast) = '{NULL,NULL,NULL}'::numeric[]),
  CONSTRAINT enforce_num_bands_rast CHECK (st_numbands(rast) = 3),
  CONSTRAINT enforce_out_db_rast CHECK (_raster_constraint_out_db(rast) = '{f,f,f}'::boolean[]),
  CONSTRAINT enforce_pixel_types_rast CHECK (_raster_constraint_pixel_types(rast) = '{16BUI,16BUI,16BUI}'::text[]),
  CONSTRAINT enforce_srid_rast CHECK (st_srid(rast) = 4326),
  CONSTRAINT enforce_width_rast CHECK (st_width(rast) = 100)
);

CREATE INDEX nls_map_1_10000_st_convexhull ON nls_map_1_10000 USING gist(st_convexhull(rast));
CREATE INDEX nls_map_1_10000_filename_tile_x_y ON nls_map_1_10000 (filename, tile_x, tile_y);

CREATE TABLE nls_map_1_20000 (
  id bigserial not null,
  rast raster,
  filename varchar (200) not null,
  tile_x int not null,
  tile_y int not null,
  PRIMARY KEY (id),
  CONSTRAINT enforce_height_rast CHECK (st_height(rast) = 100),
  CONSTRAINT enforce_nodata_values_rast CHECK (_raster_constraint_nodata_values(rast) = '{NULL,NULL,NULL}'::numeric[]),
  CONSTRAINT enforce_num_bands_rast CHECK (st_numbands(rast) = 3),
  CONSTRAINT enforce_out_db_rast CHECK (_raster_constraint_out_db(rast) = '{f,f,f}'::boolean[]),
  CONSTRAINT enforce_pixel_types_rast CHECK (_raster_constraint_pixel_types(rast) = '{16BUI,16BUI,16BUI}'::text[]),
  CONSTRAINT enforce_srid_rast CHECK (st_srid(rast) = 4326),
  CONSTRAINT enforce_width_rast CHECK (st_width(rast) = 100)
);

CREATE INDEX nls_map_1_20000_st_convexhull ON nls_map_1_20000 USING gist(st_convexhull(rast));
CREATE INDEX nls_map_1_20000_filename_tile_x_y ON nls_map_1_20000 (filename, tile_x, tile_y);

CREATE TABLE nls_map_1_40000 (
  id bigserial not null,
  rast raster,
  filename varchar (200) not null,
  tile_x int not null,
  tile_y int not null,
  PRIMARY KEY (id),
  CONSTRAINT enforce_height_rast CHECK (st_height(rast) = 100),
  CONSTRAINT enforce_nodata_values_rast CHECK (_raster_constraint_nodata_values(rast) = '{NULL,NULL,NULL}'::numeric[]),
  CONSTRAINT enforce_num_bands_rast CHECK (st_numbands(rast) = 3),
  CONSTRAINT enforce_out_db_rast CHECK (_raster_constraint_out_db(rast) = '{f,f,f}'::boolean[]),
  CONSTRAINT enforce_pixel_types_rast CHECK (_raster_constraint_pixel_types(rast) = '{16BUI,16BUI,16BUI}'::text[]),
  CONSTRAINT enforce_srid_rast CHECK (st_srid(rast) = 4326),
  CONSTRAINT enforce_width_rast CHECK (st_width(rast) = 100)
);

CREATE INDEX nls_map_1_40000_st_convexhull ON nls_map_1_40000 USING gist(st_convexhull(rast));
CREATE INDEX nls_map_1_40000_filename_tile_x_y ON nls_map_1_40000 (filename, tile_x, tile_y);

CREATE TABLE nls_map_1_80000 (
  id bigserial not null,
  rast raster,
  filename varchar (200) not null,
  tile_x int not null,
  tile_y int not null,
  PRIMARY KEY (id),
  CONSTRAINT enforce_height_rast CHECK (st_height(rast) = 100),
  CONSTRAINT enforce_nodata_values_rast CHECK (_raster_constraint_nodata_values(rast) = '{NULL,NULL,NULL}'::numeric[]),
  CONSTRAINT enforce_num_bands_rast CHECK (st_numbands(rast) = 3),
  CONSTRAINT enforce_out_db_rast CHECK (_raster_constraint_out_db(rast) = '{f,f,f}'::boolean[]),
  CONSTRAINT enforce_pixel_types_rast CHECK (_raster_constraint_pixel_types(rast) = '{16BUI,16BUI,16BUI}'::text[]),
  CONSTRAINT enforce_srid_rast CHECK (st_srid(rast) = 4326),
  CONSTRAINT enforce_width_rast CHECK (st_width(rast) = 100)
);

CREATE INDEX nls_map_1_80000_st_convexhull ON nls_map_1_80000 USING gist(st_convexhull(rast));
CREATE INDEX nls_map_1_80000_filename_tile_x_y ON nls_map_1_80000 (filename, tile_x, tile_y);

CREATE TABLE nls_map_1_160000 (
  id bigserial not null,
  rast raster,
  filename varchar (200) not null,
  tile_x int not null,
  tile_y int not null,
  PRIMARY KEY (id),
  CONSTRAINT enforce_height_rast CHECK (st_height(rast) = 100),
  CONSTRAINT enforce_nodata_values_rast CHECK (_raster_constraint_nodata_values(rast) = '{NULL,NULL,NULL}'::numeric[]),
  CONSTRAINT enforce_num_bands_rast CHECK (st_numbands(rast) = 3),
  CONSTRAINT enforce_out_db_rast CHECK (_raster_constraint_out_db(rast) = '{f,f,f}'::boolean[]),
  CONSTRAINT enforce_pixel_types_rast CHECK (_raster_constraint_pixel_types(rast) = '{16BUI,16BUI,16BUI}'::text[]),
  CONSTRAINT enforce_srid_rast CHECK (st_srid(rast) = 4326),
  CONSTRAINT enforce_width_rast CHECK (st_width(rast) = 100)
);

CREATE INDEX nls_map_1_160000_st_convexhull ON nls_map_1_160000 USING gist(st_convexhull(rast));
CREATE INDEX nls_map_1_160000_filename_tile_x_y ON nls_map_1_160000 (filename, tile_x, tile_y);

-- TODO Map for 1:320000, cannot have 100X100 tiles

CREATE TYPE nls_place_group AS ENUM (
    'natural', -- 1
    'cultural' -- 2
);

CREATE TYPE nls_place_subgroup AS ENUM (
    'terrain',        -- 11
    'hydrographic',   -- 12
    'administrative', -- 21
    'other'          -- 22
);

CREATE TABLE nls_place_type (
    id bigint not null,
    name_fi varchar(200) not null,
    name_sv varchar(200) not null,
    PRIMARY KEY (id)
);

CREATE TABLE nls_place (
    id bigint not null,
    place_type_id bigint not null,
    place_group nls_place_group not null,
    place_subgroup nls_place_subgroup not null,
    location geometry(point,4326) not null,
    altitude int,
    municipality_id bigint not null,
    created timestamp not null,
    modified timestamp not null,
    PRIMARY KEY (id),
    FOREIGN KEY (place_type_id) REFERENCES nls_place_type (id),
    FOREIGN KEY (municipality_id) REFERENCES nls_municipality (id)
);

CREATE INDEX nls_place_location ON nls_place USING gist(location);

CREATE TYPE nls_place_name_language AS ENUM (
    'fin',
    'swe',
    'sme',
    'smn',
    'sms'
);

CREATE TABLE nls_place_name (
    id bigint not null,
    place_id bigint not null,
    name varchar(200) not null,
    language nls_place_name_language not null,
    created timestamp not null,
    modified timestamp not null,
    PRIMARY KEY (id),
    FOREIGN KEY (place_id) REFERENCES nls_place (id)
);

CREATE INDEX nls_place_name_name ON nls_place_name (name);
