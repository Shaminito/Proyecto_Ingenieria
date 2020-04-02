package utils;

public class Asig_Grado {
	
	private int idAsig;
	private int idGrado;
	private int cod_asig;
	
	public Asig_Grado(int idAsig, int idGrado, int cod_asig) {
		this.idAsig = idAsig;
		this.idGrado = idGrado;
		this.cod_asig = cod_asig;
	}
	
	public int getIdAsig() {
		return idAsig;
	}
	
	public int getIdGrado() {
		return idGrado;
	}
	
	public int getCod_asig() {
		return cod_asig;
	}
}