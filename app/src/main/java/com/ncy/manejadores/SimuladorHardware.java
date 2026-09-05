package com.ncy.manejadores;

import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.inputmethod.InputConnection;

import com.ncy.estado.EstadoTeclado;
import com.ncy.utilidades.Constantes;

import java.util.Set;

public class SimuladorHardware {

    private InputConnection conexion;
    private final EstadoTeclado estado;

    public SimuladorHardware(EstadoTeclado estado) {
        this.estado = estado;
    }

    public void establecerConexion(InputConnection conexion) {
        this.conexion = conexion;
    }

    public void procesarTeclaHardware(int codigo) {
        if (conexion == null) return;
        int metaState = calcularMetaState();

        switch (codigo) {
            case Constantes.CODIGO_FLECHA_ARRIBA, Constantes.CODIGO_CURSOR_ARRIBA -> 
                enviar(KeyEvent.KEYCODE_DPAD_UP, metaState);
            case Constantes.CODIGO_FLECHA_ABAJO, Constantes.CODIGO_CURSOR_ABAJO -> 
                enviar(KeyEvent.KEYCODE_DPAD_DOWN, metaState);
            case Constantes.CODIGO_FLECHA_IZQUIERDA, Constantes.CODIGO_CURSOR_IZQUIERDA -> 
                enviar(KeyEvent.KEYCODE_DPAD_LEFT, metaState);
            case Constantes.CODIGO_FLECHA_DERECHA, Constantes.CODIGO_CURSOR_DERECHA -> 
                enviar(KeyEvent.KEYCODE_DPAD_RIGHT, metaState);
            case Constantes.CODIGO_REPAG -> enviar(KeyEvent.KEYCODE_PAGE_UP, metaState);
            case Constantes.CODIGO_AVPAG -> enviar(KeyEvent.KEYCODE_PAGE_DOWN, metaState);
            case Constantes.CODIGO_HOME -> enviar(KeyEvent.KEYCODE_MOVE_HOME, metaState);
            case Constantes.CODIGO_END -> enviar(KeyEvent.KEYCODE_MOVE_END, metaState);
            case Constantes.CODIGO_SUPRIMIR -> enviar(KeyEvent.KEYCODE_FORWARD_DEL, metaState);
            case Constantes.CODIGO_INSERT -> enviar(KeyEvent.KEYCODE_INSERT, metaState);
            case Constantes.CODIGO_SCROLL_LOCK -> enviar(KeyEvent.KEYCODE_SCROLL_LOCK, metaState);
            case Constantes.CODIGO_UNDO -> enviar(KeyEvent.KEYCODE_Z, KeyEvent.META_CTRL_ON);
            case Constantes.CODIGO_REDO -> enviar(KeyEvent.KEYCODE_Z, KeyEvent.META_CTRL_ON | KeyEvent.META_SHIFT_ON);
            case Constantes.CODIGO_ESC -> procesarEsc(metaState);
            

            default -> {
                // NOTA: Los códigos F1 a F12 son negativos y van en orden descendente.
                // F1 = -131, F12 = -142. Por eso la condición evalúa desde F12 (el más negativo/menor) hasta F1.
                if (codigo >= Constantes.CODIGO_F12 && codigo <= Constantes.CODIGO_F1) {
                    int offset = Constantes.CODIGO_F1 - codigo;
                    enviar(KeyEvent.KEYCODE_F1 + offset, metaState);
                }
            }
        }
    }

    public void enviarAtajoCaracter(int keyCode, int metaState) {
        if (conexion == null) return;
        enviar(keyCode, metaState);
    }

    public void enviarEnter(int metaState) {
        if (conexion == null) return;
        enviar(KeyEvent.KEYCODE_ENTER, metaState);
    }

    public int calcularMetaState() {
        int metaState = 0;
        if (estado.isModificadorActivo(Constantes.CODIGO_SHIFT)) metaState |= KeyEvent.META_SHIFT_ON;
        if (estado.isModificadorActivo(Constantes.CODIGO_CTRL))  metaState |= KeyEvent.META_CTRL_ON;
        if (estado.isModificadorActivo(Constantes.CODIGO_ALT))   metaState |= KeyEvent.META_ALT_ON;
        return metaState;
    }

    private static final Set<Integer> CODIGOS_HARDWARE = Set.of(
        Constantes.CODIGO_ESC, Constantes.CODIGO_UNDO, Constantes.CODIGO_REDO,
        Constantes.CODIGO_REPAG, Constantes.CODIGO_AVPAG, Constantes.CODIGO_HOME, Constantes.CODIGO_END,
        Constantes.CODIGO_SUPRIMIR, Constantes.CODIGO_INSERT, Constantes.CODIGO_SCROLL_LOCK,
        Constantes.CODIGO_FLECHA_ARRIBA, Constantes.CODIGO_FLECHA_ABAJO,
        Constantes.CODIGO_FLECHA_IZQUIERDA, Constantes.CODIGO_FLECHA_DERECHA,
        Constantes.CODIGO_CURSOR_ARRIBA, Constantes.CODIGO_CURSOR_ABAJO,
        Constantes.CODIGO_CURSOR_IZQUIERDA, Constantes.CODIGO_CURSOR_DERECHA,
        Constantes.CODIGO_F1,  Constantes.CODIGO_F2,  Constantes.CODIGO_F3,
        Constantes.CODIGO_F4,  Constantes.CODIGO_F5, 
        Constantes.CODIGO_F6,
        Constantes.CODIGO_F7,  Constantes.CODIGO_F8,  Constantes.CODIGO_F9,
        Constantes.CODIGO_F10, Constantes.CODIGO_F11, Constantes.CODIGO_F12
    );

    public static boolean esCodigo(int codigo) {
        return CODIGOS_HARDWARE.contains(codigo);
    }

    private void procesarEsc(int metaState) {
        if (conexion == null) return;

        android.view.inputmethod.ExtractedText extraido =
                conexion.getExtractedText(new android.view.inputmethod.ExtractedTextRequest(), 0);
        if (extraido != null && extraido.selectionStart != extraido.selectionEnd) {
            conexion.setSelection(extraido.selectionEnd, extraido.selectionEnd);
        } else {
            enviar(KeyEvent.KEYCODE_ESCAPE, metaState);
        }
    }

    private void enviar(int keyCode, int metaState) {
        long t = SystemClock.uptimeMillis();
        boolean conShift = (metaState & KeyEvent.META_SHIFT_ON) != 0;
        boolean conCtrl  = (metaState & KeyEvent.META_CTRL_ON)  != 0;

        if (conShift) conexion.sendKeyEvent(new KeyEvent(t, t, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_SHIFT_LEFT, 0, metaState));
        if (conCtrl)  conexion.sendKeyEvent(new KeyEvent(t, t, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_CTRL_LEFT,  0, metaState));
        
        conexion.sendKeyEvent(new KeyEvent(t, t, KeyEvent.ACTION_DOWN, keyCode, 0, metaState));
        conexion.sendKeyEvent(new KeyEvent(t, t, KeyEvent.ACTION_UP,   keyCode, 0, metaState));
        
        if (conCtrl)  conexion.sendKeyEvent(new KeyEvent(t, t, KeyEvent.ACTION_UP, KeyEvent.KEYCODE_CTRL_LEFT,  0, metaState));
        if (conShift) conexion.sendKeyEvent(new KeyEvent(t, t, KeyEvent.ACTION_UP, KeyEvent.KEYCODE_SHIFT_LEFT, 0, metaState));
    }
}