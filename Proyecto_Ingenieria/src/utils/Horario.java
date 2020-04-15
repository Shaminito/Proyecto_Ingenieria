package utils;

public class Horario {
	
	private int hora;
	private String clase;
	
	public static final String COLUMNA_HORA1 = "Hora1";
	public static final String COLUMNA_CLASE1 = "Clase1";
	public static final String COLUMNA_HORA2 = "Hora2";
	public static final String COLUMNA_CLASE2 = "Clase2";
	
	public Horario(int hora, String clase) {
		this.hora = hora;
		this.clase = clase;
	}
	
	public int getHora() {
		return hora;
	}
	
	public String getClase() {
		return clase;
	}
}