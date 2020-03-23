package utils;

public class Profesor {
    String nombre, correo, pass;

    public Profesor(String nombre, String correo, String pass) {
        this.nombre = nombre;
        this.correo = correo;
        this.pass = pass;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public String getPass() {
        return pass;
    }
}
