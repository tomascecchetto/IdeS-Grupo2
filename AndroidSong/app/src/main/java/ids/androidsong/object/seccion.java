package ids.androidsong.object;

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
}
