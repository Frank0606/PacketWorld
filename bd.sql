
--   BASE DE DATOS PACKETWORLD

DROP DATABASE IF EXISTS packetworld;
CREATE DATABASE packetworld;
USE packetworld;

--   ROLES
CREATE TABLE Rol (
    idRol INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE EstadosEnvio (
    idEstadosEnvio INT NOT NULL PRIMARY KEY,
    estadoEnvio VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE Estados (
    idEstado INT NOT NULL PRIMARY KEY,
    estadoMexico VARCHAR(100) NOT NULL UNIQUE
);

INSERT INTO EstadosEnvio (idEstadosEnvio, estadoEnvio) VALUES
(1, 'recibido en sucursal'),
(2, 'procesado'),
(3, 'en transito'),
(4, 'detenido'),
(5, 'entregado'),
(6, 'cancelado');

INSERT INTO Estados (idEstado, estadoMexico) VALUES
(1, 'Aguascalientes'),
(2, 'Baja California'),
(3, 'Baja California Sur'),
(4, 'Campeche'),
(5, 'Coahuila'),
(6, 'Colima'),
(7, 'Chiapas'),
(8, 'Chihuahua'),
(9, 'Ciudad de México'),
(10, 'Durango'),
(11, 'Guanajuato'),
(12, 'Guerrero'),
(13, 'Hidalgo'),
(14, 'Jalisco'),
(15, 'México'),
(16, 'Michoacán'),
(17, 'Morelos'),
(18, 'Nayarit'),
(19, 'Nuevo León'),
(20, 'Oaxaca'),
(21, 'Puebla'),
(22, 'Querétaro'),
(23, 'Quintana Roo'),
(24, 'San Luis Potosí'),
(25, 'Sinaloa'),
(26, 'Sonora'),
(27, 'Tabasco'),
(28, 'Tamaulipas'),
(29, 'Tlaxcala'),
(30, 'Veracruz'),
(31, 'Yucatán'),
(32, 'Zacatecas');

INSERT INTO Rol(nombre) VALUES
('Administrador'),
('Ejecutivo de tienda'),
('Conductor');

CREATE TABLE Sucursal (
    idSucursal INT AUTO_INCREMENT PRIMARY KEY,
    codigo VARCHAR(20) NOT NULL UNIQUE,
    nombreCorto VARCHAR(100) NOT NULL,
    estatus ENUM('Activa','Inactiva') DEFAULT 'Activa',
    calle VARCHAR(100) NOT NULL,
    numero VARCHAR(20) NOT NULL,
    colonia VARCHAR(100) NOT NULL,
    codigoPostal VARCHAR(10) NOT NULL,
    ciudad VARCHAR(100) NOT NULL,
    idEstado INT NOT NULL,
    CONSTRAINT fk_estado FOREIGN KEY (idEstado) REFERENCES Estados(idEstado)
);

--   TIPOS DE UNIDAD
CREATE TABLE TipoUnidad (
    idTipoUnidad INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE
);

INSERT INTO TipoUnidad(nombre) VALUES
('Gasolina'),
('Diesel'),
('Eléctrica'),
('Híbrida');

--   UNIDADES
CREATE TABLE Unidad (
    idUnidad INT AUTO_INCREMENT PRIMARY KEY,
    marca VARCHAR(100) NOT NULL,
    modelo VARCHAR(100) NOT NULL,
    anio INT NOT NULL,
    vin VARCHAR(50) NOT NULL UNIQUE,
    noIdentificacion VARCHAR(20) NOT NULL UNIQUE,
    numeroPersonal VARCHAR(20),
    idTipoUnidad INT NOT NULL,
    numeroInterno VARCHAR(20) NOT NULL UNIQUE,
    estatus ENUM('Activa','Baja') DEFAULT 'Activa',
    FOREIGN KEY (idTipoUnidad) REFERENCES TipoUnidad(idTipoUnidad)
);

--   COLABORADORES
CREATE TABLE Colaborador (
    idColaborador INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellidoPaterno VARCHAR(100) NOT NULL,
    apellidoMaterno VARCHAR(100),
    curp VARCHAR(20) NOT NULL UNIQUE,
    correo VARCHAR(150) NOT NULL UNIQUE,
    numeroPersonal VARCHAR(20) NOT NULL UNIQUE,
    contrasena VARCHAR(255) NOT NULL,
    idRol INT NOT NULL,
    idSucursal INT NOT NULL,
    idUnidad INT,
    fotografia BLOB,
    numeroLicencia VARCHAR(50),
    fechaAlta DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (idRol) REFERENCES Rol(idRol),
    FOREIGN KEY (idSucursal) REFERENCES Sucursal(idSucursal),
    FOREIGN KEY (idUnidad) REFERENCES Unidad(idUnidad)
);

-- CLIENTES
CREATE TABLE Cliente (
    idCliente INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellidoPaterno VARCHAR(100) NOT NULL,
    apellidoMaterno VARCHAR(100),
    calle VARCHAR(100) NOT NULL,
    numero VARCHAR(20) NOT NULL,
    colonia VARCHAR(100) NOT NULL,
    codigoPostal VARCHAR(10) NOT NULL,
    ciudad VARCHAR(100) NOT NULL,
    idEstado INT NOT NULL,
    telefono VARCHAR(20),
    correo VARCHAR(150),
    CONSTRAINT fk_cliente_estado
        FOREIGN KEY (idEstado)
        REFERENCES Estados(idEstado)
);

-- BAJAS DE UNIDADES
CREATE TABLE UnidadBaja (
    idBaja INT AUTO_INCREMENT PRIMARY KEY,
    idUnidad INT NOT NULL,
    motivo VARCHAR(255) NOT NULL,
    fechaBaja DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (idUnidad) REFERENCES Unidad(idUnidad)
);

--   RELACIÓN CONDUCTOR - UNIDAD
CREATE TABLE ConductorUnidad (
    idConductor INT NOT NULL,
    idUnidad INT NOT NULL UNIQUE,
    numeroPersonal VARCHAR(20) NOT NULL,
    PRIMARY KEY (idConductor),
    FOREIGN KEY (idConductor) REFERENCES Colaborador(idColaborador),
    FOREIGN KEY (numeroPersonal) REFERENCES Colaborador(numeroPersonal),
    FOREIGN KEY (idUnidad) REFERENCES Unidad(idUnidad)
);

CREATE TABLE Envio (
    idEnvio INT AUTO_INCREMENT PRIMARY KEY,
    idCliente INT NOT NULL,
    idColaborador INT,
    nombreDestinatario VARCHAR(100) NOT NULL,
    idSucursalOrigen INT NOT NULL,
    calleDestino VARCHAR(100) NOT NULL,
    numeroDestino VARCHAR(20) NOT NULL,
    coloniaDestino VARCHAR(100) NOT NULL,
    cpDestino VARCHAR(10) NOT NULL,
    ciudadDestino VARCHAR(100) NOT NULL,
    numeroGuia VARCHAR(50) NOT NULL UNIQUE,

    distanciaKm DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    costoKm DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    costoPaquetes DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    costoTotal DECIMAL(10,2) NOT NULL DEFAULT 0.00,

    idEstadosEnvio INT NOT NULL,
    idEstadoDestino INT NOT NULL,
    fechaCreacion DATETIME DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (idCliente) REFERENCES Cliente(idCliente),
    FOREIGN KEY (idSucursalOrigen) REFERENCES Sucursal(idSucursal),
    FOREIGN KEY (idColaborador) REFERENCES Colaborador(idColaborador),
    FOREIGN KEY (idEstadosEnvio) REFERENCES EstadosEnvio(idEstadosEnvio),
    FOREIGN KEY (idEstadoDestino) REFERENCES Estados(idEstado)
);

--   HISTORIAL DE ESTADOS DEL ENVÍO
CREATE TABLE EnvioHistorial (
    idHistorial INT AUTO_INCREMENT PRIMARY KEY,
    idEnvio INT NOT NULL,
    idEstadosEnvio INT NOT NULL,
    comentario VARCHAR(255),
    fechaCambio DATETIME DEFAULT CURRENT_TIMESTAMP,
    idColaborador INT NOT NULL,
    FOREIGN KEY (idEnvio) REFERENCES Envio(idEnvio),
    FOREIGN KEY (idColaborador) REFERENCES Colaborador(idColaborador),
    FOREIGN KEY (idEstadosEnvio) REFERENCES EstadosEnvio(idEstadosEnvio)
);

--   PAQUETES DE UN ENVÍO
CREATE TABLE Paquete (
    idPaquete INT AUTO_INCREMENT PRIMARY KEY,
    idEnvio INT NOT NULL,
    numeroGuia VARCHAR(50) NOT NULL, -- nuevo campo
    descripcion VARCHAR(255) NOT NULL,
    peso DECIMAL(10,2) NOT NULL,
    alto DECIMAL(10,2) NOT NULL,
    ancho DECIMAL(10,2) NOT NULL,
    profundidad DECIMAL(10,2) NOT NULL,
    costo DECIMAL(10,2) NOT NULL DEFAULT 0,
    FOREIGN KEY (idEnvio) REFERENCES Envio(idEnvio)
);

--   ASIGNACIÓN ENVÍO - CONDUCTOR (UNO A UNO)
CREATE TABLE EnvioConductor (
    idEnvio INT NOT NULL UNIQUE,
    idConductor INT NOT NULL,
    fechaAsignacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (idEnvio),
    FOREIGN KEY (idEnvio) REFERENCES Envio(idEnvio),
    FOREIGN KEY (idConductor) REFERENCES Colaborador(idColaborador)
);

-- DATOS DE PRUEBA PACKETWORLD


-- SUCURSALES
INSERT INTO Sucursal (codigo, nombreCorto, estatus, calle, numero, colonia, codigoPostal, ciudad, idEstado)
VALUES
('SUC001','Sucursal Centro','Activa','Av. Juárez','123','Centro','94000','Córdoba',30),
('SUC002','Sucursal Norte','Activa','Calle Hidalgo','45','Industrial','94100','Orizaba',30);

-- UNIDADES (ahora con anio, vin, noIdentificacion y numeroPersonal)
INSERT INTO Unidad (marca, modelo, anio, vin, noIdentificacion, numeroPersonal, idTipoUnidad, numeroInterno, estatus)
VALUES
('Nissan','NP300',2020,'VIN1234567890','2020VIN1','EMP003',1,'UNI001','Activa'),
('Toyota','Hiace',2019,'VIN0987654321','2019VIN0',NULL,2,'UNI002','Baja');

-- COLABORADORES (ahora con idUnidad opcional)
INSERT INTO Colaborador (nombre, apellidoPaterno, apellidoMaterno, curp, correo, numeroPersonal, contrasena, idRol, idSucursal, idUnidad, numeroLicencia)
VALUES
('Juan','Pérez','López','PEPJ800101HDFRRN01','juan.perez@packetworld.com','EMP001','1234segura',1,1,1,'LIC12345'),
('María','García','Hernández','GAHM850505MVZRRR02','maria.garcia@packetworld.com','EMP002','abcdsegura',2,1,NULL,NULL),
('Carlos','Ramírez','Santos','RASC900707HDFRRR03','carlos.ramirez@packetworld.com','EMP003','xyzsegura',3,2,1,'LIC67890');

INSERT INTO Cliente 
(nombre, apellidoPaterno, apellidoMaterno, calle, numero, colonia, codigoPostal, ciudad, idEstado, telefono, correo)
VALUES
('Ana','Martínez','Ruiz','Av. Reforma','321','Centro','91700','Veracruz',30,'2711234567','ana.martinez@mail.com'),
('Luis','Torres','Gómez','Calle Morelos','56','San José','94020','Tlaltetela',30,'2717654321','luis.torres@mail.com');

-- BAJA DE UNIDAD
INSERT INTO UnidadBaja (idUnidad, motivo)
VALUES
(2,'Falla mecánica grave');

-- RELACIÓN CONDUCTOR - UNIDAD
INSERT INTO ConductorUnidad (idConductor, idUnidad, numeroPersonal)
VALUES
(3,1,'EMP003');

-- ENVÍOS (ahora con idEstadosEnvio en lugar de estatus)
INSERT INTO Envio (
    idCliente, idColaborador, nombreDestinatario,
    idSucursalOrigen, calleDestino, numeroDestino, coloniaDestino, cpDestino, ciudadDestino,
    numeroGuia, distanciaKm, costoKm, costoPaquetes, costoTotal, idEstadosEnvio, idEstadoDestino
) VALUES
(1, 3, 'Pedro', 1,
 'Calle Juárez', '12', 'Centro', '94030', 'Córdoba',
 'GUIA001', 16.78, 67.12, 50.00, 117.12, 3, 30),
(2, 3, 'Sofía', 2,
 'Av. Hidalgo', '34', 'Industrial', '94120', 'Orizaba',
 'GUIA002', 88.41, 353.64, 0.00, 353.64, 3, 30);

-- HISTORIAL DE ENVÍO (ahora con idEstadosEnvio)
INSERT INTO EnvioHistorial (idEnvio, idEstadosEnvio, comentario, idColaborador, fechaCambio)
VALUES
(1, 3, 'Paquete en camino', 1, DATE_SUB(NOW(), INTERVAL 8 HOUR)),
(1, 5, 'Recibido por el cliente', 2, NOW()),
(2, 3, 'Salida de sucursal', 3, DATE_SUB(NOW(), INTERVAL 5 HOUR));

-- Añadir un atributo de fecha y hora para la actulizacion de los estatus

-- PAQUETES (ahora con numeroGuia)
INSERT INTO Paquete (idEnvio, numeroGuia, descripcion, peso, alto, ancho, profundidad)
VALUES
(1,'GUIA001','Caja mediana con ropa',5.5,40,30,20),
(1,'GUIA001','Sobre con documentos',0.2,30,20,1),
(2,'GUIA002','Caja grande con electrónicos',12.0,60,50,40);

-- ASIGNACIÓN ENVÍO - CONDUCTOR
INSERT INTO EnvioConductor (idEnvio, idConductor)
VALUES
(1,3),
(2,3);
