package utils;

public class Profesores {
	
     private String nombre, correo;
     
     //Columnas para BD
     public static final String COLUMNA_NOMBRE = "NombreProf";
     public static final String COLUMNA_CORREO = "CorreoProf";

    public Profesores(String nombre, String correo) {
        this.nombre = nombre;
        this.correo = correo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCorreo() {
        return correo;
    }
}
