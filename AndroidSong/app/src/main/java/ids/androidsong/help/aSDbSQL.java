package ids.androidsong.help;

/**
 * Created by ALAN on 10/09/2017.
 */

public final class aSDbSQL {
    private aSDbSQL(){};

    public static final String SQL_CREATE_ENTRIES =
            "DROP TABLE IF EXISTS Atributos; " +
            "CREATE TABLE Atributos (itemId INTEGER PRIMARY KEY CONSTRAINT fk_atributosItem REFERENCES Items (Id) NOT NULL, presentacion TEXT, autor TEXT, transporte INTEGER DEFAULT (0), interprete TEXT, tono CHAR (3));" +

            "DROP TABLE IF EXISTS AtributosUsuario; " +
            "CREATE TABLE AtributosUsuario (Id INTEGER PRIMARY KEY, itemId INTEGER CONSTRAINT fk_atributosUsuarioItem REFERENCES Items (Id), valor TEXT); " +

            "DROP TABLE IF EXISTS Carpetas; " +
            "CREATE TABLE Carpetas (Id INTEGER PRIMARY KEY NOT NULL, nombre CHAR (50) UNIQUE NOT NULL); " +
            "INSERT INTO Carpetas (Id, nombre) VALUES (0, 'Principal'); " +

            "DROP TABLE IF EXISTS Colecciones; " +
            "CREATE TABLE Colecciones (Id INTEGER PRIMARY KEY, nombre CHAR (50) NOT NULL); " +

            "DROP TABLE IF EXISTS Items; " +
            "CREATE TABLE Items (Id INTEGER PRIMARY KEY NOT NULL, carpetaId CONSTRAINT fk_itemsCarpetas REFERENCES Carpetas (Id) NOT NULL DEFAULT (0), tipo CHAR (10) NOT NULL, titulo TEXT NOT NULL); " +

            "DROP TABLE IF EXISTS ItemsColecciones; " +
            "CREATE TABLE ItemsColecciones (coleccionId INTEGER CONSTRAINT fk_itemColeccionColeccion REFERENCES Colecciones (Id), orden INTEGER NOT NULL, itemId INTEGER CONSTRAINT fk_itemColeccionItem REFERENCES Items (Id)); " +

            "DROP TABLE IF EXISTS Opciones; " +
            "CREATE TABLE Opciones (nombre CHAR (20) PRIMARY KEY ON CONFLICT REPLACE, tipo CHAR (3) NOT NULL, valor CHAR (20)); " +
            "INSERT INTO Opciones (nombre, tipo, valor) VALUES ('mostrarAcordes', 'bool', 'true'); " +

            "DROP TABLE IF EXISTS Secciones; " +
            "CREATE TABLE Secciones (Id INTEGER PRIMARY KEY, itemId INTEGER CONSTRAINT fk_seccionesItem REFERENCES Items (Id), nombre CHAR (3) DEFAULT V1 NOT NULL, contenido TEXT); ";
}
