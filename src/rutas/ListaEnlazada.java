package rutas;

import java.util.Iterator;

public class ListaEnlazada<T> implements Iterable<T> {
    private Nodo<T> cabeza;
    private int tamaño;

    public ListaEnlazada() {
        cabeza = null;
        tamaño = 0;
    }

    // Agregar un elemento al final de la lista
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

    // Insertar un elemento al inicio de la lista
    public void insertarInicio(T elemento) {
        Nodo<T> nuevoNodo = new Nodo<>(elemento);
        nuevoNodo.siguiente = cabeza;
        cabeza = nuevoNodo;
        tamaño++;
    }

    // Obtener el tamaño de la lista
    public int size() {
        return tamaño;
    }

    // Obtener el elemento en la posición 'indice'
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

    // Vaciar la lista
    public void clear() {
        cabeza = null;
        tamaño = 0;
    }

    // Agregar todos los elementos de otra lista
    public void agregarTodos(ListaEnlazada<T> otraLista) {
        for (T elemento : otraLista) {
            agregar(elemento);  // Agrega cada elemento al final de la lista
        }
    }

    // Copiar los elementos de otra lista
    public void copiarDesde(ListaEnlazada<T> otra) {
        this.cabeza = null;
        this.tamaño = 0;
        for (T elemento : otra) {
            this.agregar(elemento);
        }
    }

    // Iterador para recorrer la lista
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

    // Nodo interno para la lista
    private static class Nodo<T> {
        T elemento;
        Nodo<T> siguiente;

        Nodo(T elemento) {
            this.elemento = elemento;
            this.siguiente = null;
        }
    }
}
