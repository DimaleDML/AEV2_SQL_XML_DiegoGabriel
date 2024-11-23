##Crear Base de Datos
CREATE DATABASE IF NOT EXISTS population CHARACTER SET = UTF8;

##Usar BS population
USE population;

##Crear tabla Users si no existe
CREATE TABLE IF NOT EXISTS users (
  id INT(10) AUTO_INCREMENT NOT NULL,
  login VARCHAR(20) NOT NULL,
  password VARCHAR(40) NOT NULL,
  TYPE VARCHAR(6) NOT NULL,
  PRIMARY KEY (id)
);

##Crear usuario admin en la BD de MySql y Permisos de Admin
CREATE USER 'admin' IDENTIFIED BY '21232f297a57a5a743894a0e4a801fc3';
GRANT ALL PRIVILEGES ON *.* TO 'admin' WITH GRANT OPTION;
FLUSH PRIVILEGES;

##Insertar admin en la tabla users
INSERT INTO users (login,password,type) VALUES ("admin","21232f297a57a5a743894a0e4a801fc3","admin");

##Comprobacion de que el usuario esta realmente creado
SELECT User, Host, Password FROM mysql.user;

