CREATE DATABASE ogame;
/* Crear al usuario que podrá acceder desde cualquier dirección ip */
CREATE USER 'ogame'@'%' IDENTIFIED BY 'Ogame123';
/* Darle todos los permisos en la base de datos */
GRANT ALL PRIVILEGES ON ogame.* TO 'ogame'@'%';

use ogame;
/* Tabla para los logs*/
CREATE TABLE logs(
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    fecha datetime,
    texto VARCHAR(100)
);
/* Tabla para las granjas */
CREATE TABLE granjas(
    planeta int,
    sistema int,
    galaxia int,
     PRIMARY KEY (planeta, sistema, galaxia)
);

CREATE TABLE properties(
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    planta_energia int,
    sintetizador_deuterio float,
    small_cargo int,
    big_cargo int,
    expedicion VARCHAR(15),
    colonia_big_cargo int,
    colonial_resources int
);

CREATE TABLE posiciones(
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    posicion int
);

CREATE TABLE cuentas(
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user VARCHAR(20),
    password VARCHAR(20),
    servidor VARCHAR(3),
    universo int    
);

/* Este es para las granjas */
insert into posiciones values(1, 0);
/* Este es para las expediciones */
insert into posiciones values(2, 104);