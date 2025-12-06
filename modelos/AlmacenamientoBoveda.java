package modelos;

import java.io.*;
import javax.crypto.SecretKey;


public class AlmacenamientoBoveda {

    private static final String NOMBRE_ARCHIVO = "boveda.dat";
    private File archivoBoveda;

    public AlmacenamientoBoveda() {

        // Carpeta oculta en HOME para hacerlo pro y seguro
        File dirBoveda = new File(System.getProperty("user.home"), ".boveda");

        if (!dirBoveda.exists()) {
            dirBoveda.mkdirs(); // crea ~/.boveda
        }

        archivoBoveda = new File(dirBoveda, NOMBRE_ARCHIVO);

        System.out.println("Archivo de bóveda en: " + archivoBoveda.getAbsolutePath());
    }

    public boolean existeBoveda() {
        return archivoBoveda.exists();
    }

    public Boveda cargarBoveda(String contrasena) throws Exception {
        try (FileInputStream archivoEntrada = new FileInputStream(archivoBoveda)) {

            // 1. Leemos los bytes cifrados del disco
            byte[] datosCifrados = archivoEntrada.readAllBytes();

            // 2. Preparamos la llave
            SecretKey clave = UtilidadesCifrado.obtenerClaveDesdeContrasena(contrasena);

            // 3. Desciframos
            byte[] datosDescifrados = UtilidadesCifrado.descifrar(datosCifrados, clave);

            // 4. Convertimos bytes a Objeto Boveda
            return Boveda.crearDesdeBytes(datosDescifrados);

        } catch (javax.crypto.BadPaddingException e) {
            throw new Exception("Contraseña incorrecta o archivo dañado.");
        } catch (FileNotFoundException e) {
            throw new Exception("No se encontró el archivo de bóveda en: " + archivoBoveda.getAbsolutePath());
        }
    }

    // Objeto Boveda -> Convertir a Bytes -> Cifrar -> Guardar en disco
    public void guardarBoveda(Boveda boveda, String contrasena) throws Exception {

        SecretKey clave = UtilidadesCifrado.obtenerClaveDesdeContrasena(contrasena);

        byte[] datosOriginales = boveda.convertirABytes();
        byte[] datosCifrados = UtilidadesCifrado.cifrar(datosOriginales, clave);

        try (FileOutputStream archivoSalida = new FileOutputStream(archivoBoveda)) {
            archivoSalida.write(datosCifrados);
        }
    }
}
