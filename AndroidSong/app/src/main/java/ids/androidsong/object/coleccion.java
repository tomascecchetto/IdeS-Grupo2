package ids.androidsong.object;

import java.util.ArrayList;

/**
 * Created by ALAN on 01/10/2017.
 * Objeto para representar la tabla Colecciones e ItemsColecciones.
 */

public class coleccion {
    private int Id;
    private String nombre;
    private ArrayList<item> items = new ArrayList<>();

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

    public ArrayList<item> getItems() {
        return items;
    }

    public void setItems(ArrayList<item> items) {
        this.items = items;
    }
}
