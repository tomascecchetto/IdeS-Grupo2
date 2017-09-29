--
-- File generated with SQLiteStudio v3.1.1 on dom sep 10 17:32:18 2017
--
-- Text encoding used: System
--
PRAGMA foreign_keys = off;
BEGIN TRANSACTION;

-- Table: Atributos
DROP TABLE IF EXISTS Atributos;

CREATE TABLE Atributos (
    itemId       INTEGER  PRIMARY KEY
                          CONSTRAINT fk_atributosItem REFERENCES Items (Id) 
                          NOT NULL,
    presentacion TEXT,
    autor        TEXT,
    transporte   INTEGER  DEFAULT (0),
    interprete   TEXT,
    tono         CHAR (3) 
);


-- Table: AtributosUsuario
DROP TABLE IF EXISTS AtributosUsuario;

CREATE TABLE AtributosUsuario (
    Id     INTEGER PRIMARY KEY,
    itemId INTEGER CONSTRAINT fk_atributosUsuarioItem REFERENCES Items (Id),
    valor  TEXT
);


-- Table: Carpetas
DROP TABLE IF EXISTS Carpetas;

CREATE TABLE Carpetas (
    Id     INTEGER   PRIMARY KEY
                     NOT NULL,
    nombre CHAR (50) UNIQUE
                     NOT NULL
);

INSERT INTO Carpetas (
                         Id,
                         nombre
                     )
                     VALUES (
                         0,
                         'Principal'
                     );


-- Table: Colecciones
DROP TABLE IF EXISTS Colecciones;

CREATE TABLE Colecciones (
    Id     INTEGER   PRIMARY KEY,
    nombre CHAR (50) NOT NULL
);


-- Table: Items
DROP TABLE IF EXISTS Items;

CREATE TABLE Items (
    Id        INTEGER   PRIMARY KEY
                        NOT NULL,
    carpetaId           CONSTRAINT fk_itemsCarpetas REFERENCES Carpetas (Id) 
                        NOT NULL
                        DEFAULT (0),
    tipo      CHAR (10) NOT NULL,
    titulo    TEXT      NOT NULL
);


-- Table: ItemsColecciones
DROP TABLE IF EXISTS ItemsColecciones;

CREATE TABLE ItemsColecciones (
    coleccionId INTEGER CONSTRAINT fk_itemColeccionColeccion REFERENCES Colecciones (Id),
    orden       INTEGER NOT NULL,
    itemId      INTEGER CONSTRAINT fk_itemColeccionItem REFERENCES Items (Id) 
);


-- Table: Opciones
DROP TABLE IF EXISTS Opciones;

CREATE TABLE Opciones (
    nombre CHAR (20) PRIMARY KEY ON CONFLICT REPLACE,
    tipo   CHAR (3)  NOT NULL,
    valor  CHAR (20) 
);

INSERT INTO Opciones (
                         nombre,
                         tipo,
                         valor
                     )
                     VALUES (
                         'mostrarAcordes',
                         'bool',
                         'true'
                     );


-- Table: Secciones
DROP TABLE IF EXISTS Secciones;

CREATE TABLE Secciones (
    Id        INTEGER  PRIMARY KEY,
    itemId    INTEGER  CONSTRAINT fk_seccionesItem REFERENCES Items (Id),
    nombre    CHAR (3) DEFAULT V1
                       NOT NULL,
    contenido TEXT
);


COMMIT TRANSACTION;
PRAGMA foreign_keys = on;
