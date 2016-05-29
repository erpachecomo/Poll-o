package mx.edu.ittepic.poll_o;

/**
 * Created by Neto on 29/05/2016.
 */
public class Encuesta_detalle {
    String compania;
    String nombre;
    int id;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getCompania() {
        return compania;
    }

    public String getNombre() {
        return nombre;
    }

    public void setCompania(String compania) {
        this.compania = compania;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
