package utils;

public class Ano {
	
	private String Curso;
	public static final String COLUMNA_CURSO = "Curso";
	public Ano(String curso) {
		Curso = curso;
	}
	public String getCurso() {
		return Curso;
	}
	public void setCurso(String curso) {
		Curso = curso;
	}
	public static String getColumnaCurso() {
		return COLUMNA_CURSO;
	}


}