package modelos;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class UtilidadesCifrado {

    // Constantes para configurar el cifrado
    private static final String ALGORITMO_HASH = "SHA-256";
    private static final String ALGORITMO_CIFRADO = "AES";

    /**
     * Convierte texto (contraseña) en una Llave secreta válida para AES.
     */
    public static SecretKey obtenerClaveDesdeContrasena(String contrasena) throws Exception {
        // Usamos SHA-256 para "triturar" la contraseña y obtener bytes fijos
        MessageDigest digest = MessageDigest.getInstance(ALGORITMO_HASH);
        byte[] bytesClave = digest.digest(contrasena.getBytes(StandardCharsets.UTF_8));

        // Retornamos la llave lista
        return new SecretKeySpec(bytesClave, "AES");
    }

    /**
     * Encripta (Cifra) datos.
     */
    public static byte[] cifrar(byte[] datos, SecretKey clave) throws Exception {
        Cipher cifrador = Cipher.getInstance(ALGORITMO_CIFRADO);
        cifrador.init(Cipher.ENCRYPT_MODE, clave);
        return cifrador.doFinal(datos);
    }

    /**
     * Desencripta (Descifra) datos.
     */
    public static byte[] descifrar(byte[] datosCifrados, SecretKey clave) throws Exception {
        Cipher cifrador = Cipher.getInstance(ALGORITMO_CIFRADO);
        cifrador.init(Cipher.DECRYPT_MODE, clave);
        return cifrador.doFinal(datosCifrados);
    }
}