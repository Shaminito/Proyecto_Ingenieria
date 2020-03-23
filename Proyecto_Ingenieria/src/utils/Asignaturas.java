package utils;

public class Asignaturas {
    String asignatura;

    public Asignaturas(String asignatura) {
        this.asignatura = asignatura;
    }


    public String getAsignatura() {
        return asignatura;
    }

    @Override
    public String toString() {
        return getAsignatura();
    }
}
