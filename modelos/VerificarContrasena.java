package modelos;

public class VerificarContrasena {

    public static boolean esValida(String pass) {        
        return tieneLongitud(pass) &&
               noEsComun(pass) &&
               noTieneSecuencias(pass) &&
               tieneMayuscula(pass) &&
               tieneNumero(pass) &&
               tieneEspecial(pass);
    }
    
    private static boolean tieneLongitud(String pass) {
        if (pass.length() >= 8){
            return true;
        } else{
            System.out.println("La contraseña tiene menos de 8 caracteres.");
            return false;
        }
    }
    
    private static boolean noEsComun(String pass) {
        String[] comunes = { "123456", "12345678", "password", "qwerty" };
        
        for(String c : comunes){
            if(pass.equalsIgnoreCase(c)){
                System.out.println("La contraseña es comun, introduzca una mas segura.");
                return false;
            }
        }
        return true;
    }
    
    private static boolean noTieneSecuencias(String pass) {
        if (!pass.contains("123") && !pass.contains("abc")){
            return true;
        } else {
            System.out.println("La contraseña sigue una secuencia, introduzca una mas segura.");
            return false;
        }
    }
    
    private static boolean tieneMayuscula(String pass) {
        if (pass.matches(".*[A-Z].*")){
            return true;
        } else {
            System.out.println("La contraseña debe tener al menos una mayuscula.");
            return false;
        }
    }

    private static boolean tieneNumero(String pass) {
        if (pass.matches(".*[0-9].*")){
            return true;
        } else {    
            System.out.println("La contraseña debe contener al menos un numero.");
            return false;
        }
    }

    private static boolean tieneEspecial(String pass) {
        if (pass.matches(".*[^a-zA-Z0-9].*")){
            return true;
        } else {
            System.out.println("La contraseña debe contener al menos un caracter especial");
            return false;
        }
    }
    
}
