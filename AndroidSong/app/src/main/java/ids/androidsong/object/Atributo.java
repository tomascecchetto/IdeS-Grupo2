package ids.androidsong.object;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;

import ids.androidsong.excepcion.InvalidSongException;
import ids.androidsong.help.App;
import ids.androidsong.help.AsdbContract;

/**
 * Created by ALAN on 01/10/2017.
 * Objeto para representar las tablas Atributos y AtributosUsuario
 */

@SuppressWarnings("ALL")
public class Atributo {
    private int Id;
    private String nombre;
    private String valor;

    private static final String NAME_SONG = "nombre";
    private static final String AUTHOR = "autor";
    private static final String PRESENTATION = "presentacion";
    private static final String TONE = "tono";
    private static final String TRANSPORTATION = "transporte";

    public Atributo(int i, String n, String v){
        this.Id = i;
        this.nombre = n;
        this.valor = v;
    }

    public Atributo(String n, String v){
        this.nombre = n;
        this.valor = v;
    }

    public Atributo(){}

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public void alta(Item i) {
        ContentValues registro = new ContentValues();

        validarAtributo(getNombre(),getValor());

        registro.put(AsdbContract.Atributos.COLUMN_NAME_ITEMID, i.getId());
        registro.put(AsdbContract.Atributos.COLUMN_NAME_NOMBRE, getNombre());
        registro.put(AsdbContract.Atributos.COLUMN_NAME_VALOR, getValor());

        App.GetOpenDB().insert(AsdbContract.Atributos.TABLE_NAME, null, registro);
        //deberia cerrarse la conexion en el metodo
        App.CloseDB();

    }

    public void baja(Item i) {
        App.GetOpenDB().delete(AsdbContract.Atributos.TABLE_NAME, AsdbContract.Atributos.COLUMN_NAME_ITEMID + "=" + i.getId(), null);
        App.CloseDB();
        //helper.currentDB.close();
    }

    public void modificacion(Atributo a) {
        ContentValues registro = new ContentValues();
        registro.put(AsdbContract.Atributos.COLUMN_NAME_NOMBRE, a.getNombre());
        registro.put(AsdbContract.Atributos.COLUMN_NAME_VALOR, a.getValor());

        App.GetOpenDB().update(AsdbContract.Atributos.TABLE_NAME, registro, AsdbContract.Atributos.COLUMN_NAME_ID + "=" + a.getId(), null);
        App.CloseDB();
        //helper.currentDB.close();
    }

    public ArrayList<Atributo> get(Item item){
        ArrayList<Atributo> atributos = new ArrayList<>();
        String sortOrder = AsdbContract.Atributos.COLUMN_NAME_NOMBRE + " ASC";
        String filter = AsdbContract.Atributos.COLUMN_NAME_ITEMID + "=" + item.getId();
        Cursor c = App.GetOpenDB().query(AsdbContract.Atributos.TABLE_NAME, null, filter, null, null, null, sortOrder);
        if (c.moveToFirst()) {
            do {
                atributos.add(new Atributo(
                        c.getInt(c.getColumnIndex(AsdbContract.Atributos.COLUMN_NAME_ID)),
                        c.getString(c.getColumnIndex(AsdbContract.Atributos.COLUMN_NAME_NOMBRE)),
                        c.getString(c.getColumnIndex(AsdbContract.Atributos.COLUMN_NAME_VALOR))
                ));
            } while (c.moveToNext());
        }
        c.close();
        App.CloseDB();
        return atributos;
    }

    public void validarAtributo(String code,String value) throws InvalidSongException {
        switch (code){
            case AUTHOR:
                if (value.length()>50) throw new InvalidSongException(InvalidSongException.Message.INVALID_AUTHOR.getCode(),InvalidSongException.Message.INVALID_AUTHOR.getDescription());
                break;
            case PRESENTATION:
                if (value.length()>50) throw new InvalidSongException(InvalidSongException.Message.INVALID_PRESENTATION.getCode(),InvalidSongException.Message.INVALID_PRESENTATION.getDescription());
                break;
            case TONE:
                if (value.length()>3 || !("A,Bb,B,C,C#,D,Eb,E,F,F#,G,Ab,Am,Bbm,Bm,Cm,C#m,Dm,Ebm,Em,Fm,F#m,Gm,Abm").contains(value)) throw new InvalidSongException(InvalidSongException.Message.INVALID_TONE.getCode(),InvalidSongException.Message.INVALID_TONE.getDescription());
                break;
            case TRANSPORTATION:
                if (!isValidTransportation(value))  throw new InvalidSongException(InvalidSongException.Message.INVALID_TRANSPORTATION.getCode(),InvalidSongException.Message.INVALID_TRANSPORTATION.getDescription());
                break;
        }
    }

    public boolean isValidTransportation(String value) throws InvalidSongException {
        int number;
        try {
            number = Integer.parseInt(value);
            return (number >=1 && number <=12);
        }catch (Exception e ){
            throw new InvalidSongException(InvalidSongException.Message.INVALID_TRANSPORTATION.getCode(),InvalidSongException.Message.INVALID_TRANSPORTATION.getDescription());
        }
    }

}
