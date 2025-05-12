package rutas;

public class Arista {
    private final String origen;
    private final String destino;
    private final int distancia;
    private final boolean tieneEscaleras; 

    public Arista(String origen, String destino, int distancia) {
        this(origen, destino, distancia, false); 
    }

    public Arista(String origen, String destino, int distancia, boolean tieneEscaleras) {
        this.origen = origen;
        this.destino = destino;
        this.distancia = distancia;
        this.tieneEscaleras = tieneEscaleras;
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

    @Override
    public String toString() {
        return "Arista{" +
                "origen='" + origen + '\'' +
                ", destino='" + destino + '\'' +
                ", distancia=" + distancia +
                ", tieneEscaleras=" + tieneEscaleras +
                '}';
    }
}
