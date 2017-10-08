package ids.androidsong.help;

import android.provider.BaseColumns;

/**
 * Created by ALAN on 21/08/2017.
 */

public final class aSDbContract {
    private aSDbContract(){};

    public static final String CONSTRAINS_OFF = "PRAGMA foreign_keys = off";
    public static final String CONSTRAINS_ON = "PRAGMA foreign_keys = on";

    public static class Atributos implements BaseColumns {
        public static final String TABLE_NAME = "Atributos";
        public static final String COLUMN_NAME_ID = "Id";
        public static final String COLUMN_NAME_ITEMID = "itemId";
        public static final String COLUMN_NAME_NOMBRE = "valor";
        public static final String COLUMN_NAME_VALOR = "valor";

        public static final String TABLE_CREATE = "CREATE TABLE Atributos (Id INTEGER PRIMARY KEY, itemId INTEGER CONSTRAINT fk_atributosItem REFERENCES Items (Id) NOT NULL, nombre TEXT NOT NULL valor TEXT);";
        public static final String TABLE_DROP = "DROP TABLE IF EXISTS AtributosUsuario;";
    }

    public static class Carpetas implements BaseColumns {
        public static final String TABLE_NAME = "Carpetas";
        public static final String COLUMN_NAME_ID = "Id";
        public static final String COLUMN_NAME_NOMBRE = "nombre";

        public static final String TABLE_CREATE = "DROP TABLE IF EXISTS Carpetas;";
        public static final String TABLE_DROP = "CREATE TABLE Carpetas (Id INTEGER PRIMARY KEY NOT NULL, nombre CHAR (50) UNIQUE NOT NULL);";
        public static final String TABLE_INIT = "INSERT INTO Carpetas (Id, nombre) VALUES (0, 'Principal');";
    }

    public static class Colecciones implements BaseColumns {
        public static final String TABLE_NAME = "Colecciones";
        public static final String COLUMN_NAME_ID = "Id";
        public static final String COLUMN_NAME_NOMBRE = "nombre";

        public static final String TABLE_CREATE = "CREATE TABLE Colecciones (Id INTEGER PRIMARY KEY, nombre CHAR (50) NOT NULL);";
        public static final String TABLE_DROP = "DROP TABLE IF EXISTS Colecciones;";
    }

    public static class Items implements BaseColumns {
        public static final String TABLE_NAME = "Items";
        public static final String COLUMN_NAME_ID = "Id";
        public static final String COLUMN_NAME_CARPETAID = "carpetaId";
        public static final String COLUMN_NAME_TIPO = "tipo";
        public static final String COLUMN_NAME_TITULO = "titulo";

        public static final String TABLE_CREATE = "CREATE TABLE Items (Id INTEGER PRIMARY KEY NOT NULL, carpetaId CONSTRAINT fk_itemsCarpetas REFERENCES Carpetas (Id) NOT NULL DEFAULT (0), tipo CHAR (10) NOT NULL, titulo TEXT NOT NULL);";
        public static final String TABLE_DROP = "DROP TABLE IF EXISTS Items;";
    }

    public static class ItemsColecciones implements BaseColumns {
        public static final String TABLE_NAME = "ItemsColecciones";
        public static final String COLUMN_NAME_COLECCIONID = "coleccionId";
        public static final String COLUMN_NAME_ORDEN = "orden";
        public static final String COLUMN_NAME_ITEMID = "itemId";

        public static final String TABLE_CREATE = "CREATE TABLE ItemsColecciones (coleccionId INTEGER CONSTRAINT fk_itemColeccionColeccion REFERENCES Colecciones (Id), orden INTEGER NOT NULL, itemId INTEGER CONSTRAINT fk_itemColeccionItem REFERENCES Items (Id));";
        public static final String TABLE_DROP = "DROP TABLE IF EXISTS ItemsColecciones;";
    }

    public static class Opciones implements BaseColumns {
        public static final String TABLE_NAME = "Opciones";
        public static final String COLUMN_NAME_NOMBRE = "nombre";
        public static final String COLUMN_NAME_TIPO = "tipo";
        public static final String COLUMN_NAME_VALOR = "valor";

        public static final String TABLE_CREATE = "CREATE TABLE Opciones (nombre CHAR (20) PRIMARY KEY ON CONFLICT REPLACE, tipo CHAR (3) NOT NULL, valor CHAR (20));";
        public static final String TABLE_DROP = "DROP TABLE IF EXISTS Opciones;";
        public static final String TABLE_INIT = "INSERT INTO Opciones (nombre, tipo, valor) VALUES ('mostrarAcordes', 'bool', 'true');";
    }

    public static class Secciones implements BaseColumns {
        public static final String TABLE_NAME = "Secciones";
        public static final String COLUMN_NAME_ID = "Id";
        public static final String COLUMN_NAME_ITEMID = "itemId";
        public static final String COLUMN_NAME_NOMBRE = "nombre";
        public static final String COLUMN_NAME_CONTENIDO = "contenido";

        public static final String TABLE_CREATE = "CREATE TABLE Secciones (Id INTEGER PRIMARY KEY, itemId INTEGER CONSTRAINT fk_seccionesItem REFERENCES Items (Id), nombre CHAR (3) DEFAULT V1 NOT NULL, contenido TEXT);";
        public static final String TABLE_DROP = "DROP TABLE IF EXISTS Secciones;";
    }
}
