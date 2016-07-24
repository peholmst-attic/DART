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

CREATE INDEX nls_kunta_names ON nls_kunta (name_fi, name_sv);
CREATE INDEX nls_kunta_name_fi ON nls_kunta (name_fi);
CREATE INDEX nls_kunta_name_sv ON nls_kunta (name_sv);
CREATE INDEX nls_kunta_name_en ON nls_kunta (name_en);

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

CREATE TABLE nls_kieli (
    id varchar(3) not null,
    name_fi varchar(200) not null,
    name_sv varchar(200) not null,
    name_en varchar(200) not null,
    PRIMARY KEY (id)
);

CREATE TABLE nls_kieli_enemmisto (
    id bigint not null,
    name_fi varchar(200) not null,
    name_sv varchar(200) not null,
    name_en varchar(200) not null,
    PRIMARY KEY (id)
);

CREATE TABLE nls_kieli_virallisuus (
    id bigint not null,
    name_fi varchar(200) not null,
    name_sv varchar(200) not null,
    name_en varchar(200) not null,
    PRIMARY KEY (id)
);

CREATE TABLE nls_paikka (
    id bigint not null,
    nls_paikkatyyppi_id bigint not null,
    nls_paikkatyyppiryhma_id bigint not null,
    nls_paikkatyyppialaryhma_id bigint not null,
    location geometry(point,4326) not null,
    altitude int,
    tm35fin7_code varchar(200),
    ylj7_code varchar(200),
    pp6_code varchar(200),
    nls_kunta_id bigint not null,
    nls_seutukunta_id bigint not null,
    nls_maakunta_id bigint not null,
    nls_suuralue_id bigint not null,
    -- TODO mittakaavarelevanssi
    created timestamp not null,
    modified timestamp not null,
    PRIMARY KEY (id),
    FOREIGN KEY (nls_paikkatyyppi_id) REFERENCES nls_paikkatyyppi (id),
    FOREIGN KEY (nls_paikkatyyppiryhma_id) REFERENCES nls_paikkatyyppiryhma (id),
    FOREIGN KEY (nls_paikkatyyppialaryhma_id) REFERENCES nls_paikkatyyppialaryhma (id),
    FOREIGN KEY (nls_kunta_id) REFERENCES nls_kunta (id),
    FOREIGN KEY (nls_seutukunta_id) REFERENCES nls_seutukunta (id),
    FOREIGN KEY (nls_maakunta_id) REFERENCES nls_maakunta (id),
    FOREIGN KEY (nls_suuralue_id) REFERENCES nls_suuralue (id)
);

CREATE INDEX nls_paikka_tm35fin7_code ON nls_paikka (tm35fin7_code);
CREATE INDEX nls_paikka_ylj7_code ON nls_paikka (ylj7_code);
CREATE INDEX nls_paikka_pp6_code ON nls_paikka (pp6_code);

CREATE TABLE nls_paikannimi (
    id bigint not null,
    name varchar (200) not null,
    nls_kieli_id varchar(3) not null,
    nls_kieli_virallisuus_id bigint not null,
    nls_kieli_enemmisto_id bigint not null,
    nls_paikka_id bigint not null,
    created timestamp not null,
    modified timestamp,
    PRIMARY KEY (id),
    FOREIGN KEY (nls_kieli_id) REFERENCES nls_kieli (id),
    FOREIGN KEY (nls_kieli_virallisuus_id) REFERENCES nls_kieli_virallisuus (id),
    FOREIGN KEY (nls_kieli_enemmisto_id) REFERENCES nls_kieli_enemmisto (id),
    FOREIGN KEY (nls_paikka_id) REFERENCES nls_paikka (id)
);

CREATE INDEX nls_paikannimi_name ON nls_paikannimi (name);
CREATE INDEX nls_paikannimi_name_kieli_id ON nls_paikannimi (name, nls_kieli_id);
