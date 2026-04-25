package com.ncy.manejadores;

import android.view.inputmethod.InputConnection;

public class GestorAutocierre {

    private static final char[] APERTURAS = {'(', '[', '{', '"', '\'', '`'};
    private static final char[] CIERRES   = {')', ']', '}', '"', '\'', '`'};

    public boolean procesar(String texto, InputConnection conexion) {
        if (conexion == null || texto == null || texto.length() != 1) return false;

        char c = texto.charAt(0);

        // 1. Lógica para saltar el cierre si ya estamos justo delante de él
        for (int i = 0; i < CIERRES.length; i++) {
            if (c == CIERRES[i]) {
                CharSequence siguiente = conexion.getTextAfterCursor(1, 0);
                if (siguiente != null && siguiente.length() == 1 && siguiente.charAt(0) == c) {
                    // Truco relativo: borramos el carácter de la derecha y lo reescribimos.
                    // Esto avanza el cursor de forma segura sin usar coordenadas absolutas.
                    conexion.beginBatchEdit();
                    conexion.deleteSurroundingText(0, 1);
                    conexion.commitText(String.valueOf(c), 1);
                    conexion.endBatchEdit();
                    return true;
                }
                return false;
            }
        }

        // 2. Lógica para insertar el par y dejar el cursor en el centro
        for (int i = 0; i < APERTURAS.length; i++) {
            if (c == APERTURAS[i]) {
                char cierre = CIERRES[i];
                conexion.beginBatchEdit();
                
                // Insertamos la apertura y el cursor avanza (queda después de la apertura)
                conexion.commitText(texto, 1);
                
                // Insertamos el cierre con '0'. Esto inserta el texto a la derecha, 
                // pero obliga al cursor a quedarse ANTES del texto recién insertado.
                conexion.commitText(String.valueOf(cierre), 0);
                
                conexion.endBatchEdit();
                return true;
            }
        }

        return false;
    }
}