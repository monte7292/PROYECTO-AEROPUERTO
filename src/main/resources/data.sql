------------------------------------------------------------
-- 20 DIRECTORES
------------------------------------------------------------
INSERT INTO director (nombre, apellidos)
VALUES ('Carlos', 'Gómez Ruiz'),
       ('María', 'López Díaz'),
       ('Javier', 'Martínez Pérez'),
       ('Laura', 'Santos Vega'),
       ('Andrés', 'Hernández Gil'),
       ('Sofía', 'Ortega León'),
       ('Pedro', 'Ramírez Torres'),
       ('Lucía', 'Navarro Ríos'),
       ('Daniel', 'Cano Pardo'),
       ('Elena', 'Bravo Cortés'),
       ('Miguel', 'Iglesias Mora'),
       ('Paula', 'Domínguez Rey'),
       ('Hugo', 'Fernández Soto'),
       ('Ana', 'Suárez Medina'),
       ('Luis', 'Castro Navas'),
       ('Carmen', 'Vega Roldán'),
       ('Mario', 'Silva Lozano'),
       ('Sara', 'Prieto Montes'),
       ('Pablo', 'Rey Salas'),
       ('Noelia', 'Benítez Lara');

------------------------------------------------------------
-- 20 AEROPUERTOS
------------------------------------------------------------
INSERT INTO aeropuerto (id_director, nombre, ciudad, pais, codIata)
VALUES (1, 'Aeropuerto Norte', 'Madrid', 'España', 'MAD'),
       (2, 'Aeropuerto Sur', 'Barcelona', 'España', 'BCN'),
       (3, 'Aeropuerto Costa', 'Valencia', 'España', 'VLC'),
       (4, 'Aeropuerto Atlántico', 'Sevilla', 'España', 'SVQ'),
       (5, 'Aeropuerto del Sol', 'Málaga', 'España', 'AGP'),
       (6, 'Aeropuerto Montaña', 'Bilbao', 'España', 'BIO'),
       (7, 'Aeropuerto Central', 'Zaragoza', 'España', 'ZAZ'),
       (8, 'Aeropuerto Insular', 'Palma', 'España', 'PMI'),
       (9, 'Aeropuerto Mediterráneo', 'Alicante', 'España', 'ALC'),
       (10, 'Aeropuerto Vasco', 'San Sebastián', 'España', 'EAS'),
       (11, 'Aeropuerto Galego', 'A Coruña', 'España', 'LCG'),
       (12, 'Aeropuerto Miño', 'Vigo', 'España', 'VGO'),
       (13, 'Aeropuerto Rioja', 'Logroño', 'España', 'RJL'),
       (14, 'Aeropuerto Duero', 'Salamanca', 'España', 'SLM'),
       (15, 'Aeropuerto León', 'León', 'España', 'LEN'),
       (16, 'Aeropuerto Asturias', 'Oviedo', 'España', 'OVD'),
       (17, 'Aeropuerto Pirineos', 'Huesca', 'España', 'HSK'),
       (18, 'Aeropuerto Navarra', 'Pamplona', 'España', 'PNA'),
       (19, 'Aeropuerto Manchego', 'Albacete', 'España', 'ABC'),
       (20, 'Aeropuerto Extremeño', 'Badajoz', 'España', 'BJZ');

------------------------------------------------------------
-- 20 AVIONES
------------------------------------------------------------
INSERT INTO avion (id_aeropuerto, modelo, fabricante, capacidad, estado)
VALUES (1, 'A320', 'Airbus', 180, 'Operativo'),
       (2, 'B737', 'Boeing', 160, 'Operativo'),
       (3, 'A319', 'Airbus', 150, 'Mantenimiento'),
       (4, 'E190', 'Embraer', 100, 'Operativo'),
       (5, 'A321', 'Airbus', 200, 'Operativo'),
       (6, 'B787', 'Boeing', 250, 'Operativo'),
       (7, 'B777', 'Boeing', 300, 'Operativo'),
       (8, 'CRJ900', 'Bombardier', 90, 'Mantenimiento'),
       (9, 'A330', 'Airbus', 260, 'Operativo'),
       (10, 'A350', 'Airbus', 300, 'Operativo'),
       (11, 'B747', 'Boeing', 300, 'Retirado'),
       (12, 'B757', 'Boeing', 180, 'Operativo'),
       (13, 'ATR72', 'ATR', 70, 'Operativo'),
       (14, 'E175', 'Embraer', 88, 'Operativo'),
       (15, 'B767', 'Boeing', 230, 'Mantenimiento'),
       (16, 'A340', 'Airbus', 260, 'Operativo'),
       (17, 'A380', 'Airbus', 300, 'Operativo'),
       (18, 'B727', 'Boeing', 140, 'Retirado'),
       (19, 'A318', 'Airbus', 120, 'Operativo'),
       (20, 'B737 MAX', 'Boeing', 175, 'Operativo');

------------------------------------------------------------
-- 20 TRABAJADORES
------------------------------------------------------------
INSERT INTO trabajador (id_avion, nombre, apellidos, cargo, fechaContratacion)
VALUES (1, 'Luis', 'Serrano López', 'Piloto', '2019-04-12'),
       (2, 'Marta', 'Vidal Ramos', 'Copiloto', '2020-03-21'),
       (3, 'Jorge', 'Reyes Ortiz', 'Técnico', '2018-06-15'),
       (4, 'Beatriz', 'Rubio Sáez', 'Azafata', '2021-09-10'),
       (5, 'Raúl', 'Muñoz Pastor', 'Piloto', '2017-01-05'),
       (6, 'Silvia', 'Molina Cruz', 'Azafata', '2022-02-11'),
       (7, 'Iván', 'Santos Cabrera', 'Técnico', '2016-07-18'),
       (8, 'Clara', 'Gallego Pino', 'Copiloto', '2020-11-22'),
       (9, 'Tomás', 'Luque Bravo', 'Piloto', '2015-12-30'),
       (10, 'Alba', 'Esteban Lara', 'Azafata', '2019-08-14'),
       (11, 'Nuria', 'Soria Prats', 'Técnica', '2018-03-03'),
       (12, 'David', 'Corral Nieto', 'Piloto', '2021-05-25'),
       (13, 'Julia', 'Román Vera', 'Azafata', '2022-10-19'),
       (14, 'Óscar', 'Herrera Peña', 'Copiloto', '2017-04-07'),
       (15, 'Elisa', 'Montoro Suárez', 'Técnica', '2023-01-29'),
       (16, 'Adrián', 'Perales León', 'Piloto', '2016-09-13'),
       (17, 'Patricia', 'Fuentes Mora', 'Azafata', '2020-12-01'),
       (18, 'Rubén', 'Delgado Serra', 'Técnico', '2019-03-17'),
       (19, 'Eva', 'Ríos Zamora', 'Azafata', '2021-06-02'),
       (20, 'Sergio', 'Crespo Dávila', 'Piloto', '2018-10-08');

------------------------------------------------------------
-- 20 RUTAS
------------------------------------------------------------
INSERT INTO ruta (idAeropuertoOrigen, idAeropuertoDestino, duracion, distancia)
VALUES (1, 2, 90, 505),
       (2, 3, 55, 303),
       (3, 4, 70, 350),
       (4, 5, 60, 250),
       (5, 6, 80, 450),
       (6, 7, 50, 290),
       (7, 8, 75, 390),
       (8, 9, 45, 210),
       (9, 10, 65, 330),
       (10, 11, 85, 470),
       (11, 12, 40, 180),
       (12, 13, 95, 520),
       (13, 14, 55, 270),
       (14, 15, 70, 355),
       (15, 16, 60, 260),
       (16, 17, 50, 240),
       (17, 18, 110, 600),
       (18, 19, 75, 380),
       (19, 20, 65, 340),
       (20, 1, 120, 650);

------------------------------------------------------------
-- 20 PASAJEROS
------------------------------------------------------------
INSERT INTO pasajero (nombre, apellidos, documento, email)
VALUES ('Javier', 'López Ruiz', '12345678A', 'jlopez@mail.com'),
       ('María', 'Díaz Soto', '98765432B', 'mdiaz@mail.com'),
       ('Luis', 'Martín Vera', '11223344C', 'lmartin@mail.com'),
       ('Ana', 'Santos Del Río', '55667788D', 'asantos@mail.com'),
       ('Pedro', 'Navas Gil', '44332211E', 'pnavas@mail.com'),
       ('Lucía', 'Peña Ramos', '77889900F', 'lpena@mail.com'),
       ('Sergio', 'Bravo Cruz', '99887766G', 'sbravo@mail.com'),
       ('Inés', 'Romero Sáez', '22113344H', 'iromero@mail.com'),
       ('Hugo', 'Castro León', '33221144I', 'hcastro@mail.com'),
       ('Alba', 'Torres Mora', '66554433J', 'atorres@mail.com'),
       ('Raúl', 'Benito Rey', '22334455K', 'rbenito@mail.com'),
       ('Clara', 'Soria Arias', '44556677L', 'csoria@mail.com'),
       ('Tomás', 'Campos Pardo', '55664433M', 'tcampos@mail.com'),
       ('Sara', 'Estévez Soto', '99880022N', 'sestevez@mail.com'),
       ('Nuria', 'Vega Ramos', '11220033O', 'nvega@mail.com'),
       ('Pablo', 'Rey Díaz', '33445566P', 'prey@mail.com'),
       ('Julia', 'Prieto Salas', '66778899Q', 'jprieto@mail.com'),
       ('Mario', 'Domínguez Vera', '88997766R', 'mdominguez@mail.com'),
       ('Elena', 'Iglesias Cortés', '12344321S', 'eiglesias@mail.com'),
       ('David', 'Mora Lozano', '55443322T', 'dmora@mail.com');

------------------------------------------------------------
-- 20 TICKETS
------------------------------------------------------------
INSERT INTO ticket (id_ruta, id_pasajero, asiento, precio, fechaCompra)
VALUES (1, 1, '1A', 120.50, '2024-01-10'),
       (2, 2, '3C', 89.99, '2024-02-15'),
       (3, 3, '4B', 70.00, '2024-03-12'),
       (4, 4, '6D', 65.25, '2024-04-01'),
       (5, 5, '2A', 140.80, '2024-04-18'),
       (6, 6, '7F', 95.10, '2024-05-10'),
       (7, 7, '8A', 110.40, '2024-05-25'),
       (8, 8, '9C', 75.30, '2024-06-02'),
       (9, 9, '10B', 99.99, '2024-06-14'),
       (10, 10, '12A', 130.00, '2024-06-28'),
       (11, 11, '13F', 80.60, '2024-07-05'),
       (12, 12, '14C', 105.20, '2024-07-15'),
       (13, 13, '15D', 115.40, '2024-08-01'),
       (14, 14, '16A', 125.90, '2024-08-12'),
       (15, 15, '17B', 90.00, '2024-08-30'),
       (16, 16, '18C', 98.75, '2024-09-10'),
       (17, 17, '19F', 150.00, '2024-09-25'),
       (18, 18, '20A', 85.20, '2024-10-02'),
       (19, 19, '21C', 95.99, '2024-10-18'),
       (20, 20, '22B', 160.00, '2024-11-01');

------------------------------------------------------------
-- 20 RUTA_AVION
------------------------------------------------------------
INSERT INTO ruta_avion (id_ruta, id_avion)
VALUES (1, 1),
       (2, 2),
       (3, 3),
       (4, 4),
       (5, 5),
       (6, 6),
       (7, 7),
       (8, 8),
       (9, 9),
       (10, 10),
       (11, 11),
       (12, 12),
       (13, 13),
       (14, 14),
       (15, 15),
       (16, 16),
       (17, 17),
       (18, 18),
       (19, 19),
       (20, 20);

------------------------------------------------------------
-- 20 PASAJERO_AVION
------------------------------------------------------------
INSERT INTO pasajero_avion (id_pasajero, id_avion)
VALUES (1, 1),
       (2, 2),
       (3, 3),
       (4, 4),
       (5, 5),
       (6, 6),
       (7, 7),
       (8, 8),
       (9, 9),
       (10, 10),
       (11, 11),
       (12, 12),
       (13, 13),
       (14, 14),
       (15, 15),
       (16, 16),
       (17, 17),
       (18, 18),
       (19, 19),
       (20, 20);
