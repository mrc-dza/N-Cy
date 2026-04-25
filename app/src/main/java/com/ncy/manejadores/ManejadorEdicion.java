package com.ncy.manejadores;

import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;

import com.ncy.estado.EstadoTeclado;
import com.ncy.modelo.TeclaCaracter;
import com.ncy.modelo.TeclaEmoji;
import com.ncy.modelo.TeclaMacro;
import com.ncy.utilidades.Constantes;

public class ManejadorEdicion {

    private static final int MAX_TEXTO_LIMPIAR = 10_000;

    private InputConnection conexion;
    private final EstadoTeclado estado;
    private final SimuladorHardware simulador;


    public ManejadorEdicion(EstadoTeclado estado, SimuladorHardware simulador) {
        this.estado = estado;
        this.simulador = simulador;
    }

    public void establecerConexion(InputConnection conexion) {
        this.conexion = conexion;
    }

    public void procesarMacro(TeclaMacro tecla) {
        if (conexion == null) return;
        conexion.commitText(tecla.obtenerTextoLargo(), 1);
    }

    public void procesarEmoji(TeclaEmoji tecla) {
        if (conexion == null) return;
        conexion.commitText(tecla.obtenerEmoji(), 1);
    }

    public void procesarCaracter(TeclaCaracter tecla) {
        if (conexion == null) return;
        String texto = tecla.obtenerEtiqueta();
        boolean esAtajo = estado.isModificadorActivo(Constantes.CODIGO_CTRL)
                       || estado.isModificadorActivo(Constantes.CODIGO_ALT);

        if (esAtajo && texto.length() == 1) {
            char c = texto.toLowerCase(java.util.Locale.getDefault()).charAt(0);
            if (c >= 'a' && c <= 'z') {
                int keyCode = c - 'a' + android.view.KeyEvent.KEYCODE_A;
                int metaState = simulador.calcularMetaState();
                simulador.enviarAtajoCaracter(keyCode, metaState);
                limpiarModificadoresAtajo();
                return;
            }
        }

        if (estado.isModificadorActivo(Constantes.CODIGO_SHIFT)) {
            texto = texto.toUpperCase(java.util.Locale.getDefault());
            estado.consumirShiftSiEsNecesario();
        } else {
            texto = texto.toLowerCase(java.util.Locale.getDefault());
        }
        
        conexion.commitText(texto, 1);
    }

    public void procesarEdicion(int codigo) {
        if (conexion == null) return;
        switch (codigo) {
            case Constantes.CODIGO_BORRAR -> {
                CharSequence seleccion = conexion.getSelectedText(0);
                if (seleccion != null && seleccion.length() > 0) {
                    conexion.commitText("", 1);
                } else {
                    conexion.deleteSurroundingText(1, 0);
                }
            }
            case Constantes.CODIGO_BORRAR_PALABRA -> borrarPalabraHaciaAtras();
            case Constantes.CODIGO_SUPRIMIR_PALABRA -> borrarPalabraHaciaAdelante();
            case Constantes.CODIGO_ENTER -> procesarEnter();
            case Constantes.CODIGO_ESPACIO -> conexion.commitText(" ", 1);
            case Constantes.CODIGO_TAB -> conexion.commitText("\t", 1);
            case Constantes.CODIGO_LIMPIAR -> {

                CharSequence antes  = conexion.getTextBeforeCursor(MAX_TEXTO_LIMPIAR, 0);
                CharSequence despues = conexion.getTextAfterCursor(MAX_TEXTO_LIMPIAR, 0);
                conexion.deleteSurroundingText(antes == null ? 0 : antes.length(),
                                               despues == null ? 0 : despues.length());
            }
            case Constantes.CODIGO_CURSOR_IZQUIERDA -> moverCursorSeguro(android.view.KeyEvent.KEYCODE_DPAD_LEFT, true);
            case Constantes.CODIGO_CURSOR_DERECHA -> moverCursorSeguro(android.view.KeyEvent.KEYCODE_DPAD_RIGHT, false);
            case Constantes.CODIGO_CURSOR_ARRIBA -> moverCursorSeguro(android.view.KeyEvent.KEYCODE_DPAD_UP, true);
            case Constantes.CODIGO_CURSOR_ABAJO -> moverCursorSeguro(android.view.KeyEvent.KEYCODE_DPAD_DOWN, false);
        }
    }
    

    public static boolean esCodigo(int codigo) {
        return switch (codigo) {
            case Constantes.CODIGO_BORRAR,
                 Constantes.CODIGO_BORRAR_PALABRA,
                 Constantes.CODIGO_SUPRIMIR_PALABRA,
                 Constantes.CODIGO_ENTER,
                 Constantes.CODIGO_ESPACIO,
                 Constantes.CODIGO_TAB,
                 Constantes.CODIGO_LIMPIAR,
                 Constantes.CODIGO_CURSOR_IZQUIERDA,
                 Constantes.CODIGO_CURSOR_DERECHA,
                 Constantes.CODIGO_CURSOR_ARRIBA,
                 Constantes.CODIGO_CURSOR_ABAJO -> true;
            default -> false;
        };
    }

    private void moverCursorSeguro(int keyCode, boolean haciaAtras) {
        if (conexion == null) return;
        CharSequence textoPared = haciaAtras
                ? conexion.getTextBeforeCursor(1, 0)
                : conexion.getTextAfterCursor(1, 0);
        if (textoPared != null && textoPared.length() > 0) {
            int metaState = simulador.calcularMetaState();
            long t = android.os.SystemClock.uptimeMillis(); // Tiempo real
            conexion.sendKeyEvent(new KeyEvent(t, t, KeyEvent.ACTION_DOWN, keyCode, 0, metaState));
            conexion.sendKeyEvent(new KeyEvent(t, t, KeyEvent.ACTION_UP,   keyCode, 0, metaState));
        }
    }

    private void limpiarModificadoresAtajo() {
        if (estado.isModificadorActivo(Constantes.CODIGO_CTRL)) {
            estado.alternarModificador(Constantes.CODIGO_CTRL);
        }
        if (estado.isModificadorActivo(Constantes.CODIGO_ALT)) {
            estado.alternarModificador(Constantes.CODIGO_ALT);
        }
    }

    private void procesarEnter() {
        int accionIme = estado.obtenerAccionIme();
        if (!estado.isMultilinea()
                && accionIme != EditorInfo.IME_ACTION_UNSPECIFIED
                && accionIme != EditorInfo.IME_ACTION_NONE) {
            conexion.performEditorAction(accionIme);
        } else {
            int metaState = 0;
            if (estado.isModificadorActivo(Constantes.CODIGO_SHIFT)) {
                metaState |= KeyEvent.META_SHIFT_ON;
                estado.consumirShiftSiEsNecesario();
            }
            simulador.enviarEnter(metaState);
        }
    }

    private void borrarPalabraHaciaAtras() {
        CharSequence texto = conexion.getTextBeforeCursor(500, 0); // Límite aumentado a 500
        if (texto == null || texto.length() == 0) return;
        int n = 0, i = texto.length() - 1;
        while (i >= 0 && Character.isWhitespace(texto.charAt(i))) { n++; i--; }
        while (i >= 0 && !Character.isWhitespace(texto.charAt(i))) { n++; i--; }
        if (n > 0) conexion.deleteSurroundingText(n, 0);
    }

    private void borrarPalabraHaciaAdelante() {
        CharSequence texto = conexion.getTextAfterCursor(500, 0); // Límite aumentado a 500
        if (texto == null || texto.length() == 0) return;
        int n = 0, i = 0, len = texto.length();
        while (i < len && Character.isWhitespace(texto.charAt(i))) { n++; i++; }
        while (i < len && !Character.isWhitespace(texto.charAt(i))) { n++; i++; }
        if (n > 0) conexion.deleteSurroundingText(0, n);
    }
}