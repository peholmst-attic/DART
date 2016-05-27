INSERT INTO resource_state_descriptors VALUES ('UNAVAILABLE', 'Ej alarmerbar', 'Ei hälytettävissä', 'Out of service', false, 'cccccc');
INSERT INTO resource_state_descriptors VALUES ('AVAILABLE_AT_STATION', 'Ledig på stationen', 'Vapaa asemalla', 'At station', false, '186a3b');
INSERT INTO resource_state_descriptors VALUES ('AVAILABLE', 'Ledig', 'Vapaa', 'Available', true, '28b463');
INSERT INTO resource_state_descriptors VALUES ('RESERVED_AT_STATION', 'Reserverad på stationen', 'Varattu asemalla', 'Reserved at station', false, 'c0392b');
INSERT INTO resource_state_descriptors VALUES ('RESERVED', 'Reserverad', 'Varattu', 'Reserved', true, 'c0392b');
INSERT INTO resource_state_descriptors VALUES ('DISPATCHED_AT_STATION', 'Alarmerad på stationen', 'Hälytetty asemalla', 'Dispatched at station', false, 'e67e22');
INSERT INTO resource_state_descriptors VALUES ('DISPATCHED', 'Alarmerad', 'Hälytetty', 'Dispatched', true, 'e67e22');
INSERT INTO resource_state_descriptors VALUES ('EN_ROUTE', 'På väg', 'Matkalla', 'En route', true, '3498db');
INSERT INTO resource_state_descriptors VALUES ('ON_SCENE', 'På plats', 'Paikalla', 'On scene', true, 'f1c40f');

INSERT INTO capabilities VALUES (1, 'PUMPER', 'Släckningsbil', 'Sammutusauto', 'Pumper');
INSERT INTO capabilities VALUES (2, 'TANKER', 'Tankbil', 'Säiliöauto', 'Tanker');
INSERT INTO capabilities VALUES (3, 'EMS', 'Första respons', 'Ensivaste', 'EMS');
INSERT INTO capabilities VALUES (4, 'SCBA', 'Rökdykning', 'Savusukellus', 'SCBA');
INSERT INTO capabilities VALUES (5, 'SURFACE_RESCUE', 'Yträddning', 'Pintapelastus', 'Surface water rescue');
INSERT INTO capabilities VALUES (6, 'RESCUE_DIVERS', 'Räddningsdykare', 'Pelastussukeltajat', 'Rescue divers');
INSERT INTO capabilities VALUES (7, 'HAZMAT', 'Kemdykning', 'Kemikaalisukellus', 'Hazmat');
INSERT INTO capabilities VALUES (8, 'IC', 'Insatsledare', 'Pelastustoimen johtaja', 'Incident commander');

ALTER SEQUENCE capabilities_id_seq RESTART WITH 100;
