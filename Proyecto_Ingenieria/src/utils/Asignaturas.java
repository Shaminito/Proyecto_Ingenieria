package utils;

public class Asignaturas {
    
	private String nomAsignatura;
	private int semestre;
	private int curso;
	
	//Columnas para BD
	public static final String COLUMNA_NOMBRE = "NomAsig";
	public static final String COLUMNA_SEMESTRE = "Semestre";
	public static final String COLUMNA_CURSO = "Curso";

    public Asignaturas(String nomAsignatura, int semestre, int curso) {
        this.nomAsignatura = nomAsignatura;
        this.semestre = semestre;
        this.curso = curso;
    }

	public String getNomAsignatura() {
		return nomAsignatura;
	}

	public int getSemestre() {
		return semestre;
	}

	public int getCurso() {
		return curso;
	}
}