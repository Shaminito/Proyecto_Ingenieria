package utils;

public class Profesores {

	private int idProf;
	private String nombre;
	private String correo;

	// Columnas para BD
	public static final String COLUMNA_NOMBRE = "NombreProf";
	public static final String COLUMNA_CORREO = "CorreoProf";
	public static final String COLUMNA_ID = "IdProf";

	public Profesores(String nombre, String correo) {
		this.nombre = nombre;
		this.correo = correo;
	}

	public Profesores(int idProf, String nombre, String correo) {
		super();
		this.idProf = idProf;
		this.nombre = nombre;
		this.correo = correo;
	}

	public String getNombre() {
		return nombre;
	}

	public String getCorreo() {
		return correo;
	}
	
	public int getIdProf() {
		return idProf;
	}
}
