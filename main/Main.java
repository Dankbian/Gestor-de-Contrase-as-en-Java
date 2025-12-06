package main;

import modelos.Boveda;
import modelos.VerificarContrasena;
import modelos.AlmacenamientoBoveda;
import modulos.ModuloBase;
import modulos.ModuloArchivos;
import modulos.ModuloBoveda;

import java.io.Console;
import java.util.Scanner;

public class Main {

    // Declaración de variables estáticas de la clase Main.

    // Scanner para leer entrada del usuario. Scanner es un tipo de dato de referencia ya qué es un objeto complejo perteneciente a java.util
    private static final Scanner lector = new Scanner(System.in);
    // Almacenamiento de la bóveda para guardar/cargar la bóveda. Almacenamiento es un dato de referencia perteneciente a modelos. AlmacenamientoBoveda.
    private static final AlmacenamientoBoveda almacenamiento = new AlmacenamientoBoveda();
    // Bóveda actual en memoria. Boveda es un dato de referencia perteneciente a modelos. Boveda.
    private static Boveda boveda;
    // Contraseña maestra actual. Tipo de dato primitivo String.
    private static String contrasenaActual;


    // --- Método Principal ---
    public static void main(String[] args) {
        System.out.println("--- SUITE DE SEGURIDAD (JAVA POO) ---");

        // Validamos si existe bóveda para saber si simplemente iniciar sesión o crear una nueva bóveda.
        try {
            // Login (Existe bóveda o no)
            if (almacenamiento.existeBoveda()) {
                iniciarSesion();
            } else {
                crearNuevaBoveda();
            }


            
            // POLIMORFISMO: Usamos la clase Padre (ModuloBase) para referirnos a los hijos
            ModuloBase moduloBoveda = new ModuloBoveda(boveda, almacenamiento, contrasenaActual, lector);
            ModuloBase moduloArchivos = new ModuloArchivos(contrasenaActual, lector);


            // 3. Menú Principal
            boolean enEjecucion = true;
            while (enEjecucion) {
                System.out.println("\n--- Menú Principal ---");
                System.out.println("1. " + moduloBoveda.obtenerNombre());
                System.out.println("2. " + moduloArchivos.obtenerNombre());
                System.out.println("3. Cambiar contraseña maestra");
		System.out.println("4. Salir");
                System.out.print("> ");

                // Leemos opción (usando trim para evitar espacios basura)
                String opcion = lector.nextLine().trim();

                switch (opcion) {
                    case "1":
                        moduloBoveda.ejecutar(); // Polimorfismo
                        break;
                    case "2":
                        moduloArchivos.ejecutar(); // Polimorfismo
                        break;
                    case "3":
                        cambiarContrasenaMaestra();
			moduloBoveda = new ModuloBoveda(boveda, almacenamiento, contrasenaActual, lector);
			moduloArchivos = new ModuloArchivos(contrasenaActual, lector);
			break;
		    case "4":
			System.out.println("Cerrando programa... ¡Adiós!");
                        enEjecucion = false;
                        break;
                    default:
                        System.out.println("Opción no válida.");
                }
            }

        } catch (Exception e) {
            System.err.println("Error fatal: " + e.getMessage());
        }
    }

    // --- Métodos de Login ---

    private static void crearNuevaBoveda() throws Exception {
        System.out.println("\n--- Configuración Inicial ---");  
        System.out.println("Bienvenido. Crea una contraseña maestra.");
        
        String nuevaContra = leerContrasena("Nueva contraseña: ", true);
        String nuevaContraVerificada = leerContrasena("Confirme su contraseña: ", false);

        if (!nuevaContra.equals(nuevaContraVerificada)) {
            throw new Exception("Las contraseñas no coinciden.");
        }

        contrasenaActual = nuevaContra;
        boveda = new Boveda();
        almacenamiento.guardarBoveda(boveda, contrasenaActual);
        System.out.println("¡Sistema configurado correctamente!");
    }

    // Método para iniciar sesión en la bóveda existente. throws es para manejar excepciones.
    private static void iniciarSesion() throws Exception {
        System.out.println("\n--- Inicio de Sesión ---");
        // contraAlmacenada es una variable local de tipo String que almacena la contraseña ingresada por el usuario.
        String contraAlmacenada = leerContrasena("Introduce tu contraseña maestra: ", false);

        boveda = almacenamiento.cargarBoveda(contraAlmacenada);
        contrasenaActual = contraAlmacenada;
        System.out.println("¡Acceso concedido!");
    }
    
    private static String leerContrasena(String mensaje, boolean pene) {
        Console console = System.console();
        String contrasena = "";
        boolean bandera = true;

        while (bandera) {
            if (console != null) {
                char[] pass = console.readPassword(mensaje);
                contrasena = new String(pass);
            } else {
                System.out.print(mensaje);
                contrasena = lector.nextLine();
            }

            if (pene) {
                bandera = esNueva(contrasena);
            } else {
                bandera = false;
            }
        }
        return contrasena;
    }

    private static void cambiarContrasenaMaestra(){
    	System.out.println("\n--- Cambio de contraseña maestra ---");
	try {
	// Primero verificmos que la contraseña maestra sea la misma, es decir, llevamos a cabo una autenticación
	    String contraActualIngresada = leerContrasena("Introduce tu contraseña actual: ", false);
	    almacenamiento.cargarBoveda(contraActualIngresada);

	    //Solicitamos la nueva contraseña 
	    String nuevaContra = leerContrasena("Introduce la nueva contraseña: ", true);
	    String nuevaContraVerificada = leerContrasena("Confirme la nueva contraseña: ", false);

	    if (!nuevaContra.equals(nuevaContraVerificada)){
	    System.out.println("ERROR: Las contraseñas no coinciden");

	    	return;
	    }

	    //Aplicamos el cambio de la contraseña 
	    almacenamiento.guardarBoveda(boveda, nuevaContra);

	    // Se actualiza también la variable estática en memoria 
	    contrasenaActual = nuevaContra;
	    System.out.println("Contraseña maestra cambiada con éxito");
	} catch (Exception e){
	     System.err.println("Error al cambiar la contraseña: " + e.getMessage());	
	}
    }

    private static boolean esNueva(String contrasena) {
        if(VerificarContrasena.esValida(contrasena)){
            return false;
        } else {
            return true;
        }
    }

}
