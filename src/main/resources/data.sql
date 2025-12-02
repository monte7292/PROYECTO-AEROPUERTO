-- ===========================================
-- INSERTS PARA TODAS LAS TABLAS (20 REGISTROS CADA UNA)
-- ===========================================

-- =========================
-- 1. DIRECTOR
-- =========================
INSERT INTO director (nombre, apellidos)
VALUES ('Carlos', 'Gómez Ruiz'),
       ('María', 'Fernández Soto'),
       ('Javier', 'López García'),
       ('Ana', 'Martín Pérez'),
       ('Luis', 'Sánchez Ortiz'),
       ('Elena', 'Ramírez Cano'),
       ('Pablo', 'Torres Vidal'),
       ('Sofía', 'Hernández León'),
       ('Miguel', 'Castro Rivas'),
       ('Laura', 'Domínguez Vela'),
       ('Diego', 'Prieto Núñez'),
       ('Sara', 'Molina Casas'),
       ('Rubén', 'Cortés Ríos'),
       ('Lucía', 'Gallardo Díaz'),
       ('Hugo', 'Navarro Vera'),
       ('Nuria', 'Rey Blanco'),
       ('Álvaro', 'Benítez Ramos'),
       ('Irene', 'Campos Segura'),
       ('Raúl', 'Peña Montes'),
       ('Claudia', 'Suárez Lara');

-- =========================
-- 2. AEROPUERTO
-- =========================
INSERT INTO aeropuerto (id_director, nombre, ciudad, pais, codIata)
VALUES (1, 'Aeropuerto Barajas', 'Madrid', 'España', 'MAD'),
       (2, 'Aeropuerto El Prat', 'Barcelona', 'España', 'BCN'),
       (3, 'Aeropuerto de Sevilla', 'Sevilla', 'España', 'SVQ'),
       (4, 'Aeropuerto de Málaga', 'Málaga', 'España', 'AGP'),
       (5, 'Aeropuerto de Valencia', 'Valencia', 'España', 'VLC'),
       (6, 'Charles de Gaulle', 'París', 'Francia', 'CDG'),
       (7, 'Orly', 'París', 'Francia', 'ORY'),
       (8, 'Heathrow', 'Londres', 'Reino Unido', 'LHR'),
       (9, 'Gatwick', 'Londres', 'Reino Unido', 'LGW'),
       (10, 'Frankfurt Airport', 'Frankfurt', 'Alemania', 'FRA'),
       (11, 'Munich Airport', 'Munich', 'Alemania', 'MUC'),
       (12, 'Roma Fiumicino', 'Roma', 'Italia', 'FCO'),
       (13, 'Roma Ciampino', 'Roma', 'Italia', 'CIA'),
       (14, 'Lisbon Airport', 'Lisboa', 'Portugal', 'LIS'),
       (15, 'Porto Airport', 'Porto', 'Portugal', 'OPO'),
       (16, 'Amsterdam Schiphol', 'Ámsterdam', 'Países Bajos', 'AMS'),
       (17, 'Brussels Airport', 'Bruselas', 'Bélgica', 'BRU'),
       (18, 'Zurich Airport', 'Zúrich', 'Suiza', 'ZRH'),
       (19, 'Vienna Airport', 'Viena', 'Austria', 'VIE'),
       (20, 'Copenhagen Airport', 'Copenhague', 'Dinamarca', 'CPH');

-- =========================
-- 3. AVION
-- =========================
INSERT INTO avion (id_aeropuerto, modelo, fabricante, capacidad, estado)
VALUES (1, 'A320', 'Airbus', 180, 'Operativo'),
       (2, 'B737', 'Boeing', 160, 'Mantenimiento'),
       (3, 'A330', 'Airbus', 250, 'Operativo'),
       (4, 'B777', 'Boeing', 300, 'Operativo'),
       (5, 'A350', 'Airbus', 300, 'Operativo'),
       (6, 'B787', 'Boeing', 290, 'En reparación'),
       (7, 'A319', 'Airbus', 140, 'Operativo'),
       (8, 'B747', 'Boeing', 380, 'Operativo'),
       (9, 'A321', 'Airbus', 200, 'Operativo'),
       (10, 'B767', 'Boeing', 220, 'Operativo'),
       (11, 'A340', 'Airbus', 280, 'Mantenimiento'),
       (12, 'B737 MAX', 'Boeing', 170, 'Operativo'),
       (13, 'A320neo', 'Airbus', 180, 'Operativo'),
       (14, 'B757', 'Boeing', 200, 'En reparación'),
       (15, 'A380', 'Airbus', 500, 'Operativo'),
       (16, 'B787-9', 'Boeing', 280, 'Operativo'),
       (17, 'A220', 'Airbus', 120, 'Operativo'),
       (18, 'B737-900', 'Boeing', 180, 'Operativo'),
       (19, 'A321neo', 'Airbus', 200, 'Operativo'),
       (20, 'B777X', 'Boeing', 320, 'Operativo');

-- =========================
-- 4. TRABAJADOR
-- =========================
INSERT INTO trabajador (id_avion, nombre, apellidos, cargo, fechaContratacion)
VALUES (1, 'Luis', 'García Soto', 'Piloto', '2018-05-12'),
       (2, 'Ana', 'Pérez Ruiz', 'Azafata', '2020-03-20'),
       (3, 'Mario', 'López Vega', 'Copiloto', '2017-11-02'),
       (4, 'Elena', 'Torres Díaz', 'Técnico', '2019-07-14'),
       (5, 'Carlos', 'Núñez Ramos', 'Mecánico', '2016-01-18'),
       (6, 'Marta', 'Gómez Lara', 'Azafata', '2021-09-10'),
       (7, 'David', 'Ramírez León', 'Piloto', '2015-06-22'),
       (8, 'Sara', 'Herrera Cortés', 'Azafata', '2022-10-30'),
       (9, 'Pablo', 'Reyes Blanco', 'Copiloto', '2018-08-15'),
       (10, 'Laura', 'Benítez Castelo', 'Técnico', '2017-02-11'),
       (11, 'Hugo', 'Navarro Sanz', 'Piloto', '2019-04-08'),
       (12, 'Clara', 'Sáez Campos', 'Azafata', '2020-12-19'),
       (13, 'Jorge', 'Martínez Pardo', 'Mecánico', '2016-09-25'),
       (14, 'Lucía', 'Soria Vega', 'Piloto', '2018-03-01'),
       (15, 'Raúl', 'Salas Torres', 'Azafata', '2023-06-05'),
       (16, 'Nuria', 'Domínguez Ríos', 'Técnico', '2022-01-17'),
       (17, 'Álvaro', 'Santos Vidal', 'Piloto', '2017-05-20'),
       (18, 'Irene', 'Campos Vela', 'Copiloto', '2019-09-09'),
       (19, 'Claudia', 'Morales Cano', 'Azafata', '2021-04-14'),
       (20, 'Rubén', 'Rivas Peña', 'Mecánico', '2015-12-03');

-- =========================
-- 5. RUTA
-- =========================
INSERT INTO ruta (idAeropuertoOrigen, idAeropuertoDestino, duracion, distancia)
VALUES (1, 2, 85, 505),
       (3, 1, 95, 540),
       (4, 5, 60, 430),
       (6, 8, 80, 470),
       (7, 6, 40, 220),
       (9, 8, 35, 60),
       (10, 11, 50, 300),
       (12, 14, 120, 950),
       (15, 14, 30, 280),
       (16, 10, 70, 580),
       (17, 18, 90, 760),
       (19, 20, 45, 320),
       (2, 6, 105, 860),
       (3, 7, 110, 900),
       (4, 9, 140, 1100),
       (5, 12, 150, 1200),
       (13, 11, 115, 930),
       (8, 1, 130, 1260),
       (14, 16, 160, 1390),
       (20, 3, 170, 1500);

-- =========================
-- 6. PASAJERO
-- =========================
INSERT INTO pasajero (nombre, apellidos, documento, email)
VALUES ('Javier', 'Soto Reyes', 'X1234567', 'javier.soto@mail.com'),
       ('María', 'Lara Gómez', 'Y7654321', 'marialara@mail.com'),
       ('Pedro', 'Núñez Díaz', 'A9876543', 'pedronu@mail.com'),
       ('Lucía', 'Vera Campos', 'B1239876', 'luciavera@mail.com'),
       ('Marta', 'Sanz Rubio', 'C6547891', 'martasanz@mail.com'),
       ('Antonio', 'Díaz Herrera', 'D9513572', 'antoniod@mail.com'),
       ('Raquel', 'Pardo León', 'E6543219', 'raquelp@mail.com'),
       ('Sergio', 'Prieto Alba', 'F7539518', 'sergiopri@mail.com'),
       ('Alba', 'Cano Torres', 'G8524569', 'albacano@mail.com'),
       ('Héctor', 'Salas Ruiz', 'H1593578', 'hectors@mail.com'),
       ('Laura', 'Molina Soria', 'I3571594', 'lauramolina@mail.com'),
       ('Diego', 'Campos Vera', 'J1112223', 'diegoc@mail.com'),
       ('Sara', 'López Mora', 'K2221114', 'saralopez@mail.com'),
       ('Álvaro', 'Rivas Cano', 'L3334445', 'alvaror@mail.com'),
       ('Elena', 'Gómez Vega', 'M4443336', 'elenagv@mail.com'),
       ('Pablo', 'Rey Ramos', 'N5556667', 'pablor@mail.com'),
       ('Claudia', 'Torres Díaz', 'O6665558', 'claudiatd@mail.com'),
       ('Irene', 'Castro Vidal', 'P7778889', 'irenecv@mail.com'),
       ('Rubén', 'Hernández Pino', 'Q8887770', 'rubenhp@mail.com'),
       ('Nuria', 'Navarro Soto', 'R9990001', 'nurian@mail.com');

-- =========================
-- 7. TICKET
-- =========================
INSERT INTO ticket (id_ruta, id_pasajero, asiento, precio, fechaCompra)
VALUES (1, 1, '12A', 120.50, '2024-01-10'),
       (2, 2, '14C', 98.00, '2024-01-12'),
       (3, 3, '03B', 150.75, '2024-01-15'),
       (4, 4, '20D', 200.00, '2024-01-18'),
       (5, 5, '07A', 180.20, '2024-01-20'),
       (6, 6, '11F', 90.99, '2024-01-22'),
       (7, 7, '02C', 110.40, '2024-01-25'),
       (8, 8, '16B', 250.60, '2024-01-27'),
       (9, 9, '08E', 70.00, '2024-01-29'),
       (10, 10, '05A', 125.45, '2024-02-01'),
       (11, 11, '09D', 135.60, '2024-02-03'),
       (12, 12, '10A', 145.30, '2024-02-05'),
       (13, 13, '14F', 210.80, '2024-02-06'),
       (14, 14, '17C', 230.90, '2024-02-07'),
       (15, 15, '19A', 260.00, '2024-02-09'),
       (16, 16, '21B', 175.25, '2024-02-11'),
       (17, 17, '06D', 190.15, '2024-02-12'),
       (18, 18, '13E', 120.80, '2024-02-13'),
       (19, 19, '04F', 160.20, '2024-02-14'),
       (20, 20, '22C', 300.00, '2024-02-15');
