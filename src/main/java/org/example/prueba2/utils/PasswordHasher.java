package org.example.prueba2.utils;

//clase que define juego de caracteres standar como UTF-8, son utilizados para contraseña antes del hash
import java.nio.charset.StandardCharsets;
//Clase que proporciona funcionalidades para algoritmos de resumen de mensajes criptograficos, SHA-256, para hacer hash
import java.security.MessageDigest;
//para manejar excepciones de los mesnajes criptograficos solitados
import java.security.NoSuchAlgorithmException;
//clase utilitaria para codificar y decoficar datos en formaro base 64
import java.util.Base64;

public class PasswordHasher {

    public static String hashPassword(String password) throws NoSuchAlgorithmException
    {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] hashBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            return Base64.getEncoder().encodeToString(hashBytes);
        }
        catch (NoSuchAlgorithmException ex)
        {
            return null;
        }
    }
}
