INSERT INTO director (id, nombre, apellidos) VALUES
(1,'Juan','Pérez'),
(2,'María','López'),
(3,'Carlos','García'),
(4,'Ana','Martín'),
(5,'Luis','Rodríguez'),
(6,'Elena','Sánchez'),
(7,'Miguel','Gómez'),
(8,'Sara','Díaz'),
(9,'Javier','Fernández'),
(10,'Laura','Ruiz'),
(11,'Diego','Torres'),
(12,'Carmen','Castro'),
(13,'Pablo','Vargas'),
(14,'Lucía','Ortega'),
(15,'Andrés','Navarro'),
(16,'Sofía','Moreno'),
(17,'Alberto','Domínguez'),
(18,'Natalia','Hernández'),
(19,'Raúl','Iglesias'),
(20,'Patricia','Núñez');

INSERT INTO aeropuerto (id_director, nombre, ciudad, pais, codIata) VALUES
(1,'Aeropuerto Adolfo Suárez Madrid-Barajas','Madrid','España','MAD'),
(2,'Aeropuerto Josep Tarradellas Barcelona-El Prat','Barcelona','España','BCN'),
(3,'Aeropuerto de Sevilla','Sevilla','España','SVQ'),
(4,'Aeropuerto de Valencia','Valencia','España','VLC'),
(5,'Aeropuerto de Bilbao','Bilbao','España','BIO'),
(6,'Aeropuerto de Palma de Mallorca','Palma','España','PMI'),
(7,'Aeropuerto de Málaga-Costa del Sol','Málaga','España','AGP'),
(8,'Aeropuerto de Gran Canaria','Las Palmas','España','LPA'),
(9,'Aeropuerto de Tenerife Sur','Tenerife','España','TFS'),
(10,'Aeropuerto de Tenerife Norte','Santa Cruz de Tenerife','España','TFN'),
(11,'Aeropuerto de Alicante-Elche Miguel Hernández','Alicante','España','ALC'),
(12,'Aeropuerto de Santiago de Compostela','Santiago de Compostela','España','SCQ'),
(13,'Aeropuerto de Asturias','Oviedo','España','OVD'),
(14,'Aeropuerto de Zaragoza','Zaragoza','España','ZAZ'),
(15,'Aeropuerto Federico García Lorca Granada-Jaén','Granada','España','GRX'),
(16,'Aeropuerto de Vigo','Vigo','España','VGO'),
(17,'Aeropuerto de Jerez','Jerez de la Frontera','España','XRY'),
(18,'Aeropuerto de Ibiza','Ibiza','España','IBZ'),
(19,'Aeropuerto de Menorca','Mahón','España','MAH'),
(20,'Aeropuerto de San Sebastián','San Sebastián','España','EAS');

INSERT INTO avion (id_aeropuerto, modelo, fabricante, capacidad, estado) VALUES
(1,'A320','Airbus',180,'Operativo'),
(2,'B737-800','Boeing',160,'Operativo'),
(3,'A350-900','Airbus',300,'Operativo'),
(4,'B787-9','Boeing',290,'Operativo'),
(5,'E190','Embraer',110,'Mantenimiento'),
(1,'CRJ900','Bombardier',90,'Operativo'),
(7,'A321neo','Airbus',220,'Operativo'),
(8,'B777-300ER','Boeing',396,'Operativo'),
(9,'ATR72-600','ATR',72,'Operativo'),
(10,'A330-200','Airbus',268,'Fuera de servicio');

INSERT INTO trabajador (id_avion, nombre, apellidos, cargo, fechaContratacion) VALUES
(1,'Samuel','Pérez','Piloto','2020-01-15'),
(1,'Laura','Martín','Copiloto','2021-03-10'),
(2,'Carlos','Gómez','Azafato','2019-06-21'),
(2,'Marta','Santos','Azafata','2022-02-18'),
(3,'Ana','López','Mecánico','2018-11-05'),
(4,'Javier','Ruiz','Piloto','2020-07-12'),
(5,'Sofía','Navarro','Azafata','2017-04-09'),
(6,'Diego','Torres','Mecánico','2016-09-23'),
(7,'Lucía','Ortega','Piloto','2021-12-01'),
(8,'Pablo','Vargas','Copiloto','2022-08-16'),
(9,'Elena','Sánchez','Azafata','2019-05-30'),
(10,'Raúl','Iglesias','Mecánico','2015-03-27');

INSERT INTO pasajero (nombre, apellidos, documento, email) VALUES
('Mario','Alonso','12345678A','mario.alonso@example.com'),
('Beatriz','Cano','87654321B','beatriz.cano@example.com'),
('Daniel','Suárez','11223344C','daniel.suarez@example.com'),
('Paula','Ramos','44332211D','paula.ramos@example.com'),
('Sergio','Lara','55667788E','sergio.lara@example.com'),
('Noelia','Prieto','88776655F','noelia.prieto@example.com'),
('Hugo','Vega','99887766G','hugo.vega@example.com'),
('Irene','Molina','66778899H','irene.molina@example.com'),
('Rubén','Navas','33445566J','ruben.navas@example.com'),
('Alicia','Soto','22113344K','alicia.soto@example.com'),
('Adrián','León','55224477L','adrian.leon@example.com'),
('Nuria','Campos','77889900M','nuria.campos@example.com');

INSERT INTO ruta (idAeropuertoOrigen, idAeropuertoDestino, duracion, distancia) VALUES
(1,2,75,505),
(3,1,65,391),
(5,4,70,469),
(7,1,80,415),
(9,1,130,1800),
(10,2,135,2190),
(12,13,45,250),
(14,1,60,274);

INSERT INTO ticket (id_ruta, id_pasajero, asiento, precio, fechaCompra) VALUES
(1,1,'12A',89.99,'2024-09-01'),
(2,2,'14C',79.50,'2024-09-10'),
(3,3,'07B',59.00,'2024-10-05'),
(4,4,'21D',69.99,'2024-10-12'),
(5,5,'03A',199.90,'2024-11-01'),
(6,6,'18F',210.00,'2024-11-15'),
(7,7,'09E',49.99,'2024-09-20'),
(8,8,'22A',89.00,'2024-12-01'),
(1,9,'10B',95.00,'2024-08-25'),
(2,10,'11C',80.75,'2024-08-30');

INSERT INTO ruta_avion (id_ruta, id_avion) VALUES
(1,1),
(1,7),
(2,2),
(3,5),
(4,6),
(5,8),
(6,10),
(7,9),
(8,4);

INSERT INTO pasajero_avion (id_pasajero, id_avion) VALUES
(1,1),
(2,2),
(3,5),
(4,6),
(5,8),
(6,10),
(7,9),
(8,4),
(9,1),
(10,2),
(11,3),
(12,7);
