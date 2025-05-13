package rutas;

public class Arista {
    private final String origen;
    private final String destino;
    private final int distancia;
    private final boolean tieneEscaleras; 
    private final boolean esAccesible; // 

    public Arista(String origen, String destino, int distancia) {
        this(origen, destino, distancia, false, true);
    }

    public Arista(String origen, String destino, int distancia, boolean tieneEscaleras) {
        this(origen, destino, distancia, tieneEscaleras, true); 
    }

    public Arista(String origen, String destino, int distancia, boolean tieneEscaleras, boolean esAccesible) {
        this.origen = origen;
        this.destino = destino;
        this.distancia = distancia;
        this.tieneEscaleras = tieneEscaleras;
        this.esAccesible = esAccesible; 
    }

    public String getOrigen() {
        return origen;
    }

    public String getDestino() {
        return destino;
    }

    public int getDistancia() {
        return distancia;
    }

    public int getPeso() {
        return distancia;
    }

    public boolean tieneEscaleras() {
        return tieneEscaleras;
    }

    public boolean esAccesible() { // Método para obtener la accesibilidad
        return esAccesible;
    }

    @Override
    public String toString() {
        return "Arista{" +
                "origen='" + origen + '\'' +
                ", destino='" + destino + '\'' +
                ", distancia=" + distancia +
                ", tieneEscaleras=" + tieneEscaleras +
                ", esAccesible=" + esAccesible + // Mostrar la nueva propiedad
                '}';
    }
}
