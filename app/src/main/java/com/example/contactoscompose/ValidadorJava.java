
package com.example.contactoscompose;

import android.util.Patterns;

public class ValidadorJava {

    public static boolean nombreValido(String nombre) {
        return nombre != null && !nombre.trim().isEmpty();
    }

    public static boolean correoValido(String correo) {
        return correo != null
                && Patterns.EMAIL_ADDRESS
                .matcher(correo.trim())
                .matches();
    }

    public static boolean telefonoValido(String telefono) {
        return telefono != null
                && telefono.matches("[0-9]{8}");
    }
}
