package rutas;

public class Arista {
    private final String origen;
    private final String destino;
    private final int distancia;

    public Arista(String origen, String destino, int distancia) {
        this.origen = origen;
        this.destino = destino;
        this.distancia = distancia;
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

    @Override
    public String toString() {
        return "Arista{" +
                "origen='" + origen + '\'' +
                ", destino='" + destino + '\'' +
                ", distancia=" + distancia +
                '}';
    }
}
