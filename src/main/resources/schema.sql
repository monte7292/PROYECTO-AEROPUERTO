CREATE TABLE IF NOT EXISTS director (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS aeropuerto (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_director INT NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    ciudad VARCHAR(100) NOT NULL,
    pais VARCHAR(100) NOT NULL,
    codIata VARCHAR(3) NOT NULL UNIQUE,
    FOREIGN KEY (id_director) REFERENCES director(id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS avion (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_aeropuerto INT NOT NULL,
    modelo VARCHAR(100) NOT NULL,
    fabricante VARCHAR(100) NOT NULL,
    --La capacidad será maxima de un número de 3 digitios 300 personas o así
    capacidad INT(3) NOT NULL,
    estado VARCHAR(100) NOT NULL,
    FOREIGN KEY (id_aeropuerto) REFERENCES aeropuerto(id) ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS trabajador (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_avion INT,
    nombre VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    cargo VARCHAR(100),
    fechaContratacion DATE NOT NULL, --De back al front timestamp.
    FOREIGN KEY (id_avion) REFERENCES avion(id) ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS ruta (
    id INT AUTO_INCREMENT PRIMARY KEY,
    idAeropuertoOrigen INT NOT NULL,
    idAeropuertoDestino INT NOT NULL,
    duracion INT NOT NULL, --La duración en minutos.
    distancia INT NOT NULL, --La distancia en km.
    FOREIGN KEY (idAeropuertoOrigen) REFERENCES aeropuerto(id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (idAeropuertoDestino) REFERENCES aeropuerto(id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS pasajero (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    documento VARCHAR(100) NOT NULL,
    email VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS ticket (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_ruta INT NOT NULL,
    id_pasajero INT NOT NULL,
    asiento VARCHAR(3) NOT NULL,
    precio DOUBLE NOT NULL,
    fechaCompra DATE NOT NULL,
    FOREIGN KEY (id_ruta) REFERENCES ruta(id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (id_pasajero) REFERENCES pasajero(id) ON DELETE CASCADE ON UPDATE CASCADE
);


CREATE TABLE IF NOT EXISTS ruta_avion (
    id_ruta INT NOT NULL,
    id_avion INT NOT NULL,
    PRIMARY KEY (id_ruta, id_avion),
    FOREIGN KEY (id_ruta) REFERENCES ruta(id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (id_avion) REFERENCES avion(id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS pasajero_avion (
    id_pasajero INT NOT NULL,
    id_avion INT NOT NULL,
    PRIMARY KEY (id_pasajero, id_avion),
    FOREIGN KEY (id_pasajero) REFERENCES pasajero(id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (id_avion) REFERENCES avion(id) ON DELETE CASCADE ON UPDATE CASCADE
);