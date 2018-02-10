package ids.androidsong.help;

import android.provider.BaseColumns;

/**
 * Created by ALAN on 21/08/2017.
 * Definición  de tablas y columnas en constantes para referencia en las clases que interactúan con el modelo de datos.
 */

public final class aSDbContract {
    private aSDbContract(){}

    public static final String CONSTRAINS_ON = "PRAGMA foreign_keys = on";

    public static class Atributos implements BaseColumns {
        public static final String TABLE_NAME = "Atributos";
        public static final String COLUMN_NAME_ID = "Id";
        public static final String COLUMN_NAME_ITEMID = "itemId";
        public static final String COLUMN_NAME_NOMBRE = "nombre";
        public static final String COLUMN_NAME_VALOR = "valor";
    }

    public static class Carpetas implements BaseColumns {
        public static final String TABLE_NAME = "Carpetas";
        public static final String COLUMN_NAME_ID = "Id";
        public static final String COLUMN_NAME_NOMBRE = "nombre";
    }

    public static class Colecciones implements BaseColumns {
        public static final String TABLE_NAME = "Colecciones";
        public static final String COLUMN_NAME_ID = "Id";
        public static final String COLUMN_NAME_NOMBRE = "nombre";
        public static final String COLUMN_NAME_FECHABAJA = "fechabaja";
        public static final String COLUMN_NAME_FECHAMODIFICACION = "fechamodificacion";
    }

    public static class Items implements BaseColumns {
        public static final String TABLE_NAME = "Items";
        public static final String COLUMN_NAME_ID = "Id";
        public static final String COLUMN_NAME_CARPETAID = "carpetaId";
        public static final String COLUMN_NAME_TIPO = "tipo";
        public static final String COLUMN_NAME_TITULO = "titulo";
        public static final String COLUMN_NAME_FECHABAJA = "fechabaja";
        public static final String COLUMN_NAME_FECHAMODIFICACION = "fechamodificacion";
    }

    public static class ItemsColecciones implements BaseColumns {
        public static final String TABLE_NAME = "ItemsColecciones";
        public static final String COLUMN_NAME_COLECCIONID = "coleccionId";
        public static final String COLUMN_NAME_ORDEN = "orden";
        public static final String COLUMN_NAME_ITEMID = "itemId";
    }

    public static class Opciones implements BaseColumns {
        public static final String TABLE_NAME = "Opciones";
        public static final String COLUMN_NAME_NOMBRE = "nombre";
        public static final String COLUMN_NAME_TIPO = "tipo";
        public static final String COLUMN_NAME_VALOR = "valor";

        public static final String OPT_NAME_MOSTRARACORDES = "mostrarAcordes";
        public static final String OPT_NAME_IMPORTPATH = "importPath";
        public static final String OPT_NAME_IMPORTOVERRIDE = "importOverride";
        public static final String OPT_NAME_SYNCPATH = "synctPath";
        public static final String OPT_NAME_SYNCOVERRIDE = "syncOverride";

        public static final String OPT_TYPE_BOOL = "bool";
        public static final String OPT_TYPE_TEXT = "text";
    }

    public static class Secciones implements BaseColumns {
        public static final String TABLE_NAME = "Secciones";
        public static final String COLUMN_NAME_ID = "Id";
        public static final String COLUMN_NAME_ITEMID = "itemId";
        public static final String COLUMN_NAME_NOMBRE = "nombre";
        public static final String COLUMN_NAME_CONTENIDO = "contenido";
    }

    public static class DriveStatus implements BaseColumns {
        public static final String TABLE_NAME = "DriveStatus";
        public static final String COLUMN_NAME_ITEMID = "itemId";
        public static final String COLUMN_NAME_TITULO = "titulo";
        public static final String COLUMN_NAME_CARPETA = "carpeta";
        public static final String COLUMN_NAME_LOCALDT = "localDT";
        public static final String COLUMN_NAME_DRIVEDT = "driveDT";
        public static final String COLUMN_NAME_PROCESADO = "procesado";
    }
}
