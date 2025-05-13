package rutas;

import java.util.HashSet;
import java.util.Set;

public class Nodo {
    private final String nombre;
    private final int x;
    private final int y;

    // Agregar lista estática o conjunto que contiene los nodos con escaleras
    private static final Set<String> nodosConEscaleras = new HashSet<>();

    static {
        // Aquí agregar los nombres de nodos que tienen escaleras
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

    // Nuevo método para saber si el nodo tiene escaleras
    public boolean tieneEscalera() {
        return nodosConEscaleras.contains(nombre);
    }
}
