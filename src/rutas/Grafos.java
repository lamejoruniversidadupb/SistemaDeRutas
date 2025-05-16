package rutas;

import java.util.HashSet;
import java.util.Set;

public class Grafos {

    private final ListaEnlazada<Nodo> nodos = new ListaEnlazada<>();
    private final ListaEnlazada<Arista> aristas = new ListaEnlazada<>();
    private ListaEnlazada<ListaEnlazada.DistanciaNodo> distancias = new ListaEnlazada<>();

    public Grafos() {
        inicializarNodos();
        inicializarGrafo();
    }

    private void inicializarNodos() {
        agregarNodo(new Nodo("A", 20, 250));
        agregarNodo(new Nodo("B", 60, 270));
        agregarNodo(new Nodo("C", 100, 290));
        agregarNodo(new Nodo("CAFETERIA", 60, 200));
        agregarNodo(new Nodo("D", 160, 250));
        agregarNodo(new Nodo("E", 220, 200));
        agregarNodo(new Nodo("F", 280, 180));
        agregarNodo(new Nodo("G", 260, 250));
        agregarNodo(new Nodo("H", 280, 120));
        agregarNodo(new Nodo("I", 240, 60));
        agregarNodo(new Nodo("J", 180, 320));
        agregarNodo(new Nodo("K", 160, 100));
        agregarNodo(new Nodo("L", 300, 40));
        agregarNodo(new Nodo("PORTERIA", 140, 360));
    }

    private void inicializarGrafo() {
        conectar("A", "B", 10);
        conectar("B", "C", 10);
        conectar("B", "CAFETERIA", 15);
        conectar("CAFETERIA", "C", 15);
        conectar("CAFETERIA", "D", 30);
        conectar("C", "PORTERIA", 30);
        conectar("PORTERIA", "J", 30);
        conectar("J", "D", 40);
        conectar("D", "E", 40);
        conectar("D", "G", 25);
        conectar("D", "K", 50);
        conectar("E", "F", 10);
        conectar("E", "I", 60);
        conectar("F", "G", 20);
        conectar("H", "F", 15);
        conectar("H", "I", 30);
        conectar("H", "L", 130);
        conectar("I", "K", 100);
        conectar("I", "L", 20);
        conectar("J", "G", 50);
        conectar("C", "J", 15);
    }

    public void agregarNodo(Nodo nodo) {
        nodos.agregar(nodo);
    }

    public void eliminarNodo(Nodo nodo) {
        nodos.eliminar(nodo);
    }

    public void conectar(String origen, String destino, int distancia) {
        aristas.agregar(new Arista(origen, destino, distancia));
        aristas.agregar(new Arista(destino, origen, distancia));
    }

    public ListaEnlazada<String> obtenerRuta(String inicio, String destino, boolean evitarEscaleras) {
        if (evitarEscaleras) {
            return dijkstraEvitarEscaleras(inicio, destino);
        } else {
            return dijkstra(inicio, destino);
        }
    }

    public ListaEnlazada<ListaEnlazada<String>> obtenerVariasRutasAlternativas(String inicio, String destino, ListaEnlazada<String> rutaPrincipal, boolean evitarEscaleras) {
        ListaEnlazada<ListaEnlazada<String>> rutasAlternativas = new ListaEnlazada<>();
        ListaEnlazada<String> alternativa = obtenerRutaAlternativa(inicio, destino, rutaPrincipal, evitarEscaleras);
        if (alternativa != null) {
            rutasAlternativas.agregar(alternativa);
        }
        return rutasAlternativas;
    }

    private ListaEnlazada<String> obtenerRutaAlternativa(String inicio, String destino, ListaEnlazada<String> rutaPrincipal, boolean evitarEscaleras) {
        distancias.clear();
        ListaEnlazada<String> nodosPorVisitar = new ListaEnlazada<>();
        ListaEnlazada<String> nodosVisitados = new ListaEnlazada<>();
        ListaEnlazada<String> rutaAlternativa = new ListaEnlazada<>();

        ListaEnlazada<String> nodosValidos = new ListaEnlazada<>();
        for (Nodo nodo : nodos) {
            if (!evitarEscaleras || nodo.getNombre().equals(inicio) || nodo.getNombre().equals(destino) || !estaEnEscalera(nodo.getNombre())) {
                nodosValidos.agregar(nodo.getNombre());
            }
        }

        for (String nodoNombre : nodosValidos) {
            int distancia = nodoNombre.equals(inicio) ? 0 : Integer.MAX_VALUE;
            distancias.agregar(new ListaEnlazada.DistanciaNodo(nodoNombre, distancia, null));
            nodosPorVisitar.agregar(nodoNombre);
        }

        while (nodosPorVisitar.size() > 0) {
            ListaEnlazada.DistanciaNodo actual = obtenerMenorDistancia(distancias, nodosPorVisitar);
            if (actual == null) break;

            nodosVisitados.agregar(actual.nombre);
            eliminarDeLista(nodosPorVisitar, actual.nombre);

            for (Arista arista : aristas) {
                if (arista.getOrigen().equals(actual.nombre)) {
                    String vecino = arista.getDestino();
                    if (nodosValidos.contiene(vecino) && !contiene(nodosVisitados, vecino)) {
                        if (!estaEnRutaPrincipal(arista, rutaPrincipal)) {
                            int nuevaDistancia = actual.distancia == Integer.MAX_VALUE ? Integer.MAX_VALUE : actual.distancia + arista.getDistancia();
                            ListaEnlazada.DistanciaNodo dnVecino = buscarDistancia(distancias, vecino);
                            if (dnVecino != null && nuevaDistancia < dnVecino.distancia) {
                                dnVecino.distancia = nuevaDistancia;
                                dnVecino.anterior = actual.nombre;
                            }
                        }
                    }
                }
            }
        }

        String actual = destino;
        while (actual != null) {
            rutaAlternativa.insertarInicio(actual);
            ListaEnlazada.DistanciaNodo dn = buscarDistancia(distancias, actual);
            actual = (dn != null) ? dn.anterior : null;
        }

        if (rutaAlternativa.size() > 0 && rutaAlternativa.get(0).equals(inicio) && !rutaIgual(rutaAlternativa, rutaPrincipal)) {
            return rutaAlternativa;
        } else {
            return null;
        }
    }

    public ListaEnlazada<String> dijkstra(String inicio, String destino) {
        distancias.clear();
        ListaEnlazada<String> nodosPorVisitar = new ListaEnlazada<>();
        ListaEnlazada<String> nodosVisitados = new ListaEnlazada<>();
        ListaEnlazada<String> ruta = new ListaEnlazada<>();

        for (Nodo nodo : nodos) {
            int distancia = nodo.getNombre().equals(inicio) ? 0 : Integer.MAX_VALUE;
            distancias.agregar(new ListaEnlazada.DistanciaNodo(nodo.getNombre(), distancia, null));
            nodosPorVisitar.agregar(nodo.getNombre());
        }

        while (nodosPorVisitar.size() > 0) {
            ListaEnlazada.DistanciaNodo actual = obtenerMenorDistancia(distancias, nodosPorVisitar);
            if (actual == null) break;

            nodosVisitados.agregar(actual.nombre);
            eliminarDeLista(nodosPorVisitar, actual.nombre);

            for (Arista arista : aristas) {
                if (arista.getOrigen().equals(actual.nombre)) {
                    String vecino = arista.getDestino();
                    if (!contiene(nodosVisitados, vecino)) {
                        int nuevaDistancia = actual.distancia == Integer.MAX_VALUE ? Integer.MAX_VALUE : actual.distancia + arista.getDistancia();
                        ListaEnlazada.DistanciaNodo dnVecino = buscarDistancia(distancias, vecino);
                        if (dnVecino != null && nuevaDistancia < dnVecino.distancia) {
                            dnVecino.distancia = nuevaDistancia;
                            dnVecino.anterior = actual.nombre;
                        }
                    }
                }
            }
        }

        String actual = destino;
        while (actual != null) {
            ruta.insertarInicio(actual);
            ListaEnlazada.DistanciaNodo dn = buscarDistancia(distancias, actual);
            actual = (dn != null) ? dn.anterior : null;
        }

        if (ruta.size() > 0 && ruta.get(0).equals(inicio)) {
            return ruta;
        } else {
            return new ListaEnlazada<>();
        }
    }

    public ListaEnlazada<String> dijkstraEvitarEscaleras(String inicio, String destino) {
        distancias.clear();
        ListaEnlazada<String> nodosPorVisitar = new ListaEnlazada<>();
        ListaEnlazada<String> nodosVisitados = new ListaEnlazada<>();
        ListaEnlazada<String> ruta = new ListaEnlazada<>();

        ListaEnlazada<String> nodosPermitidos = new ListaEnlazada<>();
        for (Nodo nodo : nodos) {
            if (nodo.getNombre().equals(inicio) || nodo.getNombre().equals(destino) || !estaEnEscalera(nodo.getNombre())) {
                nodosPermitidos.agregar(nodo.getNombre());
            }
        }

        for (String nodoNombre : nodosPermitidos) {
            int distancia = nodoNombre.equals(inicio) ? 0 : Integer.MAX_VALUE;
            distancias.agregar(new ListaEnlazada.DistanciaNodo(nodoNombre, distancia, null));
            nodosPorVisitar.agregar(nodoNombre);
        }

        while (nodosPorVisitar.size() > 0) {
            ListaEnlazada.DistanciaNodo actual = obtenerMenorDistancia(distancias, nodosPorVisitar);
            if (actual == null) break;

            nodosVisitados.agregar(actual.nombre);
            eliminarDeLista(nodosPorVisitar, actual.nombre);

            for (Arista arista : aristas) {
                if (arista.getOrigen().equals(actual.nombre)) {
                    String vecino = arista.getDestino();
                    if (nodosPermitidos.contiene(vecino) && !contiene(nodosVisitados, vecino)) {
                        int nuevaDistancia = actual.distancia == Integer.MAX_VALUE ? Integer.MAX_VALUE : actual.distancia + arista.getDistancia();
                        ListaEnlazada.DistanciaNodo dnVecino = buscarDistancia(distancias, vecino);
                        if (dnVecino != null && nuevaDistancia < dnVecino.distancia) {
                            dnVecino.distancia = nuevaDistancia;
                            dnVecino.anterior = actual.nombre;
                        }
                    }
                }
            }
        }

        String actual = destino;
        while (actual != null) {
            ruta.insertarInicio(actual);
            ListaEnlazada.DistanciaNodo dn = buscarDistancia(distancias, actual);
            actual = (dn != null) ? dn.anterior : null;
        }

        if (ruta.size() > 0 && ruta.get(0).equals(inicio)) {
            return ruta;
        } else {
            return new ListaEnlazada<>();
        }
    }

    private boolean estaEnEscalera(String nodo) {
        switch (nodo.toUpperCase()) {
            case "PORTERIA":
            case "J":
            case "B":
            case "CAFETERIA":
            case "D":
            case "K":
            case "I":
                return true;
            default:
                return false;
        }
    }

    private ListaEnlazada.DistanciaNodo obtenerMenorDistancia(ListaEnlazada<ListaEnlazada.DistanciaNodo> distancias, ListaEnlazada<String> nodosPorVisitar) {
        ListaEnlazada.DistanciaNodo menor = null;
        for (ListaEnlazada.DistanciaNodo dn : distancias) {
            if (nodosPorVisitar.contiene(dn.nombre)) {
                if (menor == null || dn.distancia < menor.distancia) {
                    menor = dn;
                }
            }
        }
        return menor;
    }

    private void eliminarDeLista(ListaEnlazada<String> lista, String valor) {
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).equals(valor)) {
                lista.eliminar(valor);
                return;
            }
        }
    }

    private boolean contiene(ListaEnlazada<String> lista, String valor) {
        for (String s : lista) {
            if (s.equals(valor)) return true;
        }
        return false;
    }

    private ListaEnlazada.DistanciaNodo buscarDistancia(ListaEnlazada<ListaEnlazada.DistanciaNodo> distancias, String nombre) {
        for (ListaEnlazada.DistanciaNodo dn : distancias) {
            if (dn.nombre.equals(nombre)) return dn;
        }
        return null;
    }

    private boolean estaEnRutaPrincipal(Arista arista, ListaEnlazada<String> rutaPrincipal) {
        for (int i = 0; i < rutaPrincipal.size() - 1; i++) {
            String origen = rutaPrincipal.get(i);
            String destino = rutaPrincipal.get(i + 1);
            if ((arista.getOrigen().equals(origen) && arista.getDestino().equals(destino)) ||
                (arista.getOrigen().equals(destino) && arista.getDestino().equals(origen))) {
                return true;
            }
        }
        return false;
    }

    private boolean rutaIgual(ListaEnlazada<String> ruta1, ListaEnlazada<String> ruta2) {
        if (ruta1 == null || ruta2 == null) return false;
        if (ruta1.size() != ruta2.size()) return false;
        for (int i = 0; i < ruta1.size(); i++) {
            if (!ruta1.get(i).equals(ruta2.get(i))) return false;
        }
        return true;
    }

    public Nodo getNodo(String nombre) {
        for (Nodo nodo : nodos) {
            if (nodo.getNombre().equals(nombre))
                return nodo;
        }
        return null;
    }

    public Arista getArista(String origen, String destino) {
        for (Arista arista : aristas) {
            if (arista.getOrigen().equals(origen) && arista.getDestino().equals(destino))
                return arista;
        }
        return null;
    }

    public ListaEnlazada<Nodo> getNodos() {
        return nodos;
    }

    public ListaEnlazada<Arista> getAristas() {
        return aristas;
    }

    public ListaEnlazada<String> getNombresNodos() {
        ListaEnlazada<String> nombres = new ListaEnlazada<>();
        for (Nodo nodo : nodos) {
            nombres.agregar(nodo.getNombre());
        }
        return nombres;
    }
}

