package rutas;

import java.util.Iterator;

public class ListaEnlazada<T> implements Iterable<T> {
    private Nodo<T> cabeza;
    private int tamaño;

    public ListaEnlazada() {
        cabeza = null;
        tamaño = 0;
    }

    public void agregar(T elemento) {
        Nodo<T> nuevoNodo = new Nodo<>(elemento);
        if (cabeza == null) {
            cabeza = nuevoNodo;
        } else {
            Nodo<T> temp = cabeza;
            while (temp.siguiente != null) {
                temp = temp.siguiente;
            }
            temp.siguiente = nuevoNodo;
        }
        tamaño++;
    }

    public void insertarInicio(T elemento) {
        Nodo<T> nuevoNodo = new Nodo<>(elemento);
        nuevoNodo.siguiente = cabeza;
        cabeza = nuevoNodo;
        tamaño++;
    }

    public int size() {
        return tamaño;
    }

    public T get(int indice) {
        Nodo<T> temp = cabeza;
        for (int i = 0; i < indice; i++) {
            if (temp == null) {
                return null;  // Índice fuera de rango
            }
            temp = temp.siguiente;
        }
        return temp != null ? temp.elemento : null;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Nodo<T> actual = cabeza;

            @Override
            public boolean hasNext() {
                return actual != null;
            }

            @Override
            public T next() {
                T elemento = actual.elemento;
                actual = actual.siguiente;
                return elemento;
            }
        };
    }

    private static class Nodo<T> {
        T elemento;
        Nodo<T> siguiente;

        Nodo(T elemento) {
            this.elemento = elemento;
            this.siguiente = null;
        }
    }
}
