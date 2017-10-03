package ids.androidsong.object;

import java.util.ArrayList;

/**
 * Created by ALAN on 01/10/2017.
 * Objeto para representar la tabla items
 */

public class item {
    private int Id;
    private String carpeta;
    private String tipo;
    private String title;
    private ArrayList<seccion> secciones = new ArrayList<>();
    private ArrayList<atributo> atributos = new ArrayList<>();

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getCarpeta() {
        return carpeta;
    }

    public void setCarpeta(String carpeta) {
        this.carpeta = carpeta;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<seccion> getSecciones() {
        return secciones;
    }

    public void setSecciones(ArrayList<seccion> secciones) {
        this.secciones = secciones;
    }

    public ArrayList<atributo> getAtributos() {
        return atributos;
    }

    public void setAtributos(ArrayList<atributo> atributos) {
        this.atributos = atributos;
    }
}
