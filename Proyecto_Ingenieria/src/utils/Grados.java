package utils;

public class Grados {
	
	private int idGrado;
	private String nomGrado;
	private String duracion;
	
	public Grados(String nomGrado, String duracion) {
		this.nomGrado = nomGrado;
		this.duracion = duracion;
	}

	public Grados(int idGrado, String nomGrado, String duracion) {
		this.idGrado = idGrado;
		this.nomGrado = nomGrado;
		this.duracion = duracion;
	}

	public String getNomGrado() {
		return nomGrado;
	}

	public String getDuracion() {
		return duracion;
	}
	
	public int getIdGrado() {
		return idGrado;
	}
}