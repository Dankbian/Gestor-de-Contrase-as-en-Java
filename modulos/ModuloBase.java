package modulos;

import java.util.Scanner;

public abstract class ModuloBase {

    // OJO: Solo declaramos, NO inicializamos con 'new' aquí.
    protected Scanner lector;

    // Constructor: Recibe el scanner del Main y lo guarda.
    public ModuloBase(Scanner lector) {
        this.lector = lector;
    }

    // Método utilitario para leer números y limpiar el buffer
    protected int leerOpcion() {
        try {
            // El .trim() es vital para quitar espacios fantasma
            return Integer.parseInt(lector.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1; // Opción inválida
        }
    }
    // --- MÉTODOS ABSTRACTOS (Polimorfismo) ---
    // Obligamos a los hijos a tener estos métodos, así eliminamos la Interfaz.

    public abstract void ejecutar() throws Exception;

    public abstract String obtenerNombre();
}