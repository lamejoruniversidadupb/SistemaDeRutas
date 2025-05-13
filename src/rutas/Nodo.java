package rutas;

import java.util.HashSet;
import java.util.Set;

public class Nodo {
    private final String nombre;
    private final int x;
    private final int y;


    private static final Set<String> nodosConEscaleras = new HashSet<>();

    static {
    
        nodosConEscaleras.add("PORTERIA");
        nodosConEscaleras.add("K");
        nodosConEscaleras.add("D");
        nodosConEscaleras.add("J");
        nodosConEscaleras.add("E");
        nodosConEscaleras.add("I");
    }

    public Nodo(String nombre, int x, int y) {
        this.nombre = nombre;
        this.x = x;
        this.y = y;
    }

    public String getNombre() {
        return nombre;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

 
    public boolean tieneEscalera() {
        return nodosConEscaleras.contains(nombre);
    }
}
