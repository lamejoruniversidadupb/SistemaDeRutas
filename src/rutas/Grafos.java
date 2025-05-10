package rutas;

public class Grafos {
    private final ListaEnlazada<Nodo> nodos = new ListaEnlazada<>();
    private final ListaEnlazada<Arista> aristas = new ListaEnlazada<>();

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
        conectar("D", "K", 150);
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

    private void agregarNodo(Nodo nodo) {
        nodos.agregar(nodo);
    }

    private void conectar(String a, String b, int distancia) {
        aristas.agregar(new Arista(a, b, distancia));
        aristas.agregar(new Arista(b, a, distancia)); // para grafo no dirigido
    }

    public ListaEnlazada<String> dijkstra(String inicio, String destino) {
        ListaEnlazada<String> nodosPorVisitar = new ListaEnlazada<>();
        ListaEnlazada<String> nodosVisitados = new ListaEnlazada<>();
        ListaEnlazada<String> ruta = new ListaEnlazada<>();
        ListaEnlazada<DistanciaNodo> distancias = new ListaEnlazada<>();

        // Inicializar distancias
        for (Nodo nodo : nodos) {
            int distancia = nodo.getNombre().equals(inicio) ? 0 : Integer.MAX_VALUE;
            distancias.agregar(new DistanciaNodo(nodo.getNombre(), distancia, null));
            nodosPorVisitar.agregar(nodo.getNombre());
        }

        while (nodosPorVisitar.size() > 0) {
     
            DistanciaNodo actual = obtenerMenorDistancia(distancias, nodosPorVisitar);
            if (actual == null) break;

            nodosVisitados.agregar(actual.nombre);
            eliminarDeLista(nodosPorVisitar, actual.nombre);

            for (Arista arista : aristas) {
                if (arista.getOrigen().equals(actual.nombre)) {
                    String vecino = arista.getDestino();
                    if (!contiene(nodosVisitados, vecino)) {
                        int nuevaDistancia = actual.distancia + arista.getDistancia();
                        DistanciaNodo dnVecino = buscarDistancia(distancias, vecino);
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
            DistanciaNodo dn = buscarDistancia(distancias, actual);
            actual = dn != null ? dn.anterior : null;
        }

        if (ruta.size() > 0 && ruta.get(0).equals(inicio)) {
            return ruta;
        } else {
            return new ListaEnlazada<>();
        }
    }

    private DistanciaNodo obtenerMenorDistancia(ListaEnlazada<DistanciaNodo> distancias, ListaEnlazada<String> porVisitar) {
        DistanciaNodo menor = null;
        for (DistanciaNodo dn : distancias) {
            if (contiene(porVisitar, dn.nombre)) {
                if (menor == null || dn.distancia < menor.distancia) {
                    menor = dn;
                }
            }
        }
        return menor;
    }

    private void eliminarDeLista(ListaEnlazada<String> lista, String valor) {
        ListaEnlazada<String> nueva = new ListaEnlazada<>();
        for (String s : lista) {
            if (!s.equals(valor)) {
                nueva.agregar(s);
            }
        }
        lista.clear();
        lista.agregarTodos(nueva);
    }

    private boolean contiene(ListaEnlazada<String> lista, String valor) {
        for (String s : lista) {
            if (s.equals(valor)) return true;
        }
        return false;
    }

    private DistanciaNodo buscarDistancia(ListaEnlazada<DistanciaNodo> distancias, String nombre) {
        for (DistanciaNodo dn : distancias) {
            if (dn.nombre.equals(nombre)) return dn;
        }
        return null;
    }

    public Nodo getNodo(String nombre) {
        for (Nodo nodo : nodos) {
            if (nodo.getNombre().equals(nombre)) {
                return nodo;
            }
        }
        return null;  
    }
    public ListaEnlazada<String> getNombresNodos() {
    ListaEnlazada<String> nombresNodos = new ListaEnlazada<>();
    for (Nodo nodo : nodos) {
        nombresNodos.agregar(nodo.getNombre());
    }
    return nombresNodos;
}


    public ListaEnlazada<Nodo> getNodos() {
        return nodos;
    }

    public ListaEnlazada<Arista> getAristas() {
        return aristas;
    }

    private static class DistanciaNodo {
        String nombre;
        int distancia;
        String anterior;

        DistanciaNodo(String nombre, int distancia, String anterior) {
            this.nombre = nombre;
            this.distancia = distancia;
            this.anterior = anterior;
        }
    }
}
