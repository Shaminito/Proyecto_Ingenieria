package utils;

public class Nombres {
    String nombre;

    public Nombres(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString() {
        return getNombre();
    }
}
