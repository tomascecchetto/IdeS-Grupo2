package ids.androidsong.object;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;


import ids.androidsong.excepcion.InvalidSongException;
import ids.androidsong.help.Enum;

/**
 * Created by ALAN on 01/10/2017.
 * Objeto Canción
 */

public class Cancion extends Item implements Serializable {

    public Cancion(int i) {
        super(i);
    }

    public Cancion() {
        super();
    }

    @SuppressWarnings("OverlyLongMethod")
    public void llenarSecciones(String letra){
        String nombre = "";
        StringBuilder contenido = new StringBuilder();
        Seccion seccion = new Seccion(nombre, contenido.toString());
        StringReader reader = new StringReader(letra);
        BufferedReader br = new BufferedReader(reader);
        String linea;
        boolean primeralinea = true;
        try {
            while ((linea = br.readLine()) != null){
                if (linea.length()>0){
                    Character caracter = linea.charAt(0);
                    switch (caracter){
                        case '.':
                            contenido.append(linea).append("\n");
                            break;
                        case '[':
                            nombre = linea.substring(1,linea.indexOf("]"));
                            if (primeralinea){
                                seccion = new Seccion(nombre);
                                primeralinea = false;
                            }
                            else {
                                seccion.setContenido(contenido.toString());
                                this.getSecciones().add(seccion);
                                seccion = new Seccion(nombre);
                                contenido = new StringBuilder();
                            }
                            break;
                        case ' ':
                            if (linea.equals(" ||")) {
                                seccion.setContenido(contenido.toString());
                                this.getSecciones().add(seccion);
                                seccion = new Seccion(nombre);
                                contenido = new StringBuilder();
                            }else {
                                contenido.append(linea).append("\n");
                            }
                            break;
                        default:
                            contenido.append(" ").append(linea).append("\n");
                            break;
                    }
                }
            }
            if (seccion.getNombre().equals(""))
                seccion.setNombre("V1");
            seccion.setContenido(contenido.toString());
            this.getSecciones().add(seccion);
        }
        catch (Exception e){
            contenido.append("Error al cargar.");
            seccion.setContenido(contenido.toString());
            this.getSecciones().add(seccion);
        }
    }

    public ArrayList<Item> get(){
        return super.get(Enum.itemTipo.CANCION.name());
    }

    public ArrayList<Item> get(String carpeta){
        return getByCarpeta(Enum.itemTipo.CANCION.name(),carpeta,true);
    }

    public ArrayList<Item> getBajas(){
        return getByCarpeta(Enum.itemTipo.CANCION.name(),FILTRO_CARPETA_TODAS,false);
    }

    public ArrayList<Item> buscar(String filtro){
        return getByTitulo(Enum.itemTipo.CANCION.name(),filtro);
    }

    public void alta() throws InvalidSongException {
        super.alta(Enum.itemTipo.CANCION.name());
    }

    public String getLetra(){
        StringBuilder letra = new StringBuilder();
        String nombreAnterior = "";
        for (Seccion sec : getSecciones()){
            if (!nombreAnterior.equals(sec.getNombre())) {
                letra.append("[").append(sec.getNombre()).append("]\n");
                nombreAnterior = sec.getNombre();
            }
            else
                letra.append(" ||\n");
            letra.append(sec.getContenido()).append("\n");
        }
        return letra.toString();
    }

    public String getAtributosTexto(){
        StringBuilder atributos = new StringBuilder();
        for (Atributo a : getAtributos()){
            atributos.append(a.getNombre()).append(", ");
            atributos.append(a.getValor()).append("\n");
        }
        return atributos.toString();
    }

    public void llenarAtributos(String atributos) {
        String[] atributo;
        StringReader reader = new StringReader(atributos);
        BufferedReader br = new BufferedReader(reader);
        String linea;
        try {
            while ((linea = br.readLine()) != null) {
                if (linea.length() > 0) {
                    atributo = linea.split(",");
                    atributo[1]= linea.substring(atributo[0].length()+1);
                    atributo[1]= atributo[1].trim();
                    this.getAtributos().add(new Atributo(atributo[0],atributo[1]));
                }
            }
        } catch (IOException e) {
            Log.e("Error",e.getMessage());
        }
    }

    @SuppressWarnings("unused")
    public Atributo getAtributo(String nombre){
        Atributo a = new Atributo(nombre,null);
        for (Atributo at: getAtributos()){
            if (at.getNombre().equals(nombre)){
                a = at;
            }
        }
        return a;
    }
}
