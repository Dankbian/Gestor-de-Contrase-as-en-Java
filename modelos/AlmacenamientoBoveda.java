package modelos;

import java.io.*;
import javax.crypto.SecretKey;

public class AlmacenamientoBoveda {

    private static final String NOMBRE_ARCHIVO = "boveda.dat";
    private File archivoBoveda;

    public AlmacenamientoBoveda() {

        File directorioActual = new File(System.getProperty("user.dir"));
        File raizProyecto = directorioActual;

        while (raizProyecto != null) {
            File srcDir = new File(raizProyecto, "src");
            File ideaDir = new File(raizProyecto, ".idea");

            if (srcDir.exists() || ideaDir.exists()) {
                break;
            }

            if (raizProyecto.getParentFile() == null) {
                break;
            }

            raizProyecto = raizProyecto.getParentFile();
        }

        archivoBoveda = new File(raizProyecto, NOMBRE_ARCHIVO);
        System.out.println("Archivo de bóveda en: " + archivoBoveda.getAbsolutePath());
    }

    public boolean existeBoveda() {
        return archivoBoveda.exists();
    }

    // Cargar archivo del disco -> Descifrar -> Convertir en Objeto
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

        // Asegurar que el directorio padre exista
        File directorioPadre = archivoBoveda.getParentFile();
        if (directorioPadre != null && !directorioPadre.exists()) {
            if (!directorioPadre.mkdirs()) {
                throw new IOException("No se pudo crear el directorio: " + directorioPadre.getAbsolutePath());
            }
        }

        try (FileOutputStream archivoSalida = new FileOutputStream(archivoBoveda)) {
            archivoSalida.write(datosCifrados);
            System.out.println("Bóveda guardada exitosamente");
        }
    }
}
