package utils;

public class Asignaturas {
    
	private int idAsignaturas;
	private String nomAsignatura;
	private int semestre;
	private int curso;
	private Horario[] horario = new Horario[2];
	
	//Columnas para BD
	public static final String COLUMNA_IDASIG = "IdAsig";
	public static final String COLUMNA_NOMBRE = "NomAsig";
	public static final String COLUMNA_SEMESTRE = "Semestre";
	public static final String COLUMNA_CURSO = "Curso";

    public Asignaturas(String nomAsignatura, int semestre, int curso) {
        this.nomAsignatura = nomAsignatura;
        this.semestre = semestre;
        this.curso = curso;
    }

	public Asignaturas(int idAsignaturas, String nomAsignatura, int semestre, int curso) {
		super();
		this.idAsignaturas = idAsignaturas;
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
	
	public int getIdAsignaturas() {
		return idAsignaturas;
	}
	
	public void addHorario(int hora1, String clase1, int hora2, String clase2) {
		horario[0] = new Horario(hora1, clase1);
		horario[1] = new Horario(hora2, clase2);
	}
	
	public Horario[] getHorario() {
		return horario;
	}
	
	public Horario getHorario(int zona) {
		return horario[zona];
	}
}