package ids.androidsong.object;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import ids.androidsong.help.aSDbHelper;
import static ids.androidsong.help.App.getContext;

/**
 * Created by ALAN on 01/10/2017.
 * bjeto para representar la tabla Secciones
 */

public class seccion {
    private int Id;
    private String nombre;
    private String contenido;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }


    public void alta(seccion s) {

        aSDbHelper dbHelper = new aSDbHelper(getContext());
        SQLiteDatabase bd = dbHelper.getWritableDatabase();

        ContentValues registro = new ContentValues();
        registro.put("nombre", s.getNombre());
        registro.put("contenido", s.getContenido());

        bd.insert("Secciones", null, registro);
        bd.close();
    }

    public void baja(seccion s) {

        aSDbHelper dbHelper = new aSDbHelper(getContext());
        SQLiteDatabase bd = dbHelper.getWritableDatabase();

        bd.delete("Secciones", "Id=" + s.getId(), null);
        bd.close();
    }

    public void modificacion(seccion s) {

        aSDbHelper dbHelper = new aSDbHelper(getContext());
        SQLiteDatabase bd = dbHelper.getWritableDatabase();

        ContentValues registro = new ContentValues();
        registro.put("nombre", s.getNombre());
        registro.put("contenido", s.getContenido());


        bd.update("Secciones", registro, "Id=" + s.getId(), null);
        bd.close();

    }




}
