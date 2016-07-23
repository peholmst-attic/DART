--
-- Remember to run CREATE EXTENSION postgis; in the database and creating a baseline
-- before running the first migration.
--

CREATE TABLE nls_kunta (
    id bigint not null,
    name_fi varchar(200) not null,
    name_sv varchar(200) not null,
    name_en varchar(200) not null,
    PRIMARY KEY (id)
);

CREATE TABLE nls_suuralue (
    id bigint not null,
    name_fi varchar(200) not null,
    name_sv varchar(200) not null,
    name_en varchar(200) not null,
    PRIMARY KEY (id)
);

CREATE TABLE nls_seutukunta (
    id bigint not null,
    name_fi varchar(200) not null,
    name_sv varchar(200) not null,
    name_en varchar(200) not null,
    PRIMARY KEY (id)
);

CREATE TABLE nls_maakunta (
    id bigint not null,
    name_fi varchar(200) not null,
    name_sv varchar(200) not null,
    name_en varchar(200) not null,
    PRIMARY KEY (id)
);

CREATE TABLE nls_paikkatyyppi (
    id bigint not null,
    name_fi varchar(200) not null,
    name_sv varchar(200) not null,
    name_en varchar(200) not null,
    PRIMARY KEY (id)
);

CREATE TABLE nls_paikkatyyppiryhma (
    id bigint not null,
    name_fi varchar(200) not null,
    name_sv varchar(200) not null,
    name_en varchar(200) not null,
    PRIMARY KEY (id)
);

CREATE TABLE nls_paikkatyyppialaryhma (
    id bigint not null,
    name_fi varchar(200) not null,
    name_sv varchar(200) not null,
    name_en varchar(200) not null,
    PRIMARY KEY (id)
);

CREATE TABLE nls_paikannimi (
    id bigint not null,
    nls_paikkatyyppi_id bigint,
    nls_paikkatyyppiryhma_id bigint,
    nls_paikkatyyppialaryhma_id bigint,
    -- paikka_sijiainti gml:GeometryPropertyType,
    altitude int,
    tm35fin7_code varchar(200),
    ylj7_code varchar(200),
    pp6_code varchar(200),
    nls_kunta_id bigint,
    nls_seutukunta_id bigint,
    nls_maakunta_id bigint,
    nls_suuralue_id bigint,
    -- mittakaavarelevanssi
    created timestamp,
    updated timestamp,
    -- nimi gml:FeaturePropertyType
    PRIMARY KEY (id),
    FOREIGN KEY (nls_paikkatyyppi_id) REFERENCES nls_paikkatyyppi (id),
    FOREIGN KEY (nls_paikkatyyppiryhma_id) REFERENCES nls_paikkatyyppiryhma (id),
    FOREIGN KEY (nls_paikkatyyppialaryhma_id) REFERENCES nls_paikkatyyppialaryhma (id),
    FOREIGN KEY (nls_kunta_id) REFERENCES nls_kunta (id),
    FOREIGN KEY (nls_seutukunta_id) REFERENCES nls_seutukunta (id),
    FOREIGN KEY (nls_maakunta_id) REFERENCES nls_maakunta (id),
    FOREIGN KEY (nls_suuralue_id) REFERENCES nls_suuralue (id)
);