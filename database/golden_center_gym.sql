-- Script Base de Datos: golden_center_gym
-- Proyecto: Sistema de Gestión Golden Center Gym

CREATE DATABASE IF NOT EXISTS golden_center_gym
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE golden_center_gym;

CREATE TABLE rol (
    id_rol INT PRIMARY KEY AUTO_INCREMENT,
    nombre_rol VARCHAR(30) NOT NULL
);

CREATE TABLE cliente (
    id_cliente INT PRIMARY KEY AUTO_INCREMENT,
    nombre_completo VARCHAR(100) NOT NULL,
    documento VARCHAR(20) NOT NULL UNIQUE,
    correo VARCHAR(100) NOT NULL UNIQUE,
    telefono VARCHAR(15),
    estado VARCHAR(20) NOT NULL
);

CREATE TABLE membresia (
    id_membresia INT PRIMARY KEY AUTO_INCREMENT,
    fecha_inicio DATE NOT NULL,
    fecha_vencimiento DATE NOT NULL,
    duracion INT NOT NULL,
    costo DECIMAL(10,2) NOT NULL,
    tipo_membresia VARCHAR(30) NOT NULL,
    estado VARCHAR(20) NOT NULL,
    id_cliente INT NOT NULL,
    CONSTRAINT fk_membresia_cliente
        FOREIGN KEY (id_cliente)
        REFERENCES cliente(id_cliente)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

CREATE TABLE usuario (
    id_usuario INT PRIMARY KEY AUTO_INCREMENT,
    nombre_usuario VARCHAR(30) NOT NULL UNIQUE,
    contrasena VARCHAR(255) NOT NULL,
    estado VARCHAR(20) NOT NULL,
    id_rol INT NOT NULL,
    CONSTRAINT fk_usuario_rol
        FOREIGN KEY (id_rol)
        REFERENCES rol(id_rol)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

CREATE TABLE asistencia (
    id_asistencia INT PRIMARY KEY AUTO_INCREMENT,
    fecha DATE NOT NULL,
    hora TIME NOT NULL,
    id_cliente INT NOT NULL,
    id_usuario INT NOT NULL,
    CONSTRAINT fk_asistencia_cliente
        FOREIGN KEY (id_cliente)
        REFERENCES cliente(id_cliente)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT fk_asistencia_usuario
        FOREIGN KEY (id_usuario)
        REFERENCES usuario(id_usuario)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

CREATE TABLE pago (
    id_pago INT PRIMARY KEY AUTO_INCREMENT,
    fecha_pago DATE NOT NULL,
    valor DECIMAL(10,2) NOT NULL,
    metodo_pago VARCHAR(30) NOT NULL,
    id_membresia INT NOT NULL,
    CONSTRAINT fk_pago_membresia
        FOREIGN KEY (id_membresia)
        REFERENCES membresia(id_membresia)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);