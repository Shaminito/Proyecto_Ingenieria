package utils;

public class Horario {
	
	private int idAsignatura;
	private int hora1;
	private int hora2;
	private String clase1;
	private String clase2;
	
	public Horario(int hora1, int hora2, String clase1, String clase2) {
		this.hora1 = hora1;
		this.hora2 = hora2;
		this.clase1 = clase1;
		this.clase2 = clase2;
	}

	public Horario(int idAsignatura, int hora1, int hora2, String clase1, String clase2) {
		super();
		this.idAsignatura = idAsignatura;
		this.hora1 = hora1;
		this.hora2 = hora2;
		this.clase1 = clase1;
		this.clase2 = clase2;
	}

	public int getHora1() {
		return hora1;
	}

	public int getHora2() {
		return hora2;
	}

	public String getClase1() {
		return clase1;
	}

	public String getClase2() {
		return clase2;
	}
	
	public int getIdAsignatura() {
		return idAsignatura;
	}
}