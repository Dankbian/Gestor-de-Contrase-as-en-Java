package modelos;

import java.io.*;
import javax.crypto.SecretKey;
import java.net.URISyntaxException;

public class AlmacenamientoBoveda { 

    private static final String NOMBRE_ARCHIVO = "boveda.dat";
    private File archivoBoveda;

    public AlmacenamientoBoveda() {

        String rutaProyecto = obtenerRutaProyecto();

        File dirBoveda = new File(rutaProyecto);

        if (!dirBoveda.exists()) {
            dirBoveda.mkdirs();
        }

        archivoBoveda = new File(dirBoveda, NOMBRE_ARCHIVO);

        System.out.println("Archivo de bóveda en raíz del proyecto: " + archivoBoveda.getAbsolutePath());
    }

    private String obtenerRutaProyecto() {
        try {
            File actual = new File(AlmacenamientoBoveda.class
                    .getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .toURI());

           
            return actual.getParentFile().getAbsolutePath();

        } catch (URISyntaxException e) {
            throw new RuntimeException("Error al obtener ruta del proyecto", e);
        }
    }

    public boolean existeBoveda() {
        return archivoBoveda.exists();
    }

    public Boveda cargarBoveda(String contrasena) throws Exception {
        try (FileInputStream archivoEntrada = new FileInputStream(archivoBoveda)) {

            byte[] datosCifrados = archivoEntrada.readAllBytes();
            SecretKey clave = UtilidadesCifrado.obtenerClaveDesdeContrasena(contrasena);
            byte[] datosDescifrados = UtilidadesCifrado.descifrar(datosCifrados, clave);
            return Boveda.crearDesdeBytes(datosDescifrados);

        } catch (javax.crypto.BadPaddingException e) {
            throw new Exception("Contraseña incorrecta o archivo dañado.");
        } catch (FileNotFoundException e) {
            throw new Exception("No se encontró el archivo de bóveda en: " + archivoBoveda.getAbsolutePath());
        }
    }

    public void guardarBoveda(Boveda boveda, String contrasena) throws Exception {

        SecretKey clave = UtilidadesCifrado.obtenerClaveDesdeContrasena(contrasena);
        byte[] datosOriginales = boveda.convertirABytes();
        byte[] datosCifrados = UtilidadesCifrado.cifrar(datosOriginales, clave);

        try (FileOutputStream archivoSalida = new FileOutputStream(archivoBoveda)) {
            archivoSalida.write(datosCifrados);
        }
    }
}
